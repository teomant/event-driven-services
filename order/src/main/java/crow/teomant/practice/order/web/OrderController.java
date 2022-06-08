package crow.teomant.practice.order.web;

import crow.teomant.practice.order.domain.Order;
import crow.teomant.practice.order.domain.OrderService;
import java.util.Map;
import java.util.UUID;
import lombok.Data;
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
public class OrderController {
    private final OrderService orderService;

//    @PostMapping("/new")
//    public UUID create(@RequestBody CreateDto dto) {
//        return orderService.create(dto.getUserId(), dto.getItems(), dto.getDistance());
//    }
//
//    @GetMapping("/{id}")
//    public Order get(@PathVariable UUID id) {
//        return orderService.get(id);
//    }
//
//    @GetMapping("/{id}/start")
//    public void start(@PathVariable UUID id) {
//        orderService.start(id);
//    }
//
//    @GetMapping("/{id}/revert")
//    public void revert(@PathVariable UUID id) {
//        orderService.revert(id);
//    }
//
//    @Data
//    public static class CreateDto {
//        private UUID userId;
//        private Map<UUID, Integer> items;
//        private Integer distance;
//    }
}
