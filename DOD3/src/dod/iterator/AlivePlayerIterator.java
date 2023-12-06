package dod.iterator;

import dod.game.Player;

public class AlivePlayerIterator implements Iterator<Player>{
    private final PlayerList elements;
    private int currentIndex;

    public AlivePlayerIterator(PlayerList elements) {
        this.elements = elements;
        this.currentIndex = 0;
    }

    @Override
    public boolean hasNext() {
        if (elements.size() == 0){
            return false;
        }

        int nextIndex = currentIndex;
        while (nextIndex < elements.size() && elements.get(nextIndex).isDead()) {
            nextIndex++;
        }
        return nextIndex < elements.size();
    }

    @Override
    public Player next() {
        if (hasNext()) {
            while (currentIndex < elements.size() && elements.get(currentIndex).isDead()) {
                currentIndex++;
            }
            if (currentIndex < elements.size()) {
                return elements.get(currentIndex++);
            } else {
                throw new IllegalStateException("No more elements");
            }
        } else {
            throw new IllegalStateException("No more elements");
        }
    }

    @Override
    public Player first() {
        if (!elements.isEmpty()) {
            currentIndex = 0;
            return elements.get(currentIndex);
        } else {
            throw new IllegalStateException("List is empty");
        }
    }
}
