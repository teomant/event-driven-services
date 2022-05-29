package crow.teomant.client.domain.history;

import com.fasterxml.jackson.databind.ObjectMapper;
import crow.teomant.client.domain.events.source.BaseClientSourceEvent;
import crow.teomant.common.EventSource;
import crow.teomant.event.sourcing.stream.ApplicationPolicy;
import crow.teomant.event.sourcing.stream.ApplicationResult;
import crow.teomant.event.sourcing.stream.EventProcessor;
import crow.teomant.event.sourcing.stream.EventStream;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.SneakyThrows;

public class ClientEventStream extends EventStream<OffsetDateTime, ClientState, EventSource, BaseClientSourceEvent> {

    private final ObjectMapper objectMapper;

    protected ClientEventStream(ClientState state,
                                List<BaseClientSourceEvent> events, ObjectMapper objectMapper) {
        super(state, events);
        this.objectMapper = objectMapper;
    }

    @Override
    @SneakyThrows
    protected ClientState getStateClone(ClientState clientState) {
        return objectMapper.readValue(objectMapper.writeValueAsString(clientState), ClientState.class);
    }

    @Override
    protected EventProcessor<OffsetDateTime, ClientState, EventSource, BaseClientSourceEvent> getEventProcessor(
        List<BaseClientSourceEvent> events, ApplicationPolicy applicationPolicy, ClientState clientState,
        List<BaseClientSourceEvent> newEvents, List<ApplicationResult> results) {
        return new ClientEventProcessor(events, applicationPolicy, clientState, newEvents, results);
    }

}
