package crow.teomant.practice.basket.domain;

import java.util.Map;
import java.util.UUID;

public interface BasketService {

    void create(UUID clientId);

    Basket change(UUID clientId, Map<UUID, Integer> items);

    void order(UUID clientId, Map<UUID, Integer> items);

    Basket get(UUID id);
}
