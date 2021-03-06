package crow.teomant.client.domain.events.source;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import crow.teomant.client.domain.history.ClientState;
import crow.teomant.event.sourcing.source.EventSource;
import java.time.OffsetDateTime;
import java.util.UUID;

public class RegisterClientSourceEvent extends BaseClientSourceEvent {

    private final String login;
    private final UUID preferedWarehouse;
    private final String name;

    @JsonCreator
    protected RegisterClientSourceEvent(
        @JsonProperty("id") UUID id,
        @JsonProperty("discriminant") OffsetDateTime discriminant,
        @JsonProperty("version") Long version,
        @JsonProperty("eventSource") EventSource eventSource,
        @JsonProperty("login") String login,
        @JsonProperty("preferedWarehouse") UUID preferedWarehouse,
        @JsonProperty("name") String name) {
        super(id, discriminant, version, eventSource);
        this.login = login;
        this.preferedWarehouse = preferedWarehouse;
        this.name = name;
    }

    public RegisterClientSourceEvent(UUID id, OffsetDateTime discriminant, EventSource eventSource,
                                     String login, UUID preferedWarehouse, String name) {
        super(id, discriminant, eventSource);
        this.login = login;
        this.preferedWarehouse = preferedWarehouse;
        this.name = name;
    }

    @Override
    public ClientState apply(ClientState clientState) {
        clientState.setLogin(login);
        clientState.setPreferedWarehouse(preferedWarehouse);
        clientState.setName(name);

        return clientState;
    }
}
