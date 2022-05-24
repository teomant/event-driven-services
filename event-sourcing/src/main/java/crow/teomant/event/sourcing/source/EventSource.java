package crow.teomant.event.sourcing.source;

import java.util.UUID;

public interface EventSource {

    UUID getCorrelationId();
}
