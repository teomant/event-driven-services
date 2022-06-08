package crow.teomant.practice.order.domain;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface OrderService {

    UUID create(UUID userId, List<Order.Piece> pieces, UUID distance);

    void start(UUID id);

    void revert(UUID id);

    Order get(UUID id);
}
