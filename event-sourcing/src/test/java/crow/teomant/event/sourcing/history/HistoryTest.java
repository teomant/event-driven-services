package crow.teomant.event.sourcing.history;

import static java.util.UUID.randomUUID;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HistoryTest {

    @Test
    public void getState() {
        UUID id = randomUUID();
        TestHistory history = new TestHistory(id, new TestState(id, 0L, ""), Collections.emptyList());

        TestState state = history.calculateLastState();

        Assertions.assertEquals(state.getTestValue(), "");
        Assertions.assertEquals(state.getVersion(), 0);
    }


    @Test
    public void eventApplied() {
        UUID id = randomUUID();
        TestHistory history = new TestHistory(id, new TestState(id, 0L, ""), Collections.singletonList(
            new TestEvent(randomUUID(), OffsetDateTime.now(), 1L, new TestEventSource(randomUUID()), "Applied")));

        TestState state = history.calculateLastState();

        Assertions.assertEquals(state.getTestValue(), "Applied");
        Assertions.assertEquals(state.getVersion(), 0);
    }


    @Test
    public void eventsApplied() {
        UUID id = randomUUID();
        TestHistory history = new TestHistory(id, new TestState(id, 0L, ""), Arrays.asList(
            new TestEvent(randomUUID(), OffsetDateTime.now().minusDays(1), 1L, new TestEventSource(randomUUID()),
                "Applied"),
            new TestEvent(randomUUID(), OffsetDateTime.now(), 2L, new TestEventSource(randomUUID()), "Applied2"))
        );

        TestState state = history.calculateLastState();

        Assertions.assertEquals(state.getTestValue(), "Applied2");
        Assertions.assertEquals(state.getVersion(), 0);
    }


    @Test
    public void eventsInPastApplied() {
        UUID id = randomUUID();
        TestHistory history = new TestHistory(id, new TestState(id, 0L, ""), Arrays.asList(
            new TestEvent(randomUUID(), OffsetDateTime.now().minusDays(1), 1L, new TestEventSource(randomUUID()),
                "Applied"),
            new TestEvent(randomUUID(), OffsetDateTime.now().plusDays(1), 2L, new TestEventSource(randomUUID()),
                "Applied2"))
        );

        TestState state = history.getAt(OffsetDateTime.now());

        Assertions.assertEquals(state.getTestValue(), "Applied");
        Assertions.assertEquals(state.getVersion(), 0);
    }


    @Test
    public void eventsToVersion() {
        UUID id = randomUUID();
        TestHistory history = new TestHistory(id, new TestState(id, 0L, ""), Arrays.asList(
            new TestEvent(randomUUID(), OffsetDateTime.now().minusDays(1), 1L, new TestEventSource(randomUUID()),
                "Applied"),
            new TestEvent(randomUUID(), OffsetDateTime.now().plusDays(1), 2L, new TestEventSource(randomUUID()),
                "Applied2"))
        );

        TestState state = history.getVersion(0L);
        Assertions.assertEquals(state.getTestValue(), "");
        Assertions.assertEquals(state.getVersion(), 0);

        state = history.getVersion(1L);
        Assertions.assertEquals(state.getTestValue(), "Applied");
        Assertions.assertEquals(state.getVersion(), 0);

        state = history.getVersion(2L);
        Assertions.assertEquals(state.getTestValue(), "Applied2");
        Assertions.assertEquals(state.getVersion(), 0);

        state = history.getVersion(200L);
        Assertions.assertEquals(state.getTestValue(), "Applied2");
        Assertions.assertEquals(state.getVersion(), 0);
    }


    @Test
    public void newEventApplied() {
        UUID id = randomUUID();
        TestHistory history = new TestHistory(id, new TestState(id, 0L, ""), Arrays.asList(
            new TestEvent(randomUUID(), OffsetDateTime.now().minusDays(2), 1L, new TestEventSource(randomUUID()),
                "Applied"),
            new TestEvent(randomUUID(), OffsetDateTime.now().minusDays(1), 2L, new TestEventSource(randomUUID()),
                "Applied2"))
        );

        history.applyNewEvent();

        List<TestEvent> newEvents = history.getNewEvents();

        TestState state = history.getState();
        Assertions.assertTrue(history::success);
        Assertions.assertEquals(state.getTestValue(), "newValue");
        Assertions.assertEquals(state.getVersion(), 0);

        Assertions.assertEquals(newEvents.size(), 1);
        Assertions.assertEquals(newEvents.get(0).getTestValue(), "newValue");
        Assertions.assertEquals(newEvents.get(0).getVersion(), 3L);
    }


    @Test
    public void newEventsApplied() {
        UUID id = randomUUID();
        TestHistory history = new TestHistory(id, new TestState(id, 0L, ""), Arrays.asList(
            new TestEvent(randomUUID(), OffsetDateTime.now().minusDays(2), 1L, new TestEventSource(randomUUID()),
                "Applied"),
            new TestEvent(randomUUID(), OffsetDateTime.now().minusDays(1), 2L, new TestEventSource(randomUUID()),
                "Applied2"))
        );

        history.applyNewEvents();

        List<TestEvent> newEvents = history.getNewEvents();

        TestState state = history.getState();
        Assertions.assertTrue(history::success);
        Assertions.assertEquals(state.getTestValue(), "newValue2");
        Assertions.assertEquals(state.getVersion(), 0);

        Assertions.assertEquals(newEvents.size(), 2);
        Assertions.assertTrue(newEvents.stream()
            .anyMatch(event -> event.getTestValue().equals("newValue") && event.getVersion().equals(3L)));
        Assertions.assertTrue(newEvents.stream()
            .anyMatch(event -> event.getTestValue().equals("newValue2") && event.getVersion().equals(4L)));
    }


    @Test
    public void newBadEventThrowsExceptionThrowsException() {
        UUID id = randomUUID();
        TestHistory history = new TestHistory(id, new TestState(id, 0L, ""), Arrays.asList(
            new TestEvent(randomUUID(), OffsetDateTime.now().minusDays(2), 1L, new TestEventSource(randomUUID()),
                "Applied"),
            new TestEvent(randomUUID(), OffsetDateTime.now().minusDays(1), 2L, new TestEventSource(randomUUID()),
                "Applied2"))
        );

        Assertions.assertThrows(IllegalStateException.class, history::throwsException);
    }


    @Test
    public void newBadEventDontThrowsExceptionThrowsException() {
        UUID id = randomUUID();
        TestHistory history = new TestHistory(id, new TestState(id, 0L, ""), Arrays.asList(
            new TestEvent(randomUUID(), OffsetDateTime.now().minusDays(2), 1L, new TestEventSource(randomUUID()),
                "Applied"),
            new TestEvent(randomUUID(), OffsetDateTime.now().minusDays(1), 2L, new TestEventSource(randomUUID()),
                "Applied2"))
        );

        Assertions.assertDoesNotThrow(history::dontThrowsException);
        Assertions.assertFalse(history::success);
        Assertions.assertEquals(String.join("", history.getErrors()), "TEST");

        Assertions.assertThrows(IllegalStateException.class, history::getNewEvents);
    }


    @Test
    public void stateAfterLastEvent() {
        UUID id = randomUUID();

        Assertions.assertThrows(IllegalStateException.class,
            () -> new TestHistory(id, new TestState(id, 3L, ""), Arrays.asList(
                new TestEvent(randomUUID(), OffsetDateTime.now().minusDays(2), 1L, new TestEventSource(randomUUID()),
                    "Applied"),
                new TestEvent(randomUUID(), OffsetDateTime.now().minusDays(1), 2L, new TestEventSource(randomUUID()),
                    "Applied2"))
            ));
    }


    @Test
    public void oldEventsNotApplied() {
        UUID id = randomUUID();
        TestHistory history = new TestHistory(id, new TestState(id, 2L, ""), Arrays.asList(
            new TestBadEvent(randomUUID(), OffsetDateTime.now().minusDays(2), 1L, new TestEventSource(randomUUID()),
                "Applied"),
            new TestBadEvent(randomUUID(), OffsetDateTime.now().minusDays(1), 2L, new TestEventSource(randomUUID()),
                "Applied2"))
        );

        history.applyNewEvent();

        List<TestEvent> newEvents = history.getNewEvents();

        TestState state = history.getState();
        Assertions.assertTrue(history::success);
        Assertions.assertEquals(state.getTestValue(), "newValue");
        Assertions.assertEquals(state.getVersion(), 2);

        Assertions.assertEquals(newEvents.size(), 1);
        Assertions.assertEquals(newEvents.get(0).getTestValue(), "newValue");
        Assertions.assertEquals(newEvents.get(0).getVersion(), 3L);
    }

}