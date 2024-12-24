import java.util.Objects;

public class GateNode extends WireNode{
    public WireNode input1;
    public WireNode input2;
    public String operator;


    public GateNode(String name, Boolean value, WireNode input1, WireNode input2, String operator) {
        super(name, value);
        this.input1 = input1;
        this.input2 = input2;
        this.operator = operator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GateNode gateNode = (GateNode) o;
        return Objects.equals(input1, gateNode.input1) && Objects.equals(input2, gateNode.input2) && Objects.equals(operator, gateNode.operator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input1, input2, operator);
    }
}

