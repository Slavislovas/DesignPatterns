package dod.iterator;

public interface CustomCollection<T> extends IterableCollection<T>{
    void add(T element);
    void set(T element, int index);
    void remove(T element);
    boolean contains(T element);
    int size();
    T get(int index);
    boolean isEmpty();
}
