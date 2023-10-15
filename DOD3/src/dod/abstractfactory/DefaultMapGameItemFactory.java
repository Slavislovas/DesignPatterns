package dod.abstractfactory;

import dod.Adapter.FancyLanternAdapter;
import dod.Adapter.MegaGoldPintAdapter;
import dod.game.items.GameItem;
import dod.game.items.armour.LightArmour;
import dod.game.items.gold.SmallGold;
import dod.game.items.health.WeakHealth;
import dod.game.items.lantern.WeakLantern;
import dod.game.items.sword.WeakSword;

import java.util.Random;

public class DefaultMapGameItemFactory extends AbstractFactory {
    @Override
    public GameItem createHealth() {
        System.out.println("AbstractFactory: creating WeakHealth");
        return new WeakHealth();
    }

    @Override
    public GameItem createArmour() {
        System.out.println("AbstractFactory: creating LightArmour");
        return new LightArmour();
    }

    @Override
    public GameItem createSword() {
        System.out.println("AbstractFactory: creating WeakSword");
        return new WeakSword();
    }

    @Override
    public GameItem createLantern() {
        System.out.println("AbstractFactory: creating WeakLantern");
        Random random = new Random();
        int chance = random.nextInt(100); // Generating a random number between 0 and 99

        if (chance < 1) { // 1% chance to get FancyLanternAdapter
            return new FancyLanternAdapter();
        } else { // 99% chance to get StrongLantern
            return new WeakLantern();
        }
    }

    @Override
    public GameItem createGold() {
        System.out.println("AbstractFactory: creating SmallGold");
        Random random = new Random();
        int chance = random.nextInt(100); // Generating a random number between 0 and 99

        if (chance < 1) { // 1% chance to get MegaGoldPintAdapter
            return new MegaGoldPintAdapter();
        } else { // 99% chance to get MediumGold
            return new SmallGold();
        }
    }
}
