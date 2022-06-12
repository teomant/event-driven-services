package crow.teomant.practice.items.node.definition;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import crow.teomant.practice.items.node.value.AbstractValueNode;
import crow.teomant.practice.items.node.value.ObjectValueNode;
import java.util.Map;
import lombok.Getter;

@Getter
public class ObjectDefinitionNode extends AbstractDefinitionNode {

    private final Map<String, AbstractDefinitionNode> fields;

    @JsonCreator
    public ObjectDefinitionNode(@JsonProperty(value = "description") String description,
                                @JsonProperty(value = "fields") Map<String, AbstractDefinitionNode> fields,
                                @JsonProperty(value = "type") String type) {
        super(description, type);
        this.fields = fields;
    }

    public ObjectDefinitionNode(String description, Map<String, AbstractDefinitionNode> fields) {
        super(description, "OBJECT");
        this.fields = fields;
    }


    @Override
    public Boolean validate(AbstractValueNode node) {
        boolean correctInstance = node instanceof ObjectValueNode;

        if (!correctInstance) {
            return false;
        }

        Map<String, Object> nodeValue = (Map<String, Object>) node.getValue();

        if (!nodeValue.keySet().equals(fields.keySet())) {
            return false;
        }

        return fields.entrySet().stream()
            .allMatch(entry -> entry.getValue().validate(AbstractValueNode.of(nodeValue.get(entry.getKey()))));
    }
}
