package dod.abstractfactory;

import dod.Adapter.MegaGoldPintAdapter;
import dod.game.items.GameItem;
import dod.game.items.armour.MediumArmour;
import dod.game.items.gold.MediumGold;
import dod.game.items.health.MediumHealth;
import dod.game.items.lantern.MediumLantern;
import dod.game.items.sword.MediumSword;

import java.util.Random;

public class SMapGameItemFactory extends AbstractFactory {
    @Override
    public GameItem createHealth() {
        return new MediumHealth();
    }

    @Override
    public GameItem createArmour() {
        return new MediumArmour();
    }

    @Override
    public GameItem createSword() {
        return new MediumSword();
    }

    @Override
    public GameItem createLantern() {
        return new MediumLantern();
    }

    @Override
    public GameItem createGold() {
        Random random = new Random();
        int chance = random.nextInt(100); // Generating a random number between 0 and 99

        if (chance < 1) { // 1% chance to get MegaGoldPintAdapter
            return new MegaGoldPintAdapter();
        } else { // 99% chance to get MediumGold
            return new MediumGold();
        }
    }
}
