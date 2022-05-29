package crow.teomant.client.domain.events.domain;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import crow.teomant.client.domain.history.ClientState;
import crow.teomant.event.sourcing.event.domain.BaseDomainEvent;
import crow.teomant.event.sourcing.event.source.BaseSourceEvent;
import crow.teomant.event.sourcing.source.EventSource;
import java.time.OffsetDateTime;
import java.util.UUID;


@JsonTypeInfo(
    use = JsonTypeInfo.Id.MINIMAL_CLASS
)
public abstract class BaseClientDomainEvent extends BaseDomainEvent {
    protected BaseClientDomainEvent(UUID id, EventSource eventSource) {
        super(id, eventSource);
    }
}
