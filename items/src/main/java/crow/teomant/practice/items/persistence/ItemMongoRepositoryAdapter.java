package crow.teomant.practice.items.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import crow.teomant.practice.items.domain.Item;
import crow.teomant.practice.items.domain.ItemType;
import crow.teomant.practice.items.node.definition.AbstractDefinitionNode;
import crow.teomant.practice.items.node.value.AbstractValueNode;
import java.util.HashMap;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ItemMongoRepositoryAdapter {
    private final ItemMongoRepository repository;
    private final ItemTypeMongoRepository typeRepository;
    private final MongoTemplate mongoTemplate;
    private final ObjectMapper mapper;

    public void save(Item item) {
        repository.save(new ItemDocument(item.getId(), item.getDescription().getValue()));
    }

    public UUID save(ItemType itemType) {
        return typeRepository.save(
                new ItemTypeDocument(itemType.getId(), mapper.convertValue(itemType.getDefinition(), HashMap.class)))
            .getId();
    }

    public void Type(Item item) {
        repository.save(new ItemDocument(item.getId(), item.getDescription().getValue()));
    }

    public Item get() {
        Query query = new Query();
        query.addCriteria(Criteria.where("description.test.test").is("test"));
        ItemDocument one = mongoTemplate.find(query, ItemDocument.class).get(0);

        return new Item(one.getId(), AbstractValueNode.of(one.getDescription()));
    }

    public ItemType getType(UUID id) {
        return typeRepository.findById(id)
            .map(docuemnt -> new ItemType(docuemnt.getId(),
                mapper.convertValue(docuemnt.getDefinition(), AbstractDefinitionNode.class))).get();
    }
}
