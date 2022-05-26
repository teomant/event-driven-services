package crow.teomant.event.sourcing.history;

import crow.teomant.event.sourcing.state.State;
import java.util.UUID;

public class TestState extends State {

    private String testValue;

    public TestState(UUID id, Long version, String testValue) {
        super(id, version);
        this.testValue = testValue;
    }

    public String getTestValue() {
        return testValue;
    }

    public void setTestValue(String testValue) {
        this.testValue = testValue;
    }

}
