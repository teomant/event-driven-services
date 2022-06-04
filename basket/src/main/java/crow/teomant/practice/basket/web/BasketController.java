package crow.teomant.practice.basket.web;

import crow.teomant.practice.basket.domain.Basket;
import crow.teomant.practice.basket.domain.BasketService;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class BasketController {
    private final BasketService basketService;

    @GetMapping("/{id}")
    public Basket get(@PathVariable UUID id) {
        return basketService.get(id);
    }

    @PostMapping("/{id}")
    public void change(@PathVariable UUID id, @RequestBody Map<UUID, Integer> items) {
        basketService.change(id, items);
    }

}
