package crow.teomant.practice.items.web;

import crow.teomant.practice.items.domain.Item;
import crow.teomant.practice.items.domain.ItemType;
import crow.teomant.practice.items.node.definition.AbstractDefinitionNode;
import crow.teomant.practice.items.node.definition.BooleanDefinitionNode;
import crow.teomant.practice.items.node.definition.NumericDefinitionNode;
import crow.teomant.practice.items.node.definition.ObjectDefinitionNode;
import crow.teomant.practice.items.node.definition.StringDefinitionNode;
import crow.teomant.practice.items.node.value.AbstractValueNode;
import crow.teomant.practice.items.persistence.ItemMongoRepositoryAdapter;
import crow.teomant.practice.items.persistence.ItemTypeDocument;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class ItemController {
    private final ItemMongoRepositoryAdapter repository;

    @GetMapping("/create/{typeId}")
    public UUID create(@PathVariable("typeId") UUID typeId) {
        ItemType type = repository.getType(typeId);
        HashMap<String, Object> inner = new HashMap<>();
        inner.put("test", "test");
        HashMap<String, Object> outer = new HashMap<>();
        outer.put("test", inner);

        return repository.save(new Item(UUID.randomUUID(), AbstractValueNode.of(outer), type));
    }

    @GetMapping("/createType")
    public UUID createType() {
        BooleanDefinitionNode booleanDefinitionNode = new BooleanDefinitionNode("");
        NumericDefinitionNode numericDefinitionNode = new NumericDefinitionNode("");
        StringDefinitionNode stringDefinitionNode = new StringDefinitionNode("");
        HashMap<String, AbstractDefinitionNode> fields = new HashMap<>();
        fields.put("test", new NumericDefinitionNode(""));
        fields.put("test2", new StringDefinitionNode(""));
        ObjectDefinitionNode objectDefinitionNode = new ObjectDefinitionNode("", fields);
        HashMap<String, AbstractDefinitionNode> outerFields = new HashMap<>();
        outerFields.put("boolean", booleanDefinitionNode);
        outerFields.put("numeric", numericDefinitionNode);
        outerFields.put("string", stringDefinitionNode);
        outerFields.put("object", objectDefinitionNode);
        ObjectDefinitionNode outer = new ObjectDefinitionNode("", outerFields);

        return repository.save(new ItemType(UUID.randomUUID(), outer));
    }

    @GetMapping("/getType/{id}")
    public ItemType getType(@PathVariable("id") UUID id) {

        return repository.getType(id);
    }

    @GetMapping("/get/{id}")
    public Item get(@PathVariable("id") UUID id) {

        return repository.get(id);
    }

    @GetMapping("/get")
    public Object get() {
        return AbstractValueNode.of(new BigDecimal(1));
//        return repository.get().getDescription().getValue();
    }

}
