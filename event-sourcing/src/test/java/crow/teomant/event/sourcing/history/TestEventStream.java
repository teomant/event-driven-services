package crow.teomant.event.sourcing.history;

import crow.teomant.event.sourcing.stream.ApplicationPolicy;
import crow.teomant.event.sourcing.stream.ApplicationResult;
import crow.teomant.event.sourcing.stream.EventProcessor;
import crow.teomant.event.sourcing.stream.EventStream;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;

public class TestEventStream extends EventStream<OffsetDateTime, TestState, TestEventSource, TestEvent> {

    public TestEventStream(TestState state, List<TestEvent> events) {
        super(state, events);
    }

    @Override
    protected TestState getStateClone(TestState state) {
        return new TestState(state.getId(), state.getInitialVersion(), state.getTestValue());
    }

    @Override
    protected EventProcessor<OffsetDateTime, TestState, TestEventSource, TestEvent> getEventProcessor(
        List<TestEvent> events, ApplicationPolicy policy, TestState state,
        List<TestEvent> newEvents, List<ApplicationResult> results) {
        return new TestEventProcessor(events, policy, state, newEvents, results);

    }
}
