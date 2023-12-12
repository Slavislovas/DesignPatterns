package dod.abstractfactory;

import dod.bridgePattern.MediumItem;
import dod.bridgePattern.SmallItem;
import dod.builder.IWeaponDirector;
import dod.game.items.GameItem;
import dod.game.items.armour.Armour;
import dod.game.items.gold.Gold;
import dod.game.items.health.Health;
import dod.game.items.lantern.Lantern;
import dod.game.items.sword.Sword;
import dod.mediator.IMediator;
import dod.proxy.WeaponDirectorProxy;

public class SMapGameItemFactory extends AbstractFactory {

    private final IWeaponDirector weaponDirector = new WeaponDirectorProxy();

    public SMapGameItemFactory() {
        super(new MediumItem());
    }

    @Override
    public GameItem createHealth(IMediator mediator) {
        return new Health(this.itemType, mediator);
    }

    @Override
    public GameItem createArmour(IMediator mediator) {
        return new Armour(this.itemType, mediator);
    }

    @Override
    public GameItem createSword(IMediator mediator) {
        return new Sword(this.itemType, mediator, weaponDirector);
    }

    @Override
    public GameItem createLantern(IMediator mediator) {
        return new Lantern(this.itemType, mediator);
    }

    @Override
    public GameItem createGold(IMediator mediator) {
            return new Gold(this.itemType, mediator);
    }
}
