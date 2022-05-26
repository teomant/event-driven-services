package crow.teomant.event.sourcing.history;

import crow.teomant.event.sourcing.source.EventSource;
import java.util.UUID;

public class TestEventSource implements EventSource {
    private final UUID correlationId;

    public TestEventSource(UUID correlationId) {
        this.correlationId = correlationId;
    }

    @Override
    public UUID getCorrelationId() {
        return correlationId;
    }
}
