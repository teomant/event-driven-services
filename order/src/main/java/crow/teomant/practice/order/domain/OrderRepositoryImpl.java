package crow.teomant.practice.order.domain;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final Map<UUID, Order> storage = new ConcurrentHashMap<>();

    @Override
    public void save(Order order) {
        storage.put(order.getId(), order);
    }

    @Override
    public Order get(UUID id) {
        return storage.get(id);
    }
}
