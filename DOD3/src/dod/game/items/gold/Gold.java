package dod.game.items.gold;

import dod.bridgePattern.IItemType;
import dod.game.items.GameItem;
import dod.game.items.GameItemConsumer;

/**
 * An item to represent gold
 */
public class Gold extends GameItem {

    public Gold() { }

    public Gold(IItemType itemType) {
        super(itemType);
    }

    @Override
    public void processPickUp(GameItemConsumer player) {
        player.addGold(itemType.getValue());
    }

    @Override
    public boolean isRetainable() {
        return false;
    }

    @Override
    public String toString() { return itemType.getName() + " gold pile"; }

    @Override
    public char toChar() {
        return 'G';
    }
}
