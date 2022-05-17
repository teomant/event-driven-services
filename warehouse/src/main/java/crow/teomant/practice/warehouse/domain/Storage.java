package crow.teomant.practice.warehouse.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.Value;

@Value
public class Storage {
    UUID id;
    Map<UUID, Integer> items;
    Map<UUID, Map<UUID, Integer>> orders;
    Integer distance;

    public void changeItems(Map<UUID, Integer> change) {
        items.putAll(change);
        items.keySet().forEach(id -> {
            if (items.get(id) == 0) {
                items.remove(id);
            }
        });
    }

    public Map<UUID, Integer> getItems() {
        return Collections.unmodifiableMap(items);
    }

    public Map<UUID, Integer> order(UUID orderId, Map<UUID, Integer> items) {
        if (orders.containsKey(orderId)) {
            throw new IllegalStateException();
        }

        Map<UUID, Integer> left = new HashMap<>();
        Map<UUID, Integer> inOrder = new HashMap<>();
        items.forEach((id, count) -> {
            Integer exist = this.items.get(id);
            if (exist > count) {
                this.items.put(id, exist - count);
                inOrder.put(id, count);
            } else {
                this.items.remove(id);
                inOrder.put(id, exist);
                if (!exist.equals(count)) {
                    left.put(id, count - exist);
                }
            }
        });

        orders.put(orderId, inOrder);
        return left;
    }

    public void revertOrder(UUID orderId) {
        Optional.ofNullable(orders.get(orderId)).ifPresent(items -> {
            items.forEach((id, count) -> {
                Integer current = this.items.getOrDefault(id, 0);
                this.items.put(id, count + current);
            });
        });

        orders.remove(orderId);
    }
}
