package crow.teomant.event.sourcing.event.source;

import crow.teomant.event.sourcing.source.EventSource;
import crow.teomant.event.sourcing.state.State;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

public abstract class BaseSourceEvent<D extends Comparable<D>, S extends State, ES extends EventSource>
    implements Serializable {

    private final UUID id;
    private final D discriminant;
    private final EventSource eventSource;

    private final OffsetDateTime createdAt = OffsetDateTime.now();

    private Long version;

    protected BaseSourceEvent(UUID id, D discriminant, Long version, EventSource eventSource) {
        this.id = id;
        this.discriminant = discriminant;
        this.version = version;
        this.eventSource = eventSource;
    }

    protected BaseSourceEvent(UUID id, D discriminant, EventSource eventSource) {
        this.id = id;
        this.discriminant = discriminant;
        this.version = null;
        this.eventSource = eventSource;
    }

    public abstract S apply(S state);

    public UUID getId() {
        return id;
    }

    public D getDiscriminant() {
        return discriminant;
    }

    public EventSource getEventSource() {
        return eventSource;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

}
