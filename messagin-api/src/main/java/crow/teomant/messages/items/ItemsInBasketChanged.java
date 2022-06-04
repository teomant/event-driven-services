package crow.teomant.messages.items;

import crow.teomant.common.EventSource;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemsInBasketChanged {
    private UUID clientId;
    private Set<UUID> added;
    private Set<UUID> removed;
    private EventSource source;
}
