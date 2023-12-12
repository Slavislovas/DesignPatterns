package dod.game.items.armour;

import dod.bridgePattern.IItemType;
import dod.game.items.GameItem;
import dod.mediator.IMediator;
import dod.game.items.ItemType;

/**
 * A class to represent armour. So far this does nothing, but if attacking is
 * implemented, it could defend the player in the case of an attack.
 */
public class Armour extends GameItem {

    public Armour() { }

    public Armour(IItemType itemType, IMediator mediator) {
        super(itemType, mediator);
    }

    @Override
    public boolean isRetainable() {
        // An armour is retained
        return true;
    }

    @Override
    public String toString() { return itemType.getName() + " armour";  }

    @Override
    public char toChar() {
        return 'A';
    }

    @Override
    public String getType() {
        return ItemType.ARMOUR;
    }

    @Override
    public void act() {

    }
}
