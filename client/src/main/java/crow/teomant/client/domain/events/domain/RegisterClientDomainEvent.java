package crow.teomant.client.domain.events.domain;

import crow.teomant.common.EventSource;
import java.util.UUID;
import lombok.Getter;

@Getter
public class RegisterClientDomainEvent extends BaseClientDomainEvent {
    private final UUID clientId;
    private final String login;
    private final UUID preferedWarehouse;

    public RegisterClientDomainEvent(UUID id,
                                     EventSource eventSource,
                                     UUID clientId,
                                     String login,
                                     UUID preferedWarehouse) {
        super(id, eventSource);
        this.clientId = clientId;
        this.login = login;
        this.preferedWarehouse = preferedWarehouse;
    }

    @Override
    public EventSource getEventSource() {
        return (EventSource) super.getEventSource();
    }
}
