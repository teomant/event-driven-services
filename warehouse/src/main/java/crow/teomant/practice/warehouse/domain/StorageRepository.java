package crow.teomant.practice.warehouse.domain;

import java.util.Collection;
import java.util.UUID;

public interface StorageRepository {
    void save(Storage storage);

    Storage get(UUID id);

    Collection<Storage> getAll();
}
