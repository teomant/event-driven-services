package crow.teomant.event.sourcing.stream;

import crow.teomant.event.sourcing.event.source.BaseSourceEvent;
import crow.teomant.event.sourcing.source.EventSource;
import crow.teomant.event.sourcing.state.State;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class EventProcessor<
    D extends Comparable<D>,
    S extends State,
    ES extends EventSource,
    BSE extends BaseSourceEvent<D, S, ES>
    > {

    private final Comparator<BSE> comparator;
    private final List<BSE> events;
    private final ApplicationPolicy applicationPolicy;
    private final S state;

    private final List<BSE> newEvents = new ArrayList<>();
    private final List<ApplicationResult> results = new ArrayList<>();

    protected EventProcessor(Comparator<BSE> comparator, List<BSE> events, ApplicationPolicy applicationPolicy,
                             S state) {
        this.comparator = comparator;
        this.events = events;
        this.applicationPolicy = applicationPolicy;
        this.state = state;
    }

    public void addEvents(List<BSE> addEvents) {
        if (applicationPolicy == ApplicationPolicy.RETHROW) {
            rethrow(addEvents);
        } else {
            notApply(addEvents);
        }
        addEvents.forEach(this::addNewEvent);
    }

    private void addNewEvent(BSE event) {
        Long lastExistingVersion = events.stream().max(comparator).map(BSE::getVersion).orElse(state.getVersion());
        event.setVersion(lastExistingVersion + newEvents.size() + 1);
        newEvents.add(event);
    }

    private void notApply(List<BSE> addEvents) {
        prepareEvents(addEvents)
            .forEach(event -> {
                try {
                    event.apply(state);
                } catch (Exception e) {
                    results.add(new ApplicationResult(event.getId(), e.getMessage(), false));
                }
                results.add(new ApplicationResult(event.getId(), "", true));
            });
    }

    private Stream<BSE> prepareEvents(List<BSE> addEvents) {
        return Stream.of(events, addEvents)
            .flatMap(Collection::stream)
            .sorted(comparator)
            .filter(event -> Optional.ofNullable(event.getVersion())
                .map(version -> version > state.getVersion())
                .orElse(true));
    }

    private void rethrow(List<BSE> addEvents) {
        prepareEvents(addEvents)
            .forEach(event -> {
                event.apply(state);
                results.add(new ApplicationResult(event.getId(), "", true));
            });
    }

    public List<BSE> getNewEvents() {
        return newEvents;
    }

    public List<ApplicationResult> getResults() {
        return results;
    }

    public S calculateLastState() {
        rethrow(Collections.emptyList());
        return state;
    }

    public S getVersion(Long version) {
        events.stream()
            .sorted(comparator)
            .filter(event -> event.getVersion() <= version)
            .forEach(event -> event.apply(state));

        return state;
    }

    public S getAt(D discr) {
        prepareEvents(Collections.emptyList())
            .filter(event -> event.getDiscriminant().compareTo(discr) < 0)
            .forEach(event -> event.apply(state));

        return state;
    }

    public S getState() {
        return state;
    }
}
