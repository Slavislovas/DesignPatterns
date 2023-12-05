package dod.iterator;

import dod.game.Player;

import java.util.Arrays;

public class PlayerList implements CustomCollection<Player>{
    private static final int DEFAULT_CAPACITY = 10;
    private Player[] players;
    private int size;

    public PlayerList() {
        this.players = new Player[DEFAULT_CAPACITY];
        this.size = 0;
    }

    @Override
    public Iterator<Player> getIterator() {
        System.out.println("PlayerList: getting iterator");
        return new ListIterator<>(this);
    }

    @Override
    public void add(Player element) {
        if (size == players.length) {
            ensureCapacity();
        }
        players[size++] = element;
    }

    @Override
    public void set(Player element, int index) {
        if (index < 0 || index >= players.length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (players[index] == null){
            size++;
        }
        players[index] = element;
    }

    @Override
    public void remove(Player element) {
        for (int i = 0; i < size; i++) {
            if (players[i].equals(element)) {
                removeItemAt(i);
                return;
            }
        }
    }

    private void removeItemAt(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        int numToMove = size - index - 1;
        if (numToMove > 0) {
            System.arraycopy(players, index + 1, players, index, numToMove);
        }
        players[--size] = null;
    }

    @Override
    public boolean contains(Player element) {
        for (int i = 0; i < size; i++) {
            if (players[i].equals(element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Player get(int index) {
        return players[index];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity() {
        int newCapacity = players.length * 2;
        players = Arrays.copyOf(players, newCapacity);
    }
}
