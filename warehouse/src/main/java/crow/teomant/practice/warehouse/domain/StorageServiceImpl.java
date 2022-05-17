package crow.teomant.practice.warehouse.domain;

import static java.lang.Math.abs;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final StorageRepository repository;

    @Override
    public void create(UUID userId, Integer distance) {
        repository.save(new Storage(userId, new ConcurrentHashMap<>(), new ConcurrentHashMap<>(), distance));
    }

    @Override
    public Storage change(UUID userId, Map<UUID, Integer> items) {
        Storage storage = repository.get(userId);
        storage.changeItems(items);

        return storage;
    }

    @Override
    public void order(UUID orderId, Map<UUID, Integer> items, Integer distance) {
        List<Storage> storages = repository.getAll().stream()
            .sorted(Comparator.comparingInt(storage -> abs(storage.getDistance() - distance)))
            .collect(Collectors.toList());

        final Map<UUID, Integer> left = new HashMap<>(items);

        storages.forEach(storage -> {
            Map<UUID, Integer> newLeft = storage.order(orderId, left);
            left.clear();
            left.putAll(newLeft);
        });

        if (!left.isEmpty()) {
            storages.forEach(storage -> storage.revertOrder(orderId));
            throw new IllegalStateException();
        }

    }

    @Override
    public void revertOrder(UUID orderId) {
        repository.getAll().forEach(storage -> storage.revertOrder(orderId));
    }

    @Override
    public Storage get(UUID id) {
        return repository.get(id);
    }
}
