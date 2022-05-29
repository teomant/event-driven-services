package crow.teomant.client.domain.service;

import crow.teomant.client.domain.history.ClientState;
import java.util.UUID;

public interface ClientService {
    ClientState register(String login, UUID preferedWarehouse);
    ClientState get(UUID id);
}
