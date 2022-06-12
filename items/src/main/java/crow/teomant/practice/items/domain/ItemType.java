package crow.teomant.practice.items.domain;

import crow.teomant.practice.items.node.definition.AbstractDefinitionNode;
import java.util.UUID;
import lombok.Value;

@Value
public class ItemType {
    UUID id;
    AbstractDefinitionNode definition;
}
