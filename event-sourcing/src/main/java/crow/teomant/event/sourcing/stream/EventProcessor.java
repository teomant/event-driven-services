package crow.teomant.event.sourcing.stream;

import crow.teomant.event.sourcing.event.source.BaseSourceEvent;
import crow.teomant.event.sourcing.event.source.BaseSourceSkipEvent;
import crow.teomant.event.sourcing.source.EventSource;
import crow.teomant.event.sourcing.state.State;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

    private final List<BSE> newEvents;
    private final List<ApplicationResult> results;

    protected EventProcessor(List<BSE> events, ApplicationPolicy applicationPolicy,
                             S state, List<BSE> newEvents, List<ApplicationResult> results) {
        this.comparator = getComparator();
        this.events = events;
        this.applicationPolicy = applicationPolicy;
        this.state = state;
        this.newEvents = newEvents;
        this.results = results;
    }

    protected Comparator<BSE> getComparator() {
        return Comparator.comparing(BSE::getDiscriminant);
    }

    public void processEvents(List<? extends BSE> addEvents) {
        if (applicationPolicy == ApplicationPolicy.RETHROW) {
            rethrow(addEvents);
        } else {
            notApply(addEvents);
        }
    }

    public void addNewEvents(List<? extends BSE> addEvents) {
        addEvents.forEach(this::addNewEvent);
    }

    private void addNewEvent(BSE event) {
        long version = events.stream()
            .mapToLong(BSE::getVersion)
            .max()
            .orElse(state.getInitialVersion()) + newEvents.size() + 1;
        event.setVersion(version);
        newEvents.add(event);
    }

    private void notApply(List<? extends BSE> addEvents) {
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

    private Stream<BSE> prepareEvents(List<? extends BSE> addEvents) {
        List<BSE> prepared = Stream.of(events, newEvents, addEvents)
            .flatMap(Collection::stream)
            .sorted(comparator.thenComparing(BSE::getCreatedAt))
            .filter(event -> Optional.ofNullable(event.getVersion())
                .map(version -> version > state.getInitialVersion())
                .orElse(true)).collect(Collectors.toList());

        List<BaseSourceSkipEvent> skipEvents = prepared.stream()
            .filter(bse -> bse instanceof BaseSourceSkipEvent)
            .map(x -> (BaseSourceSkipEvent) x)
            .collect(Collectors.toList());

        skipEvents.forEach(skipEvent -> {
            if (prepared.stream().noneMatch(event -> event.getId().equals(skipEvent.getSkipId()))) {
                throw new IllegalStateException();
            }
        });

        return prepared.stream()
            .filter(event -> skipEvents.stream().noneMatch(skip -> skip.getSkipId().equals(event.getId())));
    }

    private void rethrow(List<? extends BSE> addEvents) {
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
