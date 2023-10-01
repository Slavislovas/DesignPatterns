package dod.game.items.health;

import dod.game.items.GameItemConsumer;

public class StrongHealth extends Health {
    @Override
    public void processPickUp(GameItemConsumer player) {
        player.incrementHealth(5);
    }

    @Override
    public boolean isRetainable() {
        return false;
    }

    @Override
    public String toString() {
        return "Strong health potion";
    }
}
