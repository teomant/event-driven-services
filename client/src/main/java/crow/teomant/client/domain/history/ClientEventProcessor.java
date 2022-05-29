package crow.teomant.client.domain.history;

import crow.teomant.client.domain.events.source.BaseClientSourceEvent;
import crow.teomant.common.EventSource;
import crow.teomant.event.sourcing.stream.ApplicationPolicy;
import crow.teomant.event.sourcing.stream.ApplicationResult;
import crow.teomant.event.sourcing.stream.EventProcessor;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;

public class ClientEventProcessor
    extends EventProcessor<OffsetDateTime, ClientState, EventSource, BaseClientSourceEvent> {

    public ClientEventProcessor(List<BaseClientSourceEvent> events,
                                ApplicationPolicy applicationPolicy,
                                ClientState state,
                                List<BaseClientSourceEvent> newEvents,
                                List<ApplicationResult> results) {
        super(events, applicationPolicy, state, newEvents, results);
    }
}
