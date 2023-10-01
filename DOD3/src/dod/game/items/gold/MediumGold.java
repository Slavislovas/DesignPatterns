package dod.game.items.gold;

import dod.game.items.GameItemConsumer;

public class MediumGold extends Gold {
    @Override
    public void processPickUp(GameItemConsumer player) {
        player.addGold(2);
    }

    @Override
    public boolean isRetainable() {
        return false;
    }

    @Override
    public String toString() {
        return "Medium gold pile";
    }
}
