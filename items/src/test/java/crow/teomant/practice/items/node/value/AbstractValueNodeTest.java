package crow.teomant.practice.items.node.value;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AbstractValueNodeTest {

    @ParameterizedTest
    @MethodSource("source")
    public void mapTest(Object object, Class clazz) {
        assertEquals(clazz, AbstractValueNode.of(object).getClass());
    }

    private static Stream<Arguments> source() {
        return Stream.of(
            Arguments.of("TEST", StringValueNode.class),
            Arguments.of(1, NumericValueNode.class),
            Arguments.of(1.2, NumericValueNode.class),
            Arguments.of(true, BooleanValueNode.class),
            Arguments.of(new HashMap<>(), ObjectValueNode.class),
            Arguments.of(new TestClass(), ObjectValueNode.class)

        );
    }

    private static class TestClass {

    }
}