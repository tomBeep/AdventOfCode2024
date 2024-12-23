package utilities;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class UnOrderedTriplet<T> {
    public final T a;
    public final T b;
    public final T c;

    private final Set<T> set;


    public UnOrderedTriplet(T a, T b, T c) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.set = new HashSet<>();
        this.set.add(a);
        this.set.add(b);
        this.set.add(c);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnOrderedTriplet<?> that = (UnOrderedTriplet<?>) o;
        return Objects.equals(set, that.set);
    }

    @Override
    public int hashCode() {
        return set.hashCode();
    }

    @Override
    public String toString() {
        return this.a + ", " + this.b + ", " + this.c;
    }

    public boolean contains(T item) {
        return this.set.contains(item);
    }

    public boolean intersectsWith(UnOrderedTriplet<T> other) {
        HashSet<T> newSet = new HashSet<>(this.set);
        newSet.retainAll(other.set);
        return !newSet.isEmpty();
    }
}