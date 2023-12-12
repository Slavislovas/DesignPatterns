package dod.game.items.lantern;

import dod.bridgePattern.IItemType;
import dod.game.items.GameItem;
import dod.mediator.IMediator;
import dod.game.items.ItemType;

/**
 * A class to represent a lantern, which the player holds to allow the player to
 * see farther.
 */
public class Lantern extends GameItem {

    private int _distanceToAdd = 1;

    public Lantern() { }

    public Lantern(IItemType itemType, IMediator mediator) {
        super(itemType, mediator);
        _distanceToAdd = itemType.getValue();
    }

    @Override
    public boolean isRetainable() {
        return true;
    }

    @Override
    public int lookDistanceIncrease() {
        return _distanceToAdd;
    }

    @Override
    public String toString() {
        return itemType.getName() + " lantern";
    }

    @Override
    public char toChar() {
        return 'L';
    }

    @Override
    public String getType() {
        return ItemType.LANTERN;
    }

    @Override
    public void act() {
        if(_distanceToAdd > 1) _distanceToAdd--;
    }
}
