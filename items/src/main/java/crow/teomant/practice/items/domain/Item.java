package crow.teomant.practice.items.domain;

import crow.teomant.practice.items.node.value.AbstractValueNode;
import java.util.UUID;
import lombok.Value;

@Value
public class Item {
    UUID id;
    AbstractValueNode description;
}
