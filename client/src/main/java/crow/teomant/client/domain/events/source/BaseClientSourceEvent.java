package crow.teomant.client.domain.events.source;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import crow.teomant.client.domain.history.ClientState;
import crow.teomant.common.EventSource;
import crow.teomant.event.sourcing.event.source.BaseSourceEvent;
import java.time.OffsetDateTime;
import java.util.UUID;


@JsonTypeInfo(
    use = JsonTypeInfo.Id.MINIMAL_CLASS
)
public abstract class BaseClientSourceEvent extends BaseSourceEvent<OffsetDateTime, ClientState, EventSource> {

    protected BaseClientSourceEvent(UUID id, OffsetDateTime discriminant, Long version,
                                    crow.teomant.event.sourcing.source.EventSource eventSource) {
        super(id, discriminant, version, eventSource);
    }

    protected BaseClientSourceEvent(UUID id, OffsetDateTime discriminant,
                                    crow.teomant.event.sourcing.source.EventSource eventSource) {
        super(id, discriminant, eventSource);
    }
}
