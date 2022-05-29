package crow.teomant.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;


public class EventSource implements crow.teomant.event.sourcing.source.EventSource {

    private final UUID correlationId;
    @Override
    public UUID getCorrelationId() {
        return correlationId;
    }

    @JsonCreator
    public EventSource(@JsonProperty("correlationId") UUID correlationId) {
        this.correlationId = correlationId;
    }
}
