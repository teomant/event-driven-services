package crow.teomant.practice.order.domain;

import java.util.Map;
import java.util.UUID;

public interface OrderService {

    UUID create(UUID userId, Map<UUID, Integer> items, Integer distance);

    void start(UUID id);

    void revert(UUID id);

    Order get(UUID id);
}
