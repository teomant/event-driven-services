package crow.teomant.practice.items.node.definition;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import crow.teomant.practice.items.node.value.AbstractValueNode;
import crow.teomant.practice.items.node.value.BooleanValueNode;
import lombok.Getter;

@Getter
public class BooleanDefinitionNode extends AbstractDefinitionNode {

    @JsonCreator
    public BooleanDefinitionNode(@JsonProperty(value = "description") String description,
                                 @JsonProperty(value = "type") String type) {
        super(description, type);
    }

    public BooleanDefinitionNode(String description) {
        super(description, "BOOLEAN");
    }

    @Override
    public Boolean validate(AbstractValueNode node) {
        return node instanceof BooleanValueNode;
    }
}
