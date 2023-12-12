package dod.game.items.sword;

import dod.bridgePattern.IItemType;
import dod.builder.*;
import dod.game.items.GameItem;
import dod.game.items.GameItemConsumer;
import dod.mediator.IMediator;
import dod.game.items.ItemType;
import dod.proxy.WeaponBuilderType;
import dod.proxy.WeaponDirectorProxy;

/**
 * A class to represent sword. So far this does nothing, but if attacking is
 * implemented, it could increase the attack potential of a player.
 */
public class Sword extends GameItem {
    private int _attackPowerToAdd = 1;

    private final IWeaponDirector weaponDirector;

    public Sword() { this.weaponDirector = new WeaponDirectorProxy(); }

    public Sword(IItemType itemType, IMediator mediator, IWeaponDirector director) {
        super(itemType,mediator);
        _attackPowerToAdd = itemType.getValue();
        this.weaponDirector = director;
    }

    @Override
    public void processPickUp(GameItemConsumer player) {
        Weapon sword = weaponDirector.setBuilder(WeaponBuilderType.SWORD).build(player.getName());
        if (sword != null) {
            WeaponType weaponType = player.getWeapon().getType();
            if (weaponType == WeaponType.SWORD) {
                System.out.printf("%d additional attack power added%n", sword.getDamage());
                player.addToAP(sword.getDamage());
            } else {
                player.setWeapon(sword);
            }
            System.out.printf("%d attack power added%n", _attackPowerToAdd);
            player.addToAP(_attackPowerToAdd);
            mediator.notify(this);
        }
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

    @Override
    public String getType() {
        return ItemType.SWORD;
    }

    @Override
    public void act() {
        if(_attackPowerToAdd > 1) {
            _attackPowerToAdd--;
            System.out.println("Attack power lowered");
        }
    }
}
