package dod.iterator;

public interface Iterator<T> {
    boolean hasNext();
    T next();
    T first();
}
