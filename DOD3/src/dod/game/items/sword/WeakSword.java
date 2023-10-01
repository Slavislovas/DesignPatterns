package dod.game.items.sword;

import dod.game.items.GameItemConsumer;

public class WeakSword extends Sword {
    @Override
    public void processPickUp(GameItemConsumer player) {
        player.addToAP(1);
    }

    @Override
    public boolean isRetainable() {
        return true;
    }

    @Override
    public String toString() {
        return "Weak sword";
    }
}
