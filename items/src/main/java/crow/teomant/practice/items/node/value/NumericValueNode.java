package crow.teomant.practice.items.node.value;


import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NumericValueNode extends AbstractValueNode {

    private final BigDecimal value;

}
