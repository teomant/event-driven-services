package crow.teomant.practice.order.domain;

import java.util.List;
import java.util.UUID;
import lombok.Value;

@Value
public class Order {
    UUID id;
    UUID userId;
    List<Piece> pieces;
    UUID destination;

    @Value
    public static class Piece {
        UUID itemId;
        Long price;
        Integer number;
    }
}
