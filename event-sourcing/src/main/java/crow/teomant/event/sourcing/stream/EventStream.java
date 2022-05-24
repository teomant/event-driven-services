package crow.teomant.event.sourcing.stream;

import crow.teomant.event.sourcing.event.source.BaseSourceEvent;
import crow.teomant.event.sourcing.source.EventSource;
import crow.teomant.event.sourcing.state.State;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class EventStream<
    D extends Comparable<?>,
    S extends State,
    ES extends EventSource,
    BSE extends BaseSourceEvent<D, S, ES>
    > {

    private final Comparator<D> comparator;
    private final S state;
    private final List<BSE> events;
    private EventProcessor<D, S, ES, BSE> eventProcessor;

    protected EventStream(Comparator<D> comparator, S state, List<BSE> events) {
        this.comparator = comparator;
        this.state = state;
        this.events = events;
    }

    public void addEvents(List<BSE> addEvents, ApplicationPolicy policy) {
        this.eventProcessor = getEventProcessor(comparator, events, policy, state, addEvents);
        eventProcessor.addEvents();
    }

    protected abstract EventProcessor<D, S, ES, BSE> getEventProcessor(Comparator<D> comparator,
                                                                       List<BSE> events,
                                                                       ApplicationPolicy policy,
                                                                       State readValue,
                                                                       List<BSE> addEvents);

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
