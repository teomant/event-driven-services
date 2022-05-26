package crow.teomant.event.sourcing.history;

import crow.teomant.event.sourcing.source.EventSource;
import java.time.OffsetDateTime;
import java.util.UUID;

public class TestBadEvent extends TestEvent {

    public TestBadEvent(UUID id, OffsetDateTime discriminant, Long version, EventSource eventSource, String testValue) {
        super(id, discriminant, version, eventSource, testValue);
    }

    public TestBadEvent(UUID id, OffsetDateTime discriminant, EventSource eventSource, String testValue) {
        super(id, discriminant, eventSource, testValue);
    }

    @Override
    public TestState apply(TestState state) {
        throw new IllegalStateException("TEST");
    }
}
