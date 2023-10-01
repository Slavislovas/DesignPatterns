package dod.game.items.health;

import dod.game.items.GameItemConsumer;

public class WeakHealth extends Health {
    @Override
    public void processPickUp(GameItemConsumer player) {
        player.incrementHealth(1);
    }

    @Override
    public boolean isRetainable() {
        return false;
    }

    @Override
    public String toString() {
        return "Weak health potion";
    }
}
