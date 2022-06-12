package crow.teomant.practice.items.node.value;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BooleanValueNode extends AbstractValueNode {

    private final Boolean value;

}
