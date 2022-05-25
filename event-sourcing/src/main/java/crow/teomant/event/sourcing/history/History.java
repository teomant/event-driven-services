package crow.teomant.event.sourcing.history;

import crow.teomant.event.sourcing.event.domain.BaseDomainEvent;
import crow.teomant.event.sourcing.event.source.BaseSourceEvent;
import crow.teomant.event.sourcing.source.EventSource;
import crow.teomant.event.sourcing.state.State;
import crow.teomant.event.sourcing.stream.EventStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public abstract class History<
    D extends Comparable<D>,
    S extends State,
    ES extends EventSource,
    BSE extends BaseSourceEvent<D, S, ES>
    > {

    private final UUID id;

    private final List<BaseDomainEvent> domainEvents = new ArrayList<>();
    private final EventStream<D, S, ES, BSE> eventStream;


    protected History(UUID id, S state, List<BSE> events) {
        this.id = id;
        eventStream = getEventStream(state, events);
    }

    protected abstract EventStream<D, S, ES, BSE> getEventStream(S state, List<BSE> events);

    protected abstract Comparator<D> getComparator();

    public UUID getId() {
        return id;
    }

    public List<BaseDomainEvent> getDomainEvents() {
        return domainEvents;
    }

    public S getCurrentState() {
        return eventStream.getCurrentState();
    }

    public S getVersion(Long version) {
        return eventStream.getVersion(version);
    }

    public S getAt(D discr) {
        return eventStream.getAt(discr);
    }
}