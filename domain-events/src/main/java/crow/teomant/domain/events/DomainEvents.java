package crow.teomant.domain.events;

import java.util.function.Consumer;

public interface DomainEvents {

    <T> Registration<T> listenTo(Class<T> eventClass, Consumer<T> consumer);

    void raise(Object object);

}
