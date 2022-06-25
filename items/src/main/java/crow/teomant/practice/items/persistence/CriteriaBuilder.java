package crow.teomant.practice.items.persistence;

import crow.teomant.practice.items.node.search.AbstractSearchNode;
import crow.teomant.practice.items.node.search.BooleanSearchNode;
import crow.teomant.practice.items.node.search.NumericSearchNode;
import crow.teomant.practice.items.node.search.ObjectSearchNode;
import crow.teomant.practice.items.node.search.StringSearchNode;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import org.springframework.data.mongodb.core.query.Criteria;

public class CriteriaBuilder {

    public static Criteria build(AbstractSearchNode node, String pathStart) {
        ArrayList<Criteria> criteriaList = new ArrayList<>();
        if (node instanceof ObjectSearchNode) {
            ObjectSearchNode objectNode = (ObjectSearchNode) node;
            ArrayList<String> fullPath = new ArrayList<>();
            fullPath.add(pathStart);
            objectNode.getValue().entrySet().stream()
                .filter(entry -> !entry.getKey().equals("metadata"))
                .forEach(entry -> {
                    fullPath.add(entry.getKey());
                    if (entry.getValue() instanceof StringSearchNode) {
                        buildString(criteriaList, fullPath, (StringSearchNode) entry.getValue());
                    }
                    if (entry.getValue() instanceof NumericSearchNode) {
                        StringSearchNode metadata =
                            Optional.ofNullable(objectNode.get("metadata"))
                                .map(x -> (ObjectSearchNode) x)
                                .map(x -> x.get(entry.getKey()))
                                .map(x -> (StringSearchNode) x)
                                .orElse(null);
                        buildNumeric(criteriaList, fullPath, (NumericSearchNode) entry.getValue(), metadata);
                    }
                    if (entry.getValue() instanceof BooleanSearchNode) {
                        buildBoolean(criteriaList, fullPath, (BooleanSearchNode) entry.getValue());
                    }
                    if (entry.getValue() instanceof ObjectSearchNode) {
                        buildObject(criteriaList, fullPath, (ObjectSearchNode) entry.getValue());
                    }
                });
        } else {
            throw new IllegalArgumentException();
        }
        return new Criteria().andOperator(criteriaList);
    }

    private static void buildString(ArrayList<Criteria> criteriaList, List<String> fullPath,
                                    StringSearchNode objectNode) {
        ArrayList<String> localPath = new ArrayList<>(fullPath);
        String searchValue = objectNode.getValue();
        criteriaList.add(
            Criteria
                .where(String.join(".", localPath))
                .regex(Pattern.compile(searchValue, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE))
        );
    }

    private static void buildNumeric(ArrayList<Criteria> criteriaList, List<String> fullPath,
                                     NumericSearchNode objectNode,
                                     StringSearchNode typeNode) {
        ArrayList<String> localPath = new ArrayList<>(fullPath);
        BigDecimal searchValue = objectNode.getValue();
        if (Objects.nonNull(typeNode)) {
            String typeString = typeNode.getValue();
            switch (typeString) {
                case "eq":
                    criteriaList.add(Criteria.where(String.join(".", localPath)).is(searchValue));
                    break;
                case "lt":
                    criteriaList.add(Criteria.where(String.join(".", localPath)).lt(searchValue));
                    break;
                case "gt":
                    criteriaList.add(Criteria.where(String.join(".", localPath)).gt(searchValue));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        } else {
            criteriaList.add(Criteria.where(String.join(".", localPath)).is(searchValue));
        }
    }

    private static void buildBoolean(ArrayList<Criteria> criteriaList, List<String> fullPath,
                                     BooleanSearchNode objectNode) {
        ArrayList<String> localPath = new ArrayList<>(fullPath);
        Boolean booleanSearchValue = objectNode.getValue();
        criteriaList.add(
            Criteria
                .where(String.join(".", localPath))
                .is(booleanSearchValue)
        );
    }

    private static void buildObject(ArrayList<Criteria> criteriaList, List<String> fullPath,
                                    ObjectSearchNode objectNode) {
        objectNode.getValue().entrySet().stream()
            .filter(entry -> !entry.getKey().equals("metadata"))
            .forEach(entry -> {
                ArrayList<String> newFullPath = new ArrayList<>(fullPath);
                newFullPath.add(entry.getKey());
                if (entry.getValue() instanceof StringSearchNode) {
                    buildString(criteriaList, newFullPath, (StringSearchNode) entry.getValue());
                }
                if (entry.getValue() instanceof NumericSearchNode) {
                    StringSearchNode metadata =
                        Optional.ofNullable(objectNode.get("metadata"))
                            .map(x -> (ObjectSearchNode) x)
                            .map(x -> x.get(entry.getKey()))
                            .map(x -> (StringSearchNode) x)
                            .orElse(null);
                    buildNumeric(criteriaList, newFullPath, (NumericSearchNode) entry.getValue(),
                        metadata);
                }
                if (entry.getValue() instanceof BooleanSearchNode) {
                    buildBoolean(criteriaList, newFullPath, (BooleanSearchNode) entry.getValue());
                }
                if (entry.getValue() instanceof ObjectSearchNode) {
                    buildObject(criteriaList, newFullPath, (ObjectSearchNode) entry.getValue());
                }
            });
    }

}
