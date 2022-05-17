package crow.teomant.practice.basket.domain;

import java.util.UUID;

public interface BasketRepository {
    void save(Basket basket);

    Basket get(UUID id);
}
