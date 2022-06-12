package crow.teomant.practice.items.persistence;

import com.fasterxml.jackson.databind.util.JSONPObject;
import crow.teomant.practice.items.node.definition.AbstractDefinitionNode;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class ItemTypeDocument {

    @Id
    private UUID id;

    private Object definition;
}
