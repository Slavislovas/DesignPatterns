package dod.Adapter;

import dod.game.items.GameItemConsumer;

public class MegaGoldPint {

    public void processPickUpGold(GameItemConsumer player) {
        player.addGold(100);
    }


    public boolean isRetainableGold() {
        return false;
    }

    public String toStringGold() {
        return "Mega gold pint";
    }

    public boolean isWalletGold() {
        return false;
    }

    public boolean isBigGold() {
        return true;
    }
}
