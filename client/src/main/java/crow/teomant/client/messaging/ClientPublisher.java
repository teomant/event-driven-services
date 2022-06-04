package crow.teomant.client.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import crow.teomant.client.domain.events.domain.RegisterClientDomainEvent;
import crow.teomant.client.domain.events.domain.UpdateClientDomainEvent;
import crow.teomant.domain.events.DomainEvents;
import crow.teomant.messages.client.ClientCreated;
import crow.teomant.messages.client.ClientUpdated;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientPublisher {

    private final DomainEvents domainEvents;
    private final KafkaTemplate<String, String> template;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    private void postConstruct() {
        domainEvents.listenTo(RegisterClientDomainEvent.class, this::registered);
        domainEvents.listenTo(UpdateClientDomainEvent.class, this::updated);
    }

    @SneakyThrows
    private void registered(RegisterClientDomainEvent event) {
        template.send("client.created", objectMapper.writeValueAsString(
            new ClientCreated(event.getClientId(), event.getPreferedWarehouse(), event.getEventSource())));
    }

    @SneakyThrows
    private void updated(UpdateClientDomainEvent event) {
        template.send("client.updated", objectMapper.writeValueAsString(
            new ClientUpdated(event.getClientId(), event.getPreferedWarehouse(), event.getEventSource())));
    }
}
