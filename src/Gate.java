import java.util.Objects;

public final class Gate {
    private final String input1;
    private final String Input2;
    private final String operator;
    private String output;

    public Gate(String input1, String Input2, String operator, String output) {
        this.input1 = input1;
        this.Input2 = Input2;
        this.operator = operator;
        this.output = output;
    }

    public String input1() {
        return input1;
    }

    public String Input2() {
        return Input2;
    }

    public String operator() {
        return operator;
    }

    public String output() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Gate) obj;
        return Objects.equals(this.input1, that.input1) &&
                Objects.equals(this.Input2, that.Input2) &&
                Objects.equals(this.operator, that.operator) &&
                Objects.equals(this.output, that.output);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input1, Input2, operator, output);
    }

    @Override
    public String toString() {
        return "Gate[" +
                "input1=" + input1 + ", " +
                "Input2=" + Input2 + ", " +
                "operator=" + operator + ", " +
                "output=" + output + ']';
    }
}
