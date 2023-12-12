package dod.abstractfactory;

import dod.Adapter.FancyLanternAdapter;
import dod.Adapter.MegaGoldPintAdapter;
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
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

@Getter
@Setter
public class DefaultMapGameItemFactory extends AbstractFactory {

    Random random;

    private final IWeaponDirector weaponDirector = new WeaponDirectorProxy();

    public DefaultMapGameItemFactory() {
        super(new SmallItem());
        random = new Random();
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
        int chance = random.nextInt(100); // Generating a random number between 0 and 99

        if (chance < 1) { // 1% chance to get FancyLanternAdapter
            return new FancyLanternAdapter();
        } else { // 99% chance to get StrongLantern
            return new Lantern(this.itemType, mediator);
        }
    }

    @Override
    public GameItem createGold(IMediator mediator) {
        int chance = random.nextInt(100); // Generating a random number between 0 and 99

        if (chance < 1) { // 1% chance to get MegaGoldPintAdapter
            return new MegaGoldPintAdapter();
        } else { // 99% chance to get MediumGold
            return new Gold(this.itemType, mediator);
        }
    }
}
