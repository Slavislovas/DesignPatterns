package dod.abstractfactory;

import dod.Adapter.FancyLanternAdapter;
import dod.game.items.GameItem;
import dod.game.items.armour.HeavyArmour;
import dod.game.items.gold.LargeGold;
import dod.game.items.health.StrongHealth;
import dod.game.items.lantern.StrongLantern;
import dod.game.items.sword.StrongSword;

import java.util.Random;

public class VSMapGameItemFactory extends AbstractFactory {
    @Override
    public GameItem createHealth() {
        return new StrongHealth();
    }

    @Override
    public GameItem createArmour() {
        return new HeavyArmour();
    }

    @Override
    public GameItem createSword() {
        return new StrongSword();
    }

    @Override
    public GameItem createLantern() {
        Random random = new Random();
        int chance = random.nextInt(100); // Generating a random number between 0 and 99

        if (chance < 1) { // 1% chance to get FancyLanternAdapter
            return new FancyLanternAdapter();
        } else { // 99% chance to get StrongLantern
            return new StrongLantern();
        }
    }

    @Override
    public GameItem createGold() {
        return new LargeGold();
    }
}
