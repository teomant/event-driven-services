package crow.teomant.event.sourcing.history;

import crow.teomant.event.sourcing.stream.ApplicationPolicy;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class TestHistory extends History<OffsetDateTime, TestState, TestEventSource, TestEvent> {
    protected TestHistory(UUID id, TestState state, List<TestEvent> events) {
        super(id, state, events);
    }

    public void applyNewEvent() {
        this.eventStream.addEvents(
            Collections.singletonList(
                new TestEvent(UUID.randomUUID(), OffsetDateTime.now(), new TestEventSource(UUID.randomUUID()),
                    "newValue")),
            ApplicationPolicy.RETHROW);
    }

    public void applyAndSkipEvent() {
        UUID toSkip = UUID.randomUUID();
        TestEventSource eventSource = new TestEventSource(UUID.randomUUID());
        this.eventStream.addEvents(
            Arrays.asList(
                new TestEvent(toSkip, OffsetDateTime.now(), eventSource, "newValue"),
                new TestSkipEvent(UUID.randomUUID(), OffsetDateTime.now(), eventSource, "", toSkip)),
            ApplicationPolicy.RETHROW);
    }

    public void skip(UUID toSkip) {
        this.eventStream.addEvents(
            Arrays.asList(
                new TestSkipEvent(UUID.randomUUID(), OffsetDateTime.now(), new TestEventSource(UUID.randomUUID()), "",
                    toSkip)),
            ApplicationPolicy.RETHROW);
    }

    public void anotherNewEvent() {
        this.eventStream.addEvents(
            Collections.singletonList(
                new TestEvent(UUID.randomUUID(), OffsetDateTime.now(), new TestEventSource(UUID.randomUUID()),
                    "anotherNewValue")),
            ApplicationPolicy.RETHROW);
    }

    @Override
    protected TestEventStream getEventStream(TestState state,
                                             List<TestEvent> events) {
        return new TestEventStream(getComparator(), state, events);
    }

    @Override
    protected Comparator<TestEvent> getComparator() {
        return Comparator.comparing(TestEvent::getDiscriminant);
    }

    public void throwsException() {
        this.eventStream.addEvents(
            Collections.singletonList(
                new TestBadEvent(UUID.randomUUID(), OffsetDateTime.now(), new TestEventSource(UUID.randomUUID()),
                    "newValue")),
            ApplicationPolicy.RETHROW);

    }

    public void dontThrowsException() {
        this.eventStream.addEvents(
            Collections.singletonList(
                new TestBadEvent(UUID.randomUUID(), OffsetDateTime.now(), new TestEventSource(UUID.randomUUID()),
                    "newValue")),
            ApplicationPolicy.NOT_APPLY);

    }

    public void applyNewEvents() {
        this.eventStream.addEvents(
            Arrays.asList(
                new TestEvent(UUID.randomUUID(), OffsetDateTime.now(), new TestEventSource(UUID.randomUUID()),
                    "newValue"),
                new TestEvent(UUID.randomUUID(), OffsetDateTime.now().plusDays(1),
                    new TestEventSource(UUID.randomUUID()),
                    "newValue2")),
            ApplicationPolicy.RETHROW);
    }
}
