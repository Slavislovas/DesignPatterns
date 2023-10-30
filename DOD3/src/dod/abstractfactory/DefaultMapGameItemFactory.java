package dod.abstractfactory;

import dod.Adapter.FancyLanternAdapter;
import dod.Adapter.MegaGoldPintAdapter;
import dod.bridgePattern.SmallItem;
import dod.game.items.GameItem;
import dod.game.items.armour.Armour;
import dod.game.items.gold.Gold;
import dod.game.items.health.Health;
import dod.game.items.lantern.Lantern;
import dod.game.items.sword.Sword;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

@Getter
@Setter
public class DefaultMapGameItemFactory extends AbstractFactory {

    Random random;

    public DefaultMapGameItemFactory() {
        super(new SmallItem());
        random = new Random();
    }

    @Override
    public GameItem createHealth() {
        System.out.println("AbstractFactory: creating WeakHealth");
        return new Health(this.itemType);
    }

    @Override
    public GameItem createArmour() {
        System.out.println("AbstractFactory: creating LightArmour");
        return new Armour(this.itemType);
    }

    @Override
    public GameItem createSword() {
        System.out.println("AbstractFactory: creating WeakSword");
        return new Sword(this.itemType);
    }

    @Override
    public GameItem createLantern() {
        System.out.println("AbstractFactory: creating WeakLantern");
        int chance = random.nextInt(100); // Generating a random number between 0 and 99

        if (chance < 1) { // 1% chance to get FancyLanternAdapter
            return new FancyLanternAdapter();
        } else { // 99% chance to get StrongLantern
            return new Lantern(this.itemType);
        }
    }

    @Override
    public GameItem createGold() {
        System.out.println("AbstractFactory: creating SmallGold");
        int chance = random.nextInt(100); // Generating a random number between 0 and 99

        if (chance < 1) { // 1% chance to get MegaGoldPintAdapter
            return new MegaGoldPintAdapter();
        } else { // 99% chance to get MediumGold
            return new Gold(this.itemType);
        }
    }
}
