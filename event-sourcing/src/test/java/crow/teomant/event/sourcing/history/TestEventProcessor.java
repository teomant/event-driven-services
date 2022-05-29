package crow.teomant.event.sourcing.history;

import crow.teomant.event.sourcing.stream.ApplicationPolicy;
import crow.teomant.event.sourcing.stream.ApplicationResult;
import crow.teomant.event.sourcing.stream.EventProcessor;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;

public class TestEventProcessor extends EventProcessor<OffsetDateTime, TestState, TestEventSource, TestEvent> {
    public TestEventProcessor(List<TestEvent> events,
                              ApplicationPolicy applicationPolicy, TestState state, List<TestEvent> newEvents,
                              List<ApplicationResult> results) {
        super(events, applicationPolicy, state, newEvents, results);
    }
}
