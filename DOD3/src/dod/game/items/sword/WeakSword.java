package dod.game.items.sword;

import dod.builder.SwordWeaponBuilder;
import dod.builder.WeaponDirector;
import dod.game.items.GameItemConsumer;

public class WeakSword extends Sword {
    @Override
    public void processPickUp(GameItemConsumer player) {
        player.addToAP(1);
        WeaponDirector weaponDirector = new WeaponDirector(new SwordWeaponBuilder());
        player.setWeapon(weaponDirector.build());
    }

    @Override
    public boolean isRetainable() {
        return true;
    }

    @Override
    public String toString() {
        return "Weak sword";
    }
}
