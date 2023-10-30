package dod.game.items.sword;

import dod.bridgePattern.IItemType;
import dod.builder.SwordWeaponBuilder;
import dod.builder.WeaponDirector;
import dod.game.items.GameItem;
import dod.game.items.GameItemConsumer;

/**
 * A class to represent sword. So far this does nothing, but if attacking is
 * implemented, it could increase the attack potential of a player.
 */
public class Sword extends GameItem {

    public Sword() { }

    public Sword(IItemType itemType) {
        super(itemType);
    }

    @Override
    public void processPickUp(GameItemConsumer player) {
        player.addToAP(itemType.getValue());
        WeaponDirector weaponDirector = new WeaponDirector(new SwordWeaponBuilder());
        player.setWeapon(weaponDirector.build());
    }

    @Override
    public boolean isRetainable() {
        return true;
    }

    @Override
    public String toString() {
        return itemType.getName() + " sword";
    }

    @Override
    public char toChar() {
        return 'S';
    }
}
