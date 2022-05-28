package crow.teomant.domain.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class SimpleSynchronousDomainEvents implements DomainEvents {

    private final Map<Class<?>, Consumers<?>> consumersMap = new ConcurrentHashMap<>();

    @Override
    public <T> Registration<T> listenTo(Class<T> eventClass, Consumer<T> consumer) {
        Consumers<T> consumers = (Consumers<T>) consumersMap.computeIfAbsent(eventClass,
            c -> new Consumers<T>(eventClass));
        consumers.addListener(consumer);

        return () -> consumers.remove(consumer);
    }

    @Override
    public void raise(Object object) {
        Consumers<?> consumers = consumersMap.computeIfAbsent(object.getClass(),
            c -> new Consumers<>(object.getClass()));

        consumers.consume(object);

    }
}
