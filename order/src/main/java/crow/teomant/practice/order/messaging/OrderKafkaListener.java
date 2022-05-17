package crow.teomant.practice.order.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import crow.teomant.practice.order.domain.OrderService;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderKafkaListener {
    private final OrderService orderService;

    @KafkaListener(topics = "order.create.response")
    @SneakyThrows
    public void response(String content) {
        ObjectMapper objectMapper = new ObjectMapper();
        OrderResponse orderResponse = objectMapper.readValue(content, OrderResponse.class);
        if (!orderResponse.getSuccess()) {
            orderService.revert(orderResponse.orderId);
        }
    }

    @Data
    private static class OrderResponse {
        private UUID orderId;
        private Boolean success;
    }
}
