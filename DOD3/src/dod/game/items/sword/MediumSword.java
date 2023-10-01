package dod.game.items.sword;

import dod.game.items.GameItemConsumer;

public class MediumSword extends Sword {
    @Override
    public void processPickUp(GameItemConsumer player) {
        player.addToAP(2);
    }

    @Override
    public boolean isRetainable() {
        return true;
    }

    @Override
    public String toString() {
        return "Medium sword";
    }
}
