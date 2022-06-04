package crow.teomant.practice.basket.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.Value;
import org.apache.commons.collections4.MapUtils;

@Value
public class Basket {
    UUID id;
    Map<UUID, Integer> items;

    public void changeItems(Map<UUID, Integer> change) {
        items.putAll(change);
        cleanIfNoItems();
    }

    private void cleanIfNoItems() {
        items.keySet().forEach(id -> {
            if (items.get(id) == 0) {
                items.remove(id);
            }
        });
    }

    public Map<UUID, Integer> getItems() {
        return new HashMap<>(items);
    }

    public void order(Map<UUID, Integer> order) {
        order.forEach((id, count) -> {
            if (!items.containsKey(id)) {
                return;
            }
            items.put(id, count < items.get(id) ? items.get(id) - count : 0);
        });
        cleanIfNoItems();
    }

}
