package crow.teomant.practice.items.node.value;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StringValueNode extends AbstractValueNode {

    private final String value;

}
