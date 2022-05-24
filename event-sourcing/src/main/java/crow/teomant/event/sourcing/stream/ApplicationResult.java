package crow.teomant.event.sourcing.stream;

import java.util.UUID;

public class ApplicationResult {
    private final UUID eventId;
    private final String errorMessage;

    private final Boolean success;

    public ApplicationResult(UUID eventId, String errorMessage, Boolean success) {
        this.eventId = eventId;
        this.errorMessage = errorMessage;
        this.success = success;
    }

    public UUID getEventId() {
        return eventId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Boolean getSuccess() {
        return success;
    }
}
