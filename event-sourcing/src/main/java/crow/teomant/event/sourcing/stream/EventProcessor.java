package crow.teomant.event.sourcing.stream;

import crow.teomant.event.sourcing.event.source.BaseSourceEvent;
import crow.teomant.event.sourcing.source.EventSource;
import crow.teomant.event.sourcing.state.State;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public abstract class EventProcessor<
    D extends Comparable<?>,
    S extends State,
    ES extends EventSource,
    BSE extends BaseSourceEvent<D, S, ES>
    > {

    private final Comparator<BSE> comparator;
    private final List<BSE> events;
    private final ApplicationPolicy applicationPolicy;
    private final S state;
    private final List<BSE> addEvents;

    private final List<BSE> newEvents = new ArrayList<>();
    private final List<ApplicationResult> results = new ArrayList<>();

    protected EventProcessor(Comparator<BSE> comparator, List<BSE> events, ApplicationPolicy applicationPolicy,
                             S state, List<BSE> addEvents) {
        this.comparator = comparator;
        this.events = events;
        this.applicationPolicy = applicationPolicy;
        this.state = state;
        this.addEvents = addEvents;
    }

    public void addEvents() {
        if (applicationPolicy == ApplicationPolicy.RETHROW) {
            rethrow();
        } else {
            notApply();
        }
    }

    private void notApply() {
        Stream.of(events, addEvents)
            .flatMap(Collection::stream)
            .sorted(comparator)
            .forEach(event -> {
                try {
                    event.apply(state);
                } catch (Exception e) {
                    results.add(new ApplicationResult(event.getId(), e.getMessage(), false));
                }
                results.add(new ApplicationResult(event.getId(), "", true));
                newEvents.add(event);
            });
    }

    private void rethrow() {
        Stream.of(events, addEvents)
            .flatMap(Collection::stream)
            .sorted(comparator)
            .forEach(event -> {
                event.apply(state);
                results.add(new ApplicationResult(event.getId(), "", true));
                newEvents.add(event);
            });
    }

    public List<BSE> getNewEvents() {
        return newEvents;
    }

    public List<ApplicationResult> getResults() {
        return results;
    }
}
