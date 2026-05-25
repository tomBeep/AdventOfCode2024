import java.util.Objects;

public class WireNode {
    public String name;
    public Boolean value;

    public WireNode(String name, Boolean value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WireNode wireNode = (WireNode) o;
        return Objects.equals(name, wireNode.name) && Objects.equals(value, wireNode.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}
