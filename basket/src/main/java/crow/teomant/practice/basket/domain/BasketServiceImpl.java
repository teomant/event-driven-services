package crow.teomant.practice.basket.domain;

import crow.teomant.common.EventSource;
import crow.teomant.domain.events.DomainEvents;
import crow.teomant.practice.basket.domain.events.BasketStateChangedEvent;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.SetUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasketServiceImpl implements BasketService {

    private final BasketRepository repository;
    private final DomainEvents domainEvents;

    @Override
    public void create(UUID clientId) {
        repository.save(new Basket(clientId, new ConcurrentHashMap<>()));
    }

    @Override
    public Basket change(UUID clientId, Map<UUID, Integer> items) {
        Basket basket = repository.get(clientId);

        //TODO: check if available
        Map<UUID, Integer> before = basket.getItems();
        basket.changeItems(items);

        Map<UUID, Integer> after = basket.getItems();

        domainEvents.raise(new BasketStateChangedEvent(clientId, SetUtils.difference(after.keySet(), before.keySet()),
            SetUtils.difference(before.keySet(), after.keySet()), new EventSource(UUID.randomUUID())));

        return basket;
    }

    @Override
    public void order(UUID clientId, Map<UUID, Integer> items) {
        Basket basket = repository.get(clientId);
        basket.order(items);
        repository.save(basket);
    }

    @Override
    public Basket get(UUID id) {
        return repository.get(id);
    }
}
