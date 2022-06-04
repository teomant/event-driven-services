package crow.teomant.client.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import crow.teomant.client.domain.events.source.BaseClientSourceEvent;
import crow.teomant.client.domain.history.ClientHistory;
import crow.teomant.client.domain.history.ClientState;
import crow.teomant.common.EventSource;
import crow.teomant.domain.events.DomainEvents;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final Map<UUID, ClientState> stateMap = new ConcurrentHashMap<>();
    private final Map<UUID, List<BaseClientSourceEvent>> eventsListsMap = new ConcurrentHashMap<>();
    private final DomainEvents domainEvents;

    @Override
    public ClientState register(String login, UUID preferedWarehouse, String name) {

        checkLogin(login);

        UUID id = UUID.randomUUID();
        ClientHistory history = new ClientHistory(id, new ClientState(id, 0L), new ArrayList<>(), new ObjectMapper());

        history.register(login, preferedWarehouse, name, new EventSource(UUID.randomUUID()));

        stateMap.put(id, history.getCurrentState());

        List<BaseClientSourceEvent> events = eventsListsMap.computeIfAbsent(id, (x) -> new ArrayList<>());
        events.addAll(history.getNewEvents());
        history.getDomainEvents().forEach(domainEvents::raise);

        return history.getCurrentState();
    }

    private void checkLogin(String login) {
        if (stateMap.values().stream().anyMatch(state -> state.getLogin().equals(login))) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public ClientState update(UUID id, UUID preferedWarehouse, String name) {
        ClientState clientState = stateMap.get(id);
        if (Objects.isNull(clientState)) {
            throw new IllegalArgumentException();
        }

        ClientHistory history = new ClientHistory(id, clientState, eventsListsMap.get(id), new ObjectMapper());
        history.update(preferedWarehouse, name, new EventSource(UUID.randomUUID()));

        ClientState currentState = history.getCurrentState();

        stateMap.put(id, new ClientState(currentState, history.getCurrentVersion()));

        List<BaseClientSourceEvent> events = eventsListsMap.computeIfAbsent(id, (x) -> new ArrayList<>());
        events.addAll(history.getNewEvents());
        history.getDomainEvents().forEach(domainEvents::raise);

        return currentState;
    }

    @Override
    public ClientState get(UUID id) {
        ClientState clientState = stateMap.get(id);
        if (Objects.isNull(clientState)) {
            throw new IllegalArgumentException();
        }

        ClientHistory history = new ClientHistory(id, clientState, eventsListsMap.get(id), new ObjectMapper());

        return history.calculateLastState();
    }
}
