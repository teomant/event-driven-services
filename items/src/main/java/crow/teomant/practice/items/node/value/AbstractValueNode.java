package crow.teomant.practice.items.node.value;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import lombok.SneakyThrows;

public abstract class AbstractValueNode {

    public abstract Object getValue();

    @SneakyThrows
    public static AbstractValueNode of(Object object) {
        if (object instanceof Map) {
            HashMap<String, AbstractValueNode> value = new HashMap<>();
            Map<String, Object> map = (Map<String, Object>) object;
            map.forEach((k, v) -> value.put(k, of(v)));
            return new ObjectValueNode(value);
        } else if (object instanceof Boolean) {
            return new BooleanValueNode((Boolean) object);
        } else if (object instanceof String) {
            return new StringValueNode((String) object);
        } else if (object instanceof BigDecimal) {
            return new NumericValueNode((BigDecimal) object);
        } else if (object instanceof Integer) {
            return new NumericValueNode(BigDecimal.valueOf((Integer) object));
        } else if (object instanceof Double) {
            return new NumericValueNode(BigDecimal.valueOf((Double) object));
        } else {
            HashMap<String, AbstractValueNode> value = new HashMap<>();
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                value.put(field.getName(), of(field.get(object)));
            }

            return new ObjectValueNode(value);
        }
    }

}
