package utilities;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class UnOrderedPair<T> {
    public final T a;
    public final T b;

    private final Set<T> set;


    public UnOrderedPair(T a, T b) {
        this.a = a;
        this.b = b;
        this.set = new HashSet<>();
        this.set.add(a);
        this.set.add(b);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnOrderedPair<?> that = (UnOrderedPair<?>) o;
        return Objects.equals(set, that.set);
    }

    @Override
    public int hashCode() {
        return set.hashCode();
    }

    @Override
    public String toString() {
        return this.a + ", " + this.b;
    }

    public boolean contains(T item) {
        return this.set.contains(item);
    }
}