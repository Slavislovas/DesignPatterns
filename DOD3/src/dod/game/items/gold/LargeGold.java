package dod.game.items.gold;

import dod.game.items.GameItemConsumer;

public class LargeGold extends Gold {
    @Override
    public void processPickUp(GameItemConsumer player) {
        player.addGold(5);
    }

    @Override
    public boolean isRetainable() {
        return false;
    }

    @Override
    public String toString() {
        return "Large gold pile";
    }
}
