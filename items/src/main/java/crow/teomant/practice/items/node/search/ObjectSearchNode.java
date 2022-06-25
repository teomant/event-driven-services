package crow.teomant.practice.items.node.search;


import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ObjectSearchNode extends AbstractSearchNode {

    private final Map<String, AbstractSearchNode> value;

    public Object getValue() {
        return value;
    }

    public AbstractSearchNode get(String key) {
        return value.get(key);
    }
}
