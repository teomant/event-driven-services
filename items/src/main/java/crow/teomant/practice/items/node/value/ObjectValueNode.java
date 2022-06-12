package crow.teomant.practice.items.node.value;


import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ObjectValueNode extends AbstractValueNode {

    private final Map<String, AbstractValueNode> value;

    public Object getValue() {
        HashMap<String, Object> toReturn = new HashMap<>();
        value.forEach((k, v) -> toReturn.put(k, v.getValue()));

        return toReturn;
    }
}
