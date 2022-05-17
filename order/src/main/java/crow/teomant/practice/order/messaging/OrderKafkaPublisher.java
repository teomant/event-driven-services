package crow.teomant.practice.order.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import crow.teomant.practice.order.domain.Order;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderKafkaPublisher {

    private final KafkaTemplate<String, String> template;

    @SneakyThrows
    private String getRequest(Order order) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(
            new OrderRequest(order.getUserId(), order.getId(), order.getItems(), order.getDistance())
        );
    }

    public void revert(Order order) {
        template.send("order.revert.request", getRequest(order));
    }

    public void start(Order order) {
        template.send("order.create.request", getRequest(order));
    }

    @Data
    @AllArgsConstructor
    private static class OrderRequest {
        private UUID userId;
        private UUID orderId;
        private Map<UUID, Integer> items;
        private Integer distance;
    }

}
