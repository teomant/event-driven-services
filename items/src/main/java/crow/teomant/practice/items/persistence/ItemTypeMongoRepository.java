package crow.teomant.practice.items.persistence;

import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemTypeMongoRepository extends MongoRepository<ItemTypeDocument, UUID> {
}
