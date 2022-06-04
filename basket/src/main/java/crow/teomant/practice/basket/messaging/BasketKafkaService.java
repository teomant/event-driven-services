package crow.teomant.practice.basket.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import crow.teomant.domain.events.DomainEvents;
import crow.teomant.messages.client.ClientCreated;
import crow.teomant.messages.items.ItemsInBasketChanged;
import crow.teomant.practice.basket.domain.BasketService;
import crow.teomant.practice.basket.domain.events.BasketStateChangedEvent;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
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
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DomainEvents domainEvents;
    private final KafkaTemplate<String, String> template;

    @PostConstruct
    private void postConstruct() {
        domainEvents.listenTo(BasketStateChangedEvent.class, this::basketChanged);
    }

    @SneakyThrows
    private void basketChanged(BasketStateChangedEvent event) {
        template.send("basket.changed", objectMapper.writeValueAsString(
            new ItemsInBasketChanged(event.getClientId(), event.getAdded(), event.getRemoved(), event.getSource())));
    }

    @KafkaListener(topics = "order.create.request")
    public void order(String content) {
        log.warn("order");
        log.warn(content);
        OrderRequest orderRequest = getOrderRequest(content);
        basketService.order(orderRequest.getUserId(), orderRequest.getItems());
    }

    @KafkaListener(topics = "client.created")
    @SneakyThrows
    public void clientCreated(String content) {
        log.warn("client");
        log.warn(content);

        ClientCreated clientCreated = objectMapper.readValue(content, ClientCreated.class);

        basketService.create(clientCreated.getClientId());
    }

    @SneakyThrows
    private OrderRequest getOrderRequest(String content) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(content, OrderRequest.class);
    }


    @Data
    //TODO перенос
    private static class OrderRequest {
        private UUID userId;
        private UUID orderId;
        private Map<UUID, Integer> items;
        private Integer distance;
    }


}
