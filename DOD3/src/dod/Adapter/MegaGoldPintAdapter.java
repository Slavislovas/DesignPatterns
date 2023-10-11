package dod.Adapter;

import dod.game.items.GameItemConsumer;
import dod.game.items.gold.Gold;

public class MegaGoldPintAdapter extends Gold {
    private final MegaGoldPint megaGoldPint = new MegaGoldPint();

    @Override
    public void processPickUp(GameItemConsumer player) {
        megaGoldPint.processPickUpGold(player);
    }

    @Override
    public boolean isRetainable() {
       return megaGoldPint.isRetainableGold();
    }

    @Override
    public String toString() {
        return megaGoldPint.toStringGold();
    }
}
