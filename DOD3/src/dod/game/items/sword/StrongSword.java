package dod.game.items.sword;

import dod.game.items.GameItemConsumer;

public class StrongSword extends Sword {
    @Override
    public void processPickUp(GameItemConsumer player) {
        player.addToAP(5);
    }

    @Override
    public boolean isRetainable() {
        return true;
    }

    @Override
    public String toString() {
        return "Strong sword";
    }
}
