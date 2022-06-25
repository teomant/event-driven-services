package crow.teomant.practice.items.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import crow.teomant.practice.items.domain.Item;
import crow.teomant.practice.items.domain.ItemType;
import crow.teomant.practice.items.node.definition.AbstractDefinitionNode;
import crow.teomant.practice.items.node.search.AbstractSearchNode;
import crow.teomant.practice.items.node.value.AbstractValueNode;
import java.util.HashMap;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ItemMongoRepositoryAdapter {
    private final ItemMongoRepository repository;
    private final ItemTypeMongoRepository typeRepository;
    private final MongoTemplate mongoTemplate;
    private final ObjectMapper mapper;

    public UUID save(Item item) {
        return repository.save(new ItemDocument(item.getId(), item.getDescription().getValue(),
            new ItemTypeDocument(item.getItemType().getId(),
                mapper.convertValue(item.getItemType().getDefinition(), HashMap.class)))).getId();
    }

    public UUID save(ItemType itemType) {
        return typeRepository.save(
                new ItemTypeDocument(itemType.getId(), mapper.convertValue(itemType.getDefinition(), HashMap.class)))
            .getId();
    }

    public Item find() {
        Query query = new Query();
        HashMap<Object, Object> inner = new HashMap<>();
        inner.put("value", "in");
        inner.put("path", "test");
        HashMap<Object, Object> middle = new HashMap<>();
        middle.put("path", "test");
        middle.put("value", inner);
        HashMap<Object, Object> top = new HashMap<>();
        top.put("path", "description");
        top.put("value", middle);

        query.addCriteria(CriteriaBuilder.build(AbstractSearchNode.of(top)));
        ItemDocument one = mongoTemplate.find(query, ItemDocument.class).get(0);

        return new Item(one.getId(), AbstractValueNode.of(one.getDescription()),
            new ItemType(one.getItemTypeDocument().getId(),
                mapper.convertValue(one.getItemTypeDocument().getDefinition(), AbstractDefinitionNode.class)));
    }

    public Item get(UUID id) {
        ItemDocument one = mongoTemplate.findById(id, ItemDocument.class);

        return new Item(one.getId(), AbstractValueNode.of(one.getDescription()),
            new ItemType(one.getItemTypeDocument().getId(),
                mapper.convertValue(one.getItemTypeDocument().getDefinition(), AbstractDefinitionNode.class)));
    }

    public ItemType getType(UUID id) {
        return typeRepository.findById(id)
            .map(docuemnt -> new ItemType(docuemnt.getId(),
                mapper.convertValue(docuemnt.getDefinition(), AbstractDefinitionNode.class))).get();
    }
}