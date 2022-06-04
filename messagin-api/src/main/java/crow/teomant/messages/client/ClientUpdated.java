package crow.teomant.messages.client;


import crow.teomant.common.EventSource;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientUpdated {
    private UUID clientId;
    private UUID preferedWarehouse;
    private EventSource eventSource;
}
