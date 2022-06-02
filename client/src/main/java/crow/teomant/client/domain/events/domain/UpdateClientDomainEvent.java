package crow.teomant.client.domain.events.domain;

import crow.teomant.event.sourcing.source.EventSource;
import java.util.UUID;

public class UpdateClientDomainEvent extends BaseClientDomainEvent {
    private final UUID clientId;
    private final UUID preferedWarehouse;

    public UpdateClientDomainEvent(UUID id,
                                   EventSource eventSource,
                                   UUID clientId,
                                   UUID preferedWarehouse) {
        super(id, eventSource);
        this.clientId = clientId;
        this.preferedWarehouse = preferedWarehouse;
    }
}
