package crow.teomant.practice.order.domain;

import java.util.UUID;

public interface OrderRepository {
    void save(Order order);

    Order get(UUID id);
}
