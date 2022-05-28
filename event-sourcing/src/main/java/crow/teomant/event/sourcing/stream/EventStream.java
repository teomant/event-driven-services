package crow.teomant.event.sourcing.stream;

import crow.teomant.event.sourcing.event.source.BaseSourceEvent;
import crow.teomant.event.sourcing.source.EventSource;
import crow.teomant.event.sourcing.state.State;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class EventStream<
    D extends Comparable<D>,
    S extends State,
    ES extends EventSource,
    BSE extends BaseSourceEvent<D, S, ES>
    > {

    private final Comparator<BSE> comparator;
    private final S state;

    private final List<BSE> events;
    private final List<BSE> newEvents = new ArrayList<>();
    private final List<ApplicationResult> results = new ArrayList<>();
    private EventProcessor<D, S, ES, BSE> eventProcessor;

    protected EventStream(Comparator<BSE> comparator, S state, List<BSE> events) {

        if (events.stream()
            .max(Comparator.comparingLong(BSE::getVersion))
            .map(BSE::getVersion).orElse(0L) < state.getInitialVersion()) {
            throw new IllegalStateException();
        }
        this.comparator = comparator;
        this.state = state;
        this.events = events;
    }

    public S addEvents(List<BSE> addEvents, ApplicationPolicy policy) {
        this.eventProcessor = getEventProcessor(policy);
        eventProcessor.processEvents(addEvents);
        if (success()) {
            eventProcessor.addNewEvents(addEvents);
        }
        return eventProcessor.getState();
    }

    public S calculateLastState() {
        this.eventProcessor = getEventProcessor(ApplicationPolicy.RETHROW);
        return eventProcessor.calculateLastState();
    }

    public S getVersion(Long version) {
        this.eventProcessor = getEventProcessor(ApplicationPolicy.RETHROW);
        return eventProcessor.getVersion(version);
    }

    public S getAt(D discr) {
        this.eventProcessor = getEventProcessor(ApplicationPolicy.RETHROW);
        return eventProcessor.getAt(discr);
    }

    private EventProcessor<D, S, ES, BSE> getEventProcessor(ApplicationPolicy policy) {
        return getEventProcessor(comparator, events, policy, getStateClone(state), newEvents, results);
    }

    protected abstract S getStateClone(S state);

    protected abstract EventProcessor<D, S, ES, BSE> getEventProcessor(Comparator<BSE> comparator,
                                                                       List<BSE> events,
                                                                       ApplicationPolicy policy,
                                                                       S state,
                                                                       List<BSE> newEvents,
                                                                       List<ApplicationResult> results);

    public List<String> getErrors() {
        return results.stream()
            .filter(result -> !result.getSuccess())
            .map(ApplicationResult::getErrorMessage)
            .collect(Collectors.toList());
    }

    public Boolean success() {
        return results.stream()
            .allMatch(ApplicationResult::getSuccess);
    }

    public List<BSE> getNewEvents() {
        if (!success()) {
            throw new IllegalStateException();
        }
        return newEvents;
    }

    public S getState() {
        return eventProcessor.getState();
    }
}
