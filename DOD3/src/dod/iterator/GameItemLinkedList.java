package dod.iterator;

import dod.game.items.GameItem;

public class GameItemLinkedList implements CustomCollection<GameItem> {
    private Node<GameItem> head;
    private int size;

    public GameItemLinkedList() {
        this.head = null;
        this.size = 0;
    }

    @Override
    public Iterator<GameItem> getIterator() {
        return new LinkedListIterator<>(head);
    }

    @Override
    public void add(GameItem direction) {
        Node<GameItem> newNode = new Node<>(direction);
        if (head == null) {
            head = newNode;
        } else {
            Node<GameItem> current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newNode);
        }
        size++;
    }

    @Override
    public void set(GameItem element, int index) {

    }

    @Override
    public void remove(GameItem direction) {
        Node<GameItem> current = head;
        Node<GameItem> prev = null;

        while (current != null && !current.getData().equals(direction)) {
            prev = current;
            current = current.getNext();
        }

        if (current != null) {
            if (prev == null) {
                head = current.getNext();
            } else {
                prev.setNext(current.getNext());
            }
            size--;
        }
    }

    @Override
    public boolean contains(GameItem direction) {
        Node<GameItem> current = head;
        while (current != null) {
            if (current.getData().equals(direction)) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public GameItem get(int index) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
