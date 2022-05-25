package crow.teomant.event.sourcing.stream;

import crow.teomant.event.sourcing.event.source.BaseSourceEvent;
import crow.teomant.event.sourcing.source.EventSource;
import crow.teomant.event.sourcing.state.State;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class EventStream<
    D extends Comparable<D>,
    S extends State,
    ES extends EventSource,
    BSE extends BaseSourceEvent<D, S, ES>
    > {

    private final Comparator<D> comparator;
    private final S state;
    private final List<BSE> events;
    private EventProcessor<D, S, ES, BSE> eventProcessor;

    protected EventStream(Comparator<D> comparator, S state, List<BSE> events) {

        if (events.stream()
            .max(Comparator.comparingLong(BSE::getVersion))
            .map(BSE::getVersion).orElse(0L) < state.getVersion()) {
            throw new IllegalStateException();
        }
        this.comparator = comparator;
        this.state = state;
        this.events = events;
    }

    public void addEvents(List<BSE> addEvents, ApplicationPolicy policy) {
        this.eventProcessor = getEventProcessor(comparator, events, policy, getStateClone(state));
        eventProcessor.addEvents(addEvents);
    }

    public S getCurrentState() {
        this.eventProcessor = getEventProcessor(comparator, events, ApplicationPolicy.RETHROW, getStateClone(state));
        return eventProcessor.getCurrentState();
    }

    public S getVersion(Long version) {
        this.eventProcessor = getEventProcessor(comparator, events, ApplicationPolicy.RETHROW, getStateClone(state));
        return eventProcessor.getVersion(version);
    }

    public S getAt(D discr) {
        this.eventProcessor = getEventProcessor(comparator, events, ApplicationPolicy.RETHROW, getStateClone(state));
        return eventProcessor.getAt(discr);
    }

    protected abstract State getStateClone(S state);

    protected abstract EventProcessor<D, S, ES, BSE> getEventProcessor(Comparator<D> comparator,
                                                                       List<BSE> events,
                                                                       ApplicationPolicy policy,
                                                                       State state);

    public List<String> getErrors() {
        return getCurrentProcessor().getResults().stream()
            .filter(result -> !result.getSuccess())
            .map(ApplicationResult::getErrorMessage)
            .collect(Collectors.toList());
    }

    public Boolean success() {
        return getCurrentProcessor().getResults().stream()
            .allMatch(ApplicationResult::getSuccess);
    }

    public List<BSE> getNewEvents() {
        if (!success()) {
            throw new IllegalStateException();
        }
        return getCurrentProcessor().getNewEvents();
    }

    private EventProcessor<D, S, ES, BSE> getCurrentProcessor() {
        return Optional.ofNullable(eventProcessor).orElseThrow(IllegalStateException::new);
    }

}
