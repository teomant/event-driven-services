package crow.teomant.domain.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.function.Consumer;
import org.junit.jupiter.api.Test;

class SimpleSynchronousDomainEventsTest {

    @Test
    public void registerTest() {
        SimpleSynchronousDomainEvents domainEvents = new SimpleSynchronousDomainEvents();

        StringConsumer consumer = new StringConsumer();
        domainEvents.listenTo(String.class, consumer);

        domainEvents.raise("Test");

        assertEquals(consumer.getValue(), "Test");
    }

    @Test
    public void registerMultipleTest() {
        SimpleSynchronousDomainEvents domainEvents = new SimpleSynchronousDomainEvents();

        StringConsumer consumer = new StringConsumer();
        StringConsumer consumer2 = new StringConsumer();
        domainEvents.listenTo(String.class, consumer);
        domainEvents.listenTo(String.class, consumer2);

        domainEvents.raise("Test");

        assertEquals(consumer.getValue(), "Test");
        assertEquals(consumer2.getValue(), "Test");
    }

    @Test
    public void registerDifferentTest() {
        SimpleSynchronousDomainEvents domainEvents = new SimpleSynchronousDomainEvents();

        StringConsumer consumer = new StringConsumer();
        IntegerConsumer consumer2 = new IntegerConsumer();
        domainEvents.listenTo(String.class, consumer);
        domainEvents.listenTo(Integer.class, consumer2);

        domainEvents.raise("Test");
        domainEvents.raise(1);

        assertEquals(consumer.getValue(), "Test");
        assertEquals(consumer2.getValue(), 1);
    }

    @Test
    public void unregisterTest() {
        SimpleSynchronousDomainEvents domainEvents = new SimpleSynchronousDomainEvents();

        StringConsumer consumer = new StringConsumer();
        StringConsumer consumer2 = new StringConsumer();
        domainEvents.listenTo(String.class, consumer);
        Registration<String> stringRegistration = domainEvents.listenTo(String.class, consumer2);

        domainEvents.raise("Test");

        assertEquals(consumer.getValue(), "Test");
        assertEquals(consumer2.getValue(), "Test");

        stringRegistration.unsubscribe();

        domainEvents.raise("Test2");

        assertEquals(consumer.getValue(), "Test2");
        assertEquals(consumer2.getValue(), "Test");
    }

    @Test
    public void unregisteredTest() {
        SimpleSynchronousDomainEvents domainEvents = new SimpleSynchronousDomainEvents();

        StringConsumer consumer = new StringConsumer();

        domainEvents.raise(1);

        assertNull(consumer.getValue());
    }


    private static class StringConsumer implements Consumer<String> {

        private String value;

        @Override
        public void accept(String s) {
            this.value = s;
        }

        public String getValue() {
            return value;
        }
    }

    private static class IntegerConsumer implements Consumer<Integer> {

        private Integer value;

        @Override
        public void accept(Integer s) {
            this.value = s;
        }

        public Integer getValue() {
            return value;
        }
    }

}