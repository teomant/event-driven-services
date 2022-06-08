package crow.teomant.practice.order.domain;

import crow.teomant.practice.order.messaging.OrderKafkaPublisher;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final OrderKafkaPublisher publisher;

    @Override
    public UUID create(UUID userId, List<Order.Piece> pieces, UUID distance) {
        Order order = new Order(UUID.randomUUID(), userId, pieces, distance);
        repository.save(order);

        return order.getId();
    }

    @Override
    public void start(UUID id) {
        publisher.start(repository.get(id));
    }

    @Override
    public void revert(UUID id) {
        Optional.ofNullable(repository.get(id)).ifPresent(publisher::revert);
    }

    @Override
    public Order get(UUID id) {
        return repository.get(id);
    }
}
