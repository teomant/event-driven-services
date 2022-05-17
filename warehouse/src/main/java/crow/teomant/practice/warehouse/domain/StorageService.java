package crow.teomant.practice.warehouse.domain;

import java.util.Map;
import java.util.UUID;

public interface StorageService {

    void create(UUID userId, Integer distance);

    Storage change(UUID userId, Map<UUID, Integer> items);

    void order(UUID orderId, Map<UUID, Integer> items, Integer distance);

    void revertOrder(UUID orderId);

    Storage get(UUID id);
}
