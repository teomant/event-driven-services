package crow.teomant.practice.basket.domain;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasketServiceImpl implements BasketService {

    private final BasketRepository repository;

    @Override
    public void create(UUID userId) {
        repository.save(new Basket(userId, new ConcurrentHashMap<>(), new ConcurrentSkipListSet<>()));
    }

    @Override
    public Basket change(UUID userId, Map<UUID, Integer> items) {
        Basket basket = repository.get(userId);
        basket.changeItems(items);

        return basket;
    }

    @Override
    public void order(UUID orderId, UUID userId, Map<UUID, Integer> items) {
        Basket basket = repository.get(userId);
        basket.order(orderId, items);
        repository.save(basket);
    }

    @Override
    public void revertOrder(UUID orderId, UUID userId, Map<UUID, Integer> items) {
        Basket basket = repository.get(userId);
        basket.revertOrder(orderId, items);
        repository.save(basket);
    }

    @Override
    public Basket get(UUID id) {
        return repository.get(id);
    }
}
