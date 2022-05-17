package crow.teomant.practice.basket.domain;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.Value;

@Value
public class Basket {
    UUID userId;
    Map<UUID, Integer> items;
    Set<UUID> orders;

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

    public void order(UUID orderId, Map<UUID, Integer> order) {
        order.forEach((id, count) -> {
            if (!items.containsKey(id)) {
                throw new IllegalStateException();
            }
            if (items.get(id) < count) {
                throw new IllegalStateException();
            }
        });
        if (orders.contains(orderId)) {
            throw new IllegalStateException();
        }
        order.forEach((id, count) -> {
            Integer inBasket = items.get(id);
            items.put(id, inBasket - count);
        });
        orders.add(orderId);
        items.keySet().forEach(id -> {
            if (items.get(id) == 0) {
                items.remove(id);
            }
        });
    }

    public void revertOrder(UUID orderId, Map<UUID, Integer> order) {
        if (orders.contains(orderId)) {
            order.forEach((id, count) -> {
                Integer inBasket = items.getOrDefault(id, 0);
                items.put(id, inBasket + count);
            });
        }
        orders.remove(orderId);
    }
}
