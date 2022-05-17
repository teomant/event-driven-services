package crow.teomant.practice.order.domain;

import java.util.Map;
import java.util.UUID;
import lombok.Value;

@Value
public class Order {
    UUID id;
    UUID userId;
    Map<UUID, Integer> items;
    Integer distance;
}
