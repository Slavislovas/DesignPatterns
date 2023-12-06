package dod.iterator;


public class LinkedListIterator<T> implements Iterator<T>{
    private Node<T> head;
    private Node<T> currentNode;

    public LinkedListIterator(Node<T> head) {
       this.currentNode = head;
       this.head = head;
    }

    @Override
    public boolean hasNext() {
        return currentNode != null;
    }

    @Override
    public T next() {
        if (hasNext()) {
            T currentData = currentNode.getData();
            currentNode = currentNode.getNext();
            return currentData;
        } else {
            throw new IllegalStateException("No more elements");
        }
    }

    @Override
    public T first() {
        currentNode = new Node<T>(head.getData());
        currentNode.setNext(head.getNext());
        return currentNode.getData();
    }
}
