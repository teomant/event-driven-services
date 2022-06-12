package crow.teomant.practice.items.node.definition;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import crow.teomant.practice.items.node.value.AbstractValueNode;
import crow.teomant.practice.items.node.value.StringValueNode;
import lombok.Getter;

@Getter
public class StringDefinitionNode extends AbstractDefinitionNode {

    @JsonCreator
    public StringDefinitionNode(@JsonProperty(value = "description") String description,
                                @JsonProperty(value = "type") String type) {
        super(description, type);
    }

    public StringDefinitionNode(String description) {
        super(description, "STRING");
    }


    @Override
    public Boolean validate(AbstractValueNode node) {
        return node instanceof StringValueNode;
    }
}
