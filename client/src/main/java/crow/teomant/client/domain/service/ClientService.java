package crow.teomant.client.domain.service;

import crow.teomant.client.domain.history.ClientState;
import java.util.UUID;

public interface ClientService {
    ClientState register(String login, UUID preferedWarehouse, String name);
    ClientState update(UUID id, UUID preferedWarehouse, String name);
    ClientState get(UUID id);
}
