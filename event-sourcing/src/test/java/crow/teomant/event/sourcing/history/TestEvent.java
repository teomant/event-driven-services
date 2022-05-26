package crow.teomant.event.sourcing.history;

import crow.teomant.event.sourcing.event.source.BaseSourceEvent;
import crow.teomant.event.sourcing.source.EventSource;
import java.time.OffsetDateTime;
import java.util.UUID;

public class TestEvent extends BaseSourceEvent<OffsetDateTime, TestState, TestEventSource> {

    private final String testValue;

    public TestEvent(UUID id, OffsetDateTime discriminant, Long version,
                     EventSource eventSource, String testValue) {
        super(id, discriminant, version, eventSource);
        this.testValue = testValue;
    }

    public TestEvent(UUID id, OffsetDateTime discriminant, EventSource eventSource, String testValue) {
        super(id, discriminant, eventSource);
        this.testValue = testValue;
    }

    @Override
    public TestState apply(TestState state) {
        state.setTestValue(testValue);

        return state;
    }

    public String getTestValue() {
        return testValue;
    }
}
