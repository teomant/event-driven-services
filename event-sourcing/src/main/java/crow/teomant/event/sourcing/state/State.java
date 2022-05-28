package crow.teomant.event.sourcing.state;

import java.util.UUID;

public abstract class State {
    private final UUID id;

    private final Long initialVersion;

    protected State(UUID id, Long initialVersion) {
        this.id = id;
        this.initialVersion = initialVersion;
    }

    public UUID getId() {
        return id;
    }

    public Long getInitialVersion() {
        return initialVersion;
    }
}
