package crow.teomant.practice.basket.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import crow.teomant.practice.basket.domain.BasketService;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BasketKafkaService {

    private final BasketService basketService;
    private final KafkaTemplate<String, String> template;

    @KafkaListener(topics = "order.create.request")
    public void order(String content) {
        log.warn("order");
        log.warn(content);
        OrderRequest orderRequest = getRequest(content);
        try {
            basketService.order(orderRequest.getOrderId(), orderRequest.getUserId(), orderRequest.getItems());
            template.send("order.create.response", getResponse(orderRequest.orderId, true));
        } catch (Exception e) {
            template.send("order.create.response", getResponse(orderRequest.orderId, false));
        }
    }

    @SneakyThrows
    private OrderRequest getRequest(String content) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(content, OrderRequest.class);
    }

    @SneakyThrows
    private String getResponse(UUID id, Boolean result) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(new OrderResponse(id, result));
    }

    @KafkaListener(topics = "order.revert.request")
    public void revertOrder(String content) {
        log.warn("revert");
        log.warn(content);
        OrderRequest orderRequest = getRequest(content);
        try {
            basketService.revertOrder(orderRequest.getOrderId(), orderRequest.getUserId(), orderRequest.getItems());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Data
    private static class OrderRequest {
        private UUID userId;
        private UUID orderId;
        private Map<UUID, Integer> items;
        private Integer distance;
    }

    @Data
    @AllArgsConstructor
    private static class OrderResponse {
        private UUID orderId;
        private Boolean success;
    }


}
