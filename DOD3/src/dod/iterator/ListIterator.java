package dod.iterator;

import java.util.List;

public class ListIterator<T> implements Iterator<T>{
    private final List<T> elements;
    private int currentIndex;

    public ListIterator(List<T> elements) {
        this.elements = elements;
        this.currentIndex = 0;
    }

    @Override
    public boolean hasNext() {
        return currentIndex < elements.size();
    }

    @Override
    public T next() {
        if (hasNext()) {
            T nextItem = elements.get(currentIndex);
            currentIndex++;
            return nextItem;
        } else {
            throw new IllegalStateException("No more elements");
        }
    }

    @Override
    public T first() {
        if (!elements.isEmpty()) {
            currentIndex = 0;
            return elements.get(currentIndex);
        } else {
            throw new IllegalStateException("List is empty");
        }
    }
}
