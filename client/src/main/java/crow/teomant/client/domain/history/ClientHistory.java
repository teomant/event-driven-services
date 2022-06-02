package crow.teomant.client.domain.history;

import com.fasterxml.jackson.databind.ObjectMapper;
import crow.teomant.client.domain.events.domain.RegisterClientDomainEvent;
import crow.teomant.client.domain.events.domain.UpdateClientDomainEvent;
import crow.teomant.client.domain.events.source.BaseClientSourceEvent;
import crow.teomant.client.domain.events.source.RegisterClientSourceEvent;
import crow.teomant.client.domain.events.source.UpdateClientSourceEvent;
import crow.teomant.common.EventSource;
import crow.teomant.event.sourcing.history.History;
import crow.teomant.event.sourcing.stream.ApplicationPolicy;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ClientHistory extends History<OffsetDateTime, ClientState, EventSource, BaseClientSourceEvent> {

    public ClientHistory(UUID id, ClientState state, List<BaseClientSourceEvent> events, ObjectMapper objectMapper) {
        super(id, new ClientEventStream(state, events, objectMapper));
    }

    public void register(String login, UUID preferedWarehouse, String name, EventSource eventSource) {
        this.eventStream.addEvents(Collections.singletonList(
                new RegisterClientSourceEvent(
                    UUID.randomUUID(), OffsetDateTime.now(), eventSource, login, preferedWarehouse,
                    name)
            ),
            ApplicationPolicy.RETHROW);

        domainEvents.add(
            new RegisterClientDomainEvent(UUID.randomUUID(), eventSource, this.getId(), login, preferedWarehouse)
        );
    }

    public void update(UUID preferedWarehouse, String name, EventSource eventSource) {
        this.eventStream.addEvents(Collections.singletonList(
                new UpdateClientSourceEvent(
                    UUID.randomUUID(), OffsetDateTime.now(), eventSource, preferedWarehouse,
                    name
                )
            ),
            ApplicationPolicy.RETHROW);

        domainEvents.add(
            new UpdateClientDomainEvent(UUID.randomUUID(), eventSource, this.getId(), preferedWarehouse)
        );
    }
}
