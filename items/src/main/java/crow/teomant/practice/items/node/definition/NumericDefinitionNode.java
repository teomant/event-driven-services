package crow.teomant.practice.items.node.definition;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import crow.teomant.practice.items.node.value.AbstractValueNode;
import crow.teomant.practice.items.node.value.NumericValueNode;
import lombok.Getter;

@Getter
public class NumericDefinitionNode extends AbstractDefinitionNode {

    @JsonCreator
    public NumericDefinitionNode(@JsonProperty(value = "description") String description,
                                 @JsonProperty(value = "type") String type) {
        super(description, type);
    }

    public NumericDefinitionNode(String description) {
        super(description, "NUMERIC");
    }

    @Override
    public Boolean validate(AbstractValueNode node) {
        return node instanceof NumericValueNode;
    }
}
