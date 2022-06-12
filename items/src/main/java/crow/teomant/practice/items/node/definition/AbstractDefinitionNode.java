package crow.teomant.practice.items.node.definition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import crow.teomant.practice.items.node.value.AbstractValueNode;
import lombok.Getter;


@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME, property = "type",
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    visible = true
)
@JsonSubTypes(value = {
    @JsonSubTypes.Type(value = BooleanDefinitionNode.class, name = "NUMERIC"),
    @JsonSubTypes.Type(value = NumericDefinitionNode.class, name = "BOOLEAN"),
    @JsonSubTypes.Type(value = ObjectDefinitionNode.class, name = "OBJECT"),
    @JsonSubTypes.Type(value = StringDefinitionNode.class, name = "STRING")
})
public abstract class AbstractDefinitionNode {

    @Getter
    private final String description;
    @Getter
    private final String type;

    public AbstractDefinitionNode(String description, String type) {
        this.description = description;
        this.type = type;
    }

    public abstract Boolean validate(AbstractValueNode node);
}
