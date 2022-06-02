package crow.teomant.client.domain.history;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import crow.teomant.event.sourcing.state.State;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientState extends State {

    private String login;
    private UUID preferedWarehouse;
    private String name;

    public ClientState(
        UUID id,
        Long initialVersion
    ) {
        super(id, initialVersion);
    }

    @JsonCreator
    public ClientState(
        @JsonProperty("id") UUID id,
        @JsonProperty("initialVersion") Long initialVersion,
        @JsonProperty("login") String login,
        @JsonProperty("preferedWarehouse") UUID preferedWarehouse,
        @JsonProperty("name") String name
        ) {
        super(id, initialVersion);
        this.login = login;
        this.preferedWarehouse = preferedWarehouse;
        this.name = name;
    }

    public ClientState(ClientState state, Long newVersion) {
        super(state.getId(), newVersion);
        this.login = state.getLogin();
        this.preferedWarehouse = state.getPreferedWarehouse();
        this.name = state.getName();
    }
}
