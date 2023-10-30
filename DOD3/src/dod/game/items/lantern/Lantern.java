package dod.game.items.lantern;

import dod.bridgePattern.IItemType;
import dod.game.items.GameItem;

/**
 * A class to represent a lantern, which the player holds to allow the player to
 * see farther.
 */
public class Lantern extends GameItem {

    public Lantern() { }

    public Lantern(IItemType itemType) {
        super(itemType);
    }

    @Override
    public boolean isRetainable() {
        return true;
    }

    @Override
    public int lookDistanceIncrease() {
        return itemType.getValue();
    }

    @Override
    public String toString() {
        return itemType.getName() + " lantern";
    }

    @Override
    public char toChar() {
        return 'L';
    }
}
