package crow.teomant.event.sourcing.state;

import java.util.UUID;

public abstract class State {
    private final UUID id;

    private final Long version;

    protected State(UUID id, Long version) {
        this.id = id;
        this.version = version;
    }

    public UUID getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }
}
