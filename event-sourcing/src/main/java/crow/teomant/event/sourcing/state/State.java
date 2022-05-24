package crow.teomant.event.sourcing.state;

import java.util.UUID;

public abstract class State {
    private final UUID id;

    protected State(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
