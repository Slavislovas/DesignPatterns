package dod.game.items.sword;

import dod.builder.SwordWeaponBuilder;
import dod.builder.WeaponDirector;
import dod.game.items.GameItemConsumer;

public class MediumSword extends Sword {
    @Override
    public void processPickUp(GameItemConsumer player) {
        player.addToAP(2);
        WeaponDirector weaponDirector = new WeaponDirector(new SwordWeaponBuilder());
        player.setWeapon(weaponDirector.build());
    }

    @Override
    public boolean isRetainable() {
        return true;
    }

    @Override
    public String toString() {
        return "Medium sword";
    }
}
