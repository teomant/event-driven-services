package crow.teomant.client.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import crow.teomant.client.domain.events.source.BaseClientSourceEvent;
import crow.teomant.client.domain.history.ClientHistory;
import crow.teomant.client.domain.history.ClientState;
import crow.teomant.common.EventSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

    private final Map<UUID, ClientState> stateMap = new ConcurrentHashMap<>();
    private final Map<UUID, List<BaseClientSourceEvent>> eventsListsMap = new ConcurrentHashMap<>();

    @Override
    public ClientState register(String login, UUID preferedWarehouse) {

        if (stateMap.values().stream().anyMatch(state -> state.getLogin().equals(login))) {
            throw new IllegalArgumentException();
        }

        UUID id = UUID.randomUUID();
        ClientHistory history = new ClientHistory(id, new ClientState(id, 0L), new ArrayList<>(), new ObjectMapper());

        history.register(login, preferedWarehouse, new EventSource(UUID.randomUUID()));

        stateMap.put(id, history.getCurrentState());

        List<BaseClientSourceEvent> events = eventsListsMap.computeIfAbsent(id, (x) -> new ArrayList<>());
        events.addAll(history.getNewEvents());

        return history.getCurrentState();
    }

    @Override
    public ClientState get(UUID id) {
        return new ClientHistory(id, stateMap.get(id), eventsListsMap.get(id), new ObjectMapper()).calculateLastState();
    }
}
