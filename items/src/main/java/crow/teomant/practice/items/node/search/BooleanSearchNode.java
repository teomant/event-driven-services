package crow.teomant.practice.items.node.search;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BooleanSearchNode extends AbstractSearchNode {

    private final Boolean value;

}
