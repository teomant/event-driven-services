package crow.teomant.client.domain.events.domain;

import crow.teomant.common.EventSource;
import java.util.UUID;
import lombok.Getter;

@Getter
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

    @Override
    public EventSource getEventSource() {
        return (crow.teomant.common.EventSource) super.getEventSource();
    }
}
