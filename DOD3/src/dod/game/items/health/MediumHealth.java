package dod.game.items.health;

import dod.game.items.GameItemConsumer;

public class MediumHealth extends Health {

    @Override
    public void processPickUp(GameItemConsumer player) {
        player.incrementHealth(2);
    }

    @Override
    public boolean isRetainable() {
        return false;
    }

    @Override
    public String toString() {
        return "Medium health potion";
    }
}
