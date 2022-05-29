package crow.teomant.event.sourcing.history;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.OffsetDateTime;
import java.util.ArrayList;
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
        Assertions.assertEquals(state.getInitialVersion(), 0);
    }


    @Test
    public void eventApplied() {
        UUID id = randomUUID();
        TestHistory history = new TestHistory(id, new TestState(id, 0L, ""), Collections.singletonList(
            new TestEvent(randomUUID(), OffsetDateTime.now(), 1L, new TestEventSource(randomUUID()), "Applied")));

        TestState state = history.calculateLastState();

        Assertions.assertEquals(state.getTestValue(), "Applied");
        Assertions.assertEquals(state.getInitialVersion(), 0);
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
        Assertions.assertEquals(state.getInitialVersion(), 0);
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
        Assertions.assertEquals(state.getInitialVersion(), 0);
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
        Assertions.assertEquals(state.getInitialVersion(), 0);

        state = history.getVersion(1L);
        Assertions.assertEquals(state.getTestValue(), "Applied");
        Assertions.assertEquals(state.getInitialVersion(), 0);

        state = history.getVersion(2L);
        Assertions.assertEquals(state.getTestValue(), "Applied2");
        Assertions.assertEquals(state.getInitialVersion(), 0);

        state = history.getVersion(200L);
        Assertions.assertEquals(state.getTestValue(), "Applied2");
        Assertions.assertEquals(state.getInitialVersion(), 0);
    }


    @Test
    public void newEventApplied() {
        UUID id = randomUUID();
        TestHistory history = new TestHistory(id, new TestState(id, 0L, ""), new ArrayList<>(Arrays.asList(
            new TestEvent(randomUUID(), OffsetDateTime.now().minusDays(2), 1L, new TestEventSource(randomUUID()),
                "Applied"),
            new TestEvent(randomUUID(), OffsetDateTime.now().minusDays(1), 2L, new TestEventSource(randomUUID()),
                "Applied2"))
        ));

        history.applyNewEvent();

        List<TestEvent> newEvents = history.getNewEvents();

        TestState state = history.getCurrentState();
        Assertions.assertTrue(history::success);
        Assertions.assertEquals(state.getTestValue(), "newValue");
        Assertions.assertEquals(state.getInitialVersion(), 0);

        Assertions.assertEquals(newEvents.size(), 1);
        Assertions.assertEquals(newEvents.get(0).getTestValue(), "newValue");
        Assertions.assertEquals(newEvents.get(0).getVersion(), 3L);
    }

    @Test
    public void newEventAppliedAndSkippedTest() {
        UUID id = randomUUID();
        TestHistory history = new TestHistory(id, new TestState(id, 0L, ""), new ArrayList<>(Arrays.asList(
            new TestEvent(randomUUID(), OffsetDateTime.now().minusDays(2), 1L, new TestEventSource(randomUUID()),
                "Applied"),
            new TestEvent(randomUUID(), OffsetDateTime.now().minusDays(1), 2L, new TestEventSource(randomUUID()),
                "Applied2"))
        ));

        history.applyAndSkipEvent();

        List<TestEvent> newEvents = history.getNewEvents();

        TestState state = history.getCurrentState();
        Assertions.assertTrue(history::success);
        Assertions.assertEquals(state.getTestValue(), "Applied2");
        Assertions.assertEquals(state.getInitialVersion(), 0);

        Assertions.assertEquals(newEvents.size(), 2);
        Assertions.assertEquals(newEvents.get(0).getTestValue(), "newValue");
        Assertions.assertEquals(newEvents.get(0).getVersion(), 3L);

        Assertions.assertEquals(newEvents.get(1).getTestValue(), "");
        Assertions.assertEquals(newEvents.get(1).getVersion(), 4L);
        Assertions.assertTrue(newEvents.get(1) instanceof TestSkipEvent);
    }

    @Test
    public void oldEventSkippedTest() {
        UUID id = randomUUID();
        UUID toSkip = randomUUID();
        TestHistory history = new TestHistory(id, new TestState(id, 0L, ""), new ArrayList<>(Arrays.asList(
            new TestEvent(randomUUID(), OffsetDateTime.now().minusDays(2), 1L, new TestEventSource(randomUUID()),
                "Applied"),
            new TestEvent(toSkip, OffsetDateTime.now().minusDays(1), 2L, new TestEventSource(randomUUID()),
                "Applied2"))
        ));

        history.skip(toSkip);

        List<TestEvent> newEvents = history.getNewEvents();

        TestState state = history.getCurrentState();
        Assertions.assertTrue(history::success);
        Assertions.assertEquals(state.getTestValue(), "Applied");
        Assertions.assertEquals(state.getInitialVersion(), 0);

        Assertions.assertEquals(newEvents.size(), 1);
        Assertions.assertEquals(newEvents.get(0).getTestValue(), "");
        Assertions.assertEquals(newEvents.get(0).getVersion(), 3L);
    }

    @Test
    public void exceptionIfCantSkipTest() {
        UUID id = randomUUID();
        UUID toSkip = randomUUID();
        TestHistory history = new TestHistory(id, new TestState(id, 2L, ""), new ArrayList<>(Arrays.asList(
            new TestEvent(randomUUID(), OffsetDateTime.now().minusDays(2), 1L, new TestEventSource(randomUUID()),
                "Applied"),
            new TestEvent(toSkip, OffsetDateTime.now().minusDays(1), 2L, new TestEventSource(randomUUID()),
                "Applied2"))
        ));

        assertThrows(IllegalStateException.class, () -> history.skip(toSkip));
    }

    @Test
    public void newEventsApplied() {
        UUID id = randomUUID();
        TestHistory history = new TestHistory(id, new TestState(id, 0L, ""), new ArrayList<>(Arrays.asList(
            new TestEvent(randomUUID(), OffsetDateTime.now().minusDays(2), 1L, new TestEventSource(randomUUID()),
                "Applied"),
            new TestEvent(randomUUID(), OffsetDateTime.now().minusDays(1), 2L, new TestEventSource(randomUUID()),
                "Applied2"))
        ));

        history.applyNewEvents();

        List<TestEvent> newEvents = history.getNewEvents();

        TestState state = history.getCurrentState();
        Assertions.assertTrue(history::success);
        Assertions.assertEquals(state.getTestValue(), "newValue2");
        Assertions.assertEquals(state.getInitialVersion(), 0);

        Assertions.assertEquals(newEvents.size(), 2);
        Assertions.assertTrue(newEvents.stream()
            .anyMatch(event -> event.getTestValue().equals("newValue") && event.getVersion().equals(3L)));
        Assertions.assertTrue(newEvents.stream()
            .anyMatch(event -> event.getTestValue().equals("newValue2") && event.getVersion().equals(4L)));
    }


    @Test
    public void multipleEventApplied() {
        UUID id = randomUUID();
        TestHistory history = new TestHistory(id, new TestState(id, 0L, ""), new ArrayList<>(Arrays.asList(
            new TestEvent(randomUUID(), OffsetDateTime.now().minusDays(2), 1L, new TestEventSource(randomUUID()),
                "Applied"),
            new TestEvent(randomUUID(), OffsetDateTime.now().minusDays(1), 2L, new TestEventSource(randomUUID()),
                "Applied2"))
        ));

        history.applyNewEvent();

        List<TestEvent> newEvents = history.getNewEvents();

        TestState state = history.getCurrentState();
        Assertions.assertTrue(history::success);
        Assertions.assertEquals(state.getTestValue(), "newValue");
        Assertions.assertEquals(state.getInitialVersion(), 0);

        Assertions.assertEquals(newEvents.size(), 1);
        Assertions.assertEquals(newEvents.get(0).getTestValue(), "newValue");
        Assertions.assertEquals(newEvents.get(0).getVersion(), 3L);

        history.anotherNewEvent();

        newEvents = history.getNewEvents();

        state = history.getCurrentState();
        Assertions.assertTrue(history::success);
        Assertions.assertEquals(state.getTestValue(), "anotherNewValue");
        Assertions.assertEquals(state.getInitialVersion(), 0);

        Assertions.assertEquals(newEvents.size(), 2);
        Assertions.assertEquals(newEvents.get(0).getTestValue(), "newValue");
        Assertions.assertEquals(newEvents.get(0).getVersion(), 3L);
        Assertions.assertEquals(newEvents.get(1).getTestValue(), "anotherNewValue");
        Assertions.assertEquals(newEvents.get(1).getVersion(), 4L);

        history.applyNewEvents();

        newEvents = history.getNewEvents();

        state = history.getCurrentState();
        Assertions.assertTrue(history::success);
        Assertions.assertEquals(state.getTestValue(), "newValue2");
        Assertions.assertEquals(state.getInitialVersion(), 0);

        Assertions.assertEquals(newEvents.size(), 4);
        Assertions.assertEquals(newEvents.get(0).getTestValue(), "newValue");
        Assertions.assertEquals(newEvents.get(0).getVersion(), 3L);
        Assertions.assertEquals(newEvents.get(1).getTestValue(), "anotherNewValue");
        Assertions.assertEquals(newEvents.get(1).getVersion(), 4L);
        Assertions.assertEquals(newEvents.get(2).getTestValue(), "newValue");
        Assertions.assertEquals(newEvents.get(2).getVersion(), 5L);
        Assertions.assertEquals(newEvents.get(3).getTestValue(), "newValue2");
        Assertions.assertEquals(newEvents.get(3).getVersion(), 6L);
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

        assertThrows(IllegalStateException.class, history::throwsException);
    }


    @Test
    public void newBadEventDontThrowsExceptionThrowsException() {
        UUID id = randomUUID();
        TestHistory history = new TestHistory(id, new TestState(id, 0L, ""), new ArrayList<>(Arrays.asList(
            new TestEvent(randomUUID(), OffsetDateTime.now().minusDays(2), 1L, new TestEventSource(randomUUID()),
                "Applied"),
            new TestEvent(randomUUID(), OffsetDateTime.now().minusDays(1), 2L, new TestEventSource(randomUUID()),
                "Applied2"))
        ));

        Assertions.assertDoesNotThrow(history::dontThrowsException);
        Assertions.assertFalse(history::success);
        Assertions.assertEquals(String.join(",", history.getErrors()), "TEST");

        assertThrows(IllegalStateException.class, history::getNewEvents);

        Assertions.assertDoesNotThrow(history::dontThrowsException);
        Assertions.assertFalse(history::success);
        Assertions.assertEquals(String.join(",", history.getErrors()), "TEST,TEST");

        assertThrows(IllegalStateException.class, history::getNewEvents);
    }


    @Test
    public void stateAfterLastEvent() {
        UUID id = randomUUID();

        assertThrows(IllegalStateException.class,
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
        TestHistory history = new TestHistory(id, new TestState(id, 2L, ""), new ArrayList<>(Arrays.asList(
            new TestBadEvent(randomUUID(), OffsetDateTime.now().minusDays(2), 1L, new TestEventSource(randomUUID()),
                "Applied"),
            new TestBadEvent(randomUUID(), OffsetDateTime.now().minusDays(1), 2L, new TestEventSource(randomUUID()),
                "Applied2"))
        ));

        history.applyNewEvent();

        List<TestEvent> newEvents = history.getNewEvents();

        TestState state = history.getCurrentState();
        Assertions.assertTrue(history::success);
        Assertions.assertEquals(state.getTestValue(), "newValue");
        Assertions.assertEquals(state.getInitialVersion(), 2);

        Assertions.assertEquals(newEvents.size(), 1);
        Assertions.assertEquals(newEvents.get(0).getTestValue(), "newValue");
        Assertions.assertEquals(newEvents.get(0).getVersion(), 3L);
    }

}