package crow.teomant.event.sourcing.history;

import crow.teomant.event.sourcing.event.source.BaseSourceSkipEvent;
import crow.teomant.event.sourcing.source.EventSource;
import java.time.OffsetDateTime;
import java.util.UUID;

public class TestSkipEvent extends TestEvent implements BaseSourceSkipEvent {

    private final UUID skipId;

    public TestSkipEvent(UUID id, OffsetDateTime discriminant, Long version,
                         EventSource eventSource, String testValue, UUID skipId) {
        super(id, discriminant, version, eventSource, testValue);
        this.skipId = skipId;
    }

    public TestSkipEvent(UUID id, OffsetDateTime discriminant, EventSource eventSource, String testValue, UUID skipId) {
        super(id, discriminant, eventSource, testValue);
        this.skipId = skipId;
    }

    @Override
    public UUID getSkipId() {
        return skipId;
    }

    @Override
    public TestState apply(TestState state) {
        return state;
    }
}
