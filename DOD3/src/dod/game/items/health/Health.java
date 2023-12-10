package dod.game.items.health;

import dod.bridgePattern.IItemType;
import dod.game.items.GameItem;
import dod.game.items.GameItemConsumer;

/**
 * A class to represent health "potion", which is consumed immediately and gives
 * the player an extra HP.
 */
public class Health extends GameItem {

    public Health() { }

    public Health(IItemType itemType) {
        super(itemType);
    }

    @Override
    public void processPickUp(GameItemConsumer player) {
        player.handleRequest("Health", itemType.getValue());
    }

    @Override
    public boolean isRetainable() {
        return false;
    }

    @Override
    public String toString() {
        return itemType.getName() + " health potion";
    }

    @Override
    public char toChar() {
        return 'H';
    }
}
