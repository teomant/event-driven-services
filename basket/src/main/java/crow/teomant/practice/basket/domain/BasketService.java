package crow.teomant.practice.basket.domain;

import java.util.Map;
import java.util.UUID;

public interface BasketService {

    void create(UUID userId);

    Basket change(UUID userId, Map<UUID, Integer> items);

    void order(UUID orderId, UUID userId, Map<UUID, Integer> items);

    void revertOrder(UUID orderId, UUID userId, Map<UUID, Integer> items);

    Basket get(UUID id);
}
