package crow.teomant.practice.warehouse.web;

import crow.teomant.practice.warehouse.domain.Storage;
import crow.teomant.practice.warehouse.domain.StorageService;
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
public class StorageController {
    private final StorageService storageService;

    @PostMapping("/new")
    public void create(@RequestBody CreateDto dto) {
        storageService.create(dto.getId(), dto.getDistance());
    }

    @GetMapping("/{id}")
    public Storage get(@PathVariable UUID id) {
        return storageService.get(id);
    }

    @PostMapping("/{id}")
    public void change(@PathVariable UUID id, @RequestBody Map<UUID, Integer> items) {
        storageService.change(id, items);
    }

    @Data
    public static class CreateDto {
        private UUID id;
        private Integer distance;
    }
}
