package crow.teomant.practice.basket.persistance;

import crow.teomant.practice.basket.domain.Basket;
import crow.teomant.practice.basket.domain.BasketRepository;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class BasketRepositoryImpl implements BasketRepository {

    private final Map<UUID, Basket> storage = new ConcurrentHashMap<>();

    @Override
    public void save(Basket basket) {
        storage.put(basket.getId(), basket);
    }

    @Override
    public Basket get(UUID id) {
        return storage.get(id);
    }
}
