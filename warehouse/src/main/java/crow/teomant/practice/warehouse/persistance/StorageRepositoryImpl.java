package crow.teomant.practice.warehouse.persistance;

import crow.teomant.practice.warehouse.domain.Storage;
import crow.teomant.practice.warehouse.domain.StorageRepository;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class StorageRepositoryImpl implements StorageRepository {

    private final Map<UUID, Storage> storages = new ConcurrentHashMap<>();

    @Override
    public void save(Storage storage) {
        storages.put(storage.getId(), storage);
    }

    @Override
    public Storage get(UUID id) {
        return storages.get(id);
    }

    @Override
    public Collection<Storage> getAll() {
        return storages.values();
    }
}
