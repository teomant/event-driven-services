package crow.teomant.practice.items.persistence;

import crow.teomant.practice.items.node.search.AbstractSearchNode;
import crow.teomant.practice.items.node.search.BooleanSearchNode;
import crow.teomant.practice.items.node.search.NumericSearchNode;
import crow.teomant.practice.items.node.search.ObjectSearchNode;
import crow.teomant.practice.items.node.search.StringSearchNode;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import org.springframework.data.mongodb.core.query.Criteria;

public class CriteriaBuilder {

    public static Criteria build(AbstractSearchNode node) {
        Criteria criteria = new Criteria();
        List<String> fullPath = new ArrayList<>();
        if (node instanceof ObjectSearchNode) {
            ObjectSearchNode objectNode = (ObjectSearchNode) node;
            if (objectNode.get("value") instanceof StringSearchNode) {
                buildString(criteria, fullPath, objectNode);
            }
            if (objectNode.get("value") instanceof NumericSearchNode) {
                buildNumeric(criteria, fullPath, objectNode);
            }
            if (objectNode.get("value") instanceof BooleanSearchNode) {
                buildBoolean(criteria, fullPath, objectNode);
            }
            if (objectNode.get("value") instanceof ObjectSearchNode) {
                buildObject(criteria, fullPath, objectNode);
            }
        } else {
            throw new IllegalArgumentException();
        }
        return criteria;
    }

    private static void buildString(Criteria criteria, List<String> fullPath, ObjectSearchNode objectNode) {
        Map value = (Map) objectNode.getValue();
        String path = ((StringSearchNode) value.get("path")).getValue();
        ArrayList<String> localPath = new ArrayList<>(fullPath);
        localPath.add(path);
        StringSearchNode stringValue = (StringSearchNode) value.get("value");
        String searchValue = ((StringSearchNode) value.get("value")).getValue();
        criteria.andOperator(
            Criteria
                .where(String.join(".", localPath))
                .regex(Pattern.compile(searchValue, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE))
        );
    }

    private static void buildNumeric(Criteria criteria, List<String> fullPath, ObjectSearchNode objectNode) {
        Map value = (Map) objectNode.getValue();
        String path = ((StringSearchNode) value.get("path")).getValue();
        ArrayList<String> localPath = new ArrayList<>(fullPath);
        localPath.add(path);
        BigDecimal searchValue = ((NumericSearchNode) value.get("value")).getValue();
        Object type = value.get("type");
        if (Objects.nonNull(type)) {
            String typeString = ((StringSearchNode) type).getValue();
            switch (typeString) {
                case "eq":
                    criteria.andOperator(Criteria.where(String.join(".", localPath)).is(searchValue));
                    break;
                case "lt":
                    criteria.andOperator(Criteria.where(String.join(".", localPath)).lt(searchValue));
                    break;
                case "gt":
                    criteria.andOperator(Criteria.where(String.join(".", localPath)).gt(searchValue));
                    break;
                default: throw new IllegalArgumentException();
            }
        } else {
            criteria.andOperator(Criteria.where(String.join(".", localPath)).is(searchValue));
        }
    }

    private static void buildBoolean(Criteria criteria, List<String> fullPath, ObjectSearchNode objectNode) {
        Map value = (Map) objectNode.getValue();
        String path = ((StringSearchNode) value.get("path")).getValue();
        ArrayList<String> localPath = new ArrayList<>(fullPath);
        localPath.add(path);
        Boolean booleanSearchValue = ((BooleanSearchNode) value.get("value")).getValue();
        criteria.andOperator(
            Criteria
                .where(String.join(".", localPath))
                .is(booleanSearchValue)
        );
    }

    private static void buildObject(Criteria criteria, List<String> fullPath, ObjectSearchNode objectNode) {
        Map value = (Map) objectNode.getValue();
        String path = ((StringSearchNode) value.get("path")).getValue();
        ArrayList<String> localPath = new ArrayList<>(fullPath);
        localPath.add(path);
        ObjectSearchNode inner = (ObjectSearchNode) value.get("value");
        if (inner.get("value") instanceof StringSearchNode) {
            buildString(criteria, localPath, inner);
        }
        if (inner.get("value") instanceof NumericSearchNode) {
            buildNumeric(criteria, localPath, inner);
        }
        if (inner.get("value") instanceof BooleanSearchNode) {
            buildBoolean(criteria, localPath, inner);
        }
        if (inner.get("value") instanceof ObjectSearchNode) {
            buildObject(criteria, localPath, inner);
        }
    }

}
