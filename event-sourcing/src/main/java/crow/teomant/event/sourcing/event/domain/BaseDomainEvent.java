package crow.teomant.event.sourcing.event.domain;

import crow.teomant.event.sourcing.source.EventSource;
import java.io.Serializable;
import java.util.UUID;

public abstract class BaseDomainEvent implements Serializable {

    private final UUID id;
    private final EventSource eventSource;

    protected BaseDomainEvent(UUID id, EventSource eventSource) {
        this.id = id;
        this.eventSource = eventSource;
    }

    public UUID getId() {
        return id;
    }

    public EventSource getEventSource() {
        return eventSource;
    }

}
