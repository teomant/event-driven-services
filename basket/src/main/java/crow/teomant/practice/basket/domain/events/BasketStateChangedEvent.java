package crow.teomant.practice.basket.domain.events;

import crow.teomant.common.EventSource;
import java.util.Set;
import java.util.UUID;
import lombok.Value;

@Value
public class BasketStateChangedEvent {
    UUID clientId;
    Set<UUID> added;
    Set<UUID> removed;
    EventSource source;
}
