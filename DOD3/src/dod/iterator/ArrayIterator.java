package dod.iterator;

import java.util.Arrays;

public class ArrayIterator<T> implements Iterator<T>{
    private final T[] elements;
    private int currentIndex;

    public ArrayIterator(T[] elements) {
        this.elements = elements;
        this.currentIndex = 0;
    }

    @Override
    public boolean hasNext() {
        return currentIndex < elements.length;
    }

    @Override
    public T next() {
        if (hasNext()) {
            return elements[currentIndex++];
        } else {
            throw new IllegalStateException("No more elements");
        }
    }

    @Override
    public T first() {
        if (elements.length == 0) {
            throw new IllegalStateException("Array is empty");
        }

        currentIndex = 0;
        return (T) elements[currentIndex];
    }

    private int findIndex(T item) {
        for (int i = 0; i < elements.length; i++) {
            if (item.equals(elements[i])) {
                return i;
            }
        }
        return -1;
    }
}
