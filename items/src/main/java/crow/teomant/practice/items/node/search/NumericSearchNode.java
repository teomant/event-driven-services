package crow.teomant.practice.items.node.search;


import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NumericSearchNode extends AbstractSearchNode {

    private final BigDecimal value;

}
