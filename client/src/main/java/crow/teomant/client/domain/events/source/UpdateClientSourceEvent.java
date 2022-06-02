package crow.teomant.client.domain.events.source;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import crow.teomant.client.domain.history.ClientState;
import crow.teomant.event.sourcing.source.EventSource;
import java.time.OffsetDateTime;
import java.util.UUID;

public class UpdateClientSourceEvent extends BaseClientSourceEvent {

    private final UUID preferedWarehouse;
    private final String name;

    @JsonCreator
    protected UpdateClientSourceEvent(
        @JsonProperty("id") UUID id,
        @JsonProperty("discriminant") OffsetDateTime discriminant,
        @JsonProperty("version") Long version,
        @JsonProperty("eventSource") EventSource eventSource,
        @JsonProperty("preferedWarehouse") UUID preferedWarehouse,
        @JsonProperty("name") String name) {
        super(id, discriminant, version, eventSource);
        this.preferedWarehouse = preferedWarehouse;
        this.name = name;
    }

    public UpdateClientSourceEvent(UUID id, OffsetDateTime discriminant, EventSource eventSource,
                                   UUID preferedWarehouse, String name) {
        super(id, discriminant, eventSource);
        this.preferedWarehouse = preferedWarehouse;
        this.name = name;
    }

    @Override
    public ClientState apply(ClientState clientState) {
        clientState.setPreferedWarehouse(preferedWarehouse);
        clientState.setName(name);

        return clientState;
    }
}
