package crow.teomant.practice.items.node.definition;

import static org.junit.jupiter.api.Assertions.assertTrue;

import crow.teomant.practice.items.node.value.AbstractValueNode;
import crow.teomant.practice.items.node.value.BooleanValueNode;
import crow.teomant.practice.items.node.value.NumericValueNode;
import crow.teomant.practice.items.node.value.StringValueNode;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AbstractDefinitionNodeTest {


    @ParameterizedTest
    @MethodSource("source")
    public void mapTest(AbstractDefinitionNode definition, AbstractValueNode node) {
        assertTrue(definition.validate(node));
    }

    private static Stream<Arguments> source() {
        return Stream.of(
            Arguments.of(new BooleanDefinitionNode(""), new BooleanValueNode(true)),
            Arguments.of(new NumericDefinitionNode(""), new NumericValueNode(new BigDecimal(1))),
            Arguments.of(new StringDefinitionNode(""), new StringValueNode("")),
            Arguments.of(new ObjectDefinitionNode("", new HashMap<>()), AbstractValueNode.of(new TestClass()))
        );
    }


    @Test
    public void complexTest() {
        BooleanDefinitionNode booleanDefinitionNode = new BooleanDefinitionNode("");
        NumericDefinitionNode numericDefinitionNode = new NumericDefinitionNode("");
        StringDefinitionNode stringDefinitionNode = new StringDefinitionNode("");
        HashMap<String, AbstractDefinitionNode> fields = new HashMap<>();
        fields.put("test", new NumericDefinitionNode(""));
        fields.put("test2", new StringDefinitionNode(""));
        ObjectDefinitionNode objectDefinitionNode = new ObjectDefinitionNode("", fields);
        HashMap<String, AbstractDefinitionNode> outerFields = new HashMap<>();
        outerFields.put("boolean", booleanDefinitionNode);
        outerFields.put("numeric", numericDefinitionNode);
        outerFields.put("string", stringDefinitionNode);
        outerFields.put("object", objectDefinitionNode);
        ObjectDefinitionNode outer = new ObjectDefinitionNode("", outerFields);

        HashMap<String, Object> value = new HashMap<>();
        value.put("boolean", true);
        value.put("numeric", 1);
        value.put("string", "true");
        HashMap<String, Object> object = new HashMap<>();
        object.put("test", 1);
        object.put("test2", "");
        value.put("object", object);

        assertTrue(outer.validate(AbstractValueNode.of(value)));
    }

    private static class TestClass {

    }
}