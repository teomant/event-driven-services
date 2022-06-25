package crow.teomant.practice.items.node.search;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import lombok.SneakyThrows;

public abstract class AbstractSearchNode {

    public abstract Object getValue();

    @SneakyThrows
    public static AbstractSearchNode of(Object object) {
        if (object instanceof Map) {
            HashMap<String, AbstractSearchNode> value = new HashMap<>();
            Map<String, Object> map = (Map<String, Object>) object;
            map.forEach((k, v) -> value.put(k, of(v)));
            return new ObjectSearchNode(value);
        } else if (object instanceof Boolean) {
            return new BooleanSearchNode((Boolean) object);
        } else if (object instanceof String) {
            return new StringSearchNode((String) object);
        } else if (object instanceof BigDecimal) {
            return new NumericSearchNode((BigDecimal) object);
        } else if (object instanceof Integer) {
            return new NumericSearchNode(BigDecimal.valueOf((Integer) object));
        } else if (object instanceof Double) {
            return new NumericSearchNode(BigDecimal.valueOf((Double) object));
        } else {
            HashMap<String, AbstractSearchNode> value = new HashMap<>();
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                value.put(field.getName(), of(field.get(object)));
            }

            return new ObjectSearchNode(value);
        }
    }

}
