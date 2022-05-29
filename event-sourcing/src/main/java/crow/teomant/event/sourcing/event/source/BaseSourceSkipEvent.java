package crow.teomant.event.sourcing.event.source;

import java.util.UUID;

public interface BaseSourceSkipEvent {
    UUID getSkipId();
}
