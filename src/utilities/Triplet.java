package utilities;

import java.util.Objects;

public class Triplet<A, B, C> {
    public final A a;
    public final B b;
    public final C c;

    public Triplet(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triplet<?, ?, ?> triplet = (Triplet<?, ?, ?>) o;
        return Objects.equals(a, triplet.a) && Objects.equals(b, triplet.b) && Objects.equals(c, triplet.c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c);
    }

    @Override
    public String toString() {
        return this.a + ", " + this.b + ", " + this.c;
    }
}