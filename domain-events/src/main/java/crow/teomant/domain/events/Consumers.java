package crow.teomant.domain.events;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class Consumers<T> {

    private final Class<T> type;
    final Set<Consumer<T>> consumers = new HashSet<>();


    public Consumers(Class<T> type) {
        this.type = type;
    }

    public synchronized void addListener(Consumer<T> consumer) {
        consumers.add(consumer);
    }

    public synchronized void remove(Consumer<T> consumer) {
        consumers.remove(consumer);
    }

    public void consume(Object object) {
        if (type.isInstance(object)) {
            consumers.forEach(consumer -> consumer.accept(type.cast(object)));
        }
    }
}
