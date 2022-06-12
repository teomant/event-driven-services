package crow.teomant.practice.items.persistence;

import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class ItemDocument {

    @Id
    private UUID id;
    private Object description;
    @DBRef
    private ItemTypeDocument itemTypeDocument;
}
