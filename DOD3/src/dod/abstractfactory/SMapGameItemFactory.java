package dod.abstractfactory;

import dod.game.items.GameItem;
import dod.game.items.armour.MediumArmour;
import dod.game.items.gold.MediumGold;
import dod.game.items.health.MediumHealth;
import dod.game.items.lantern.MediumLantern;
import dod.game.items.sword.MediumSword;

public class SMapGameItemFactory extends AbstractFactory {
    @Override
    public GameItem createHealth() {
        System.out.println("AbstractFactory: creating MediumHealth");
        return new MediumHealth();
    }

    @Override
    public GameItem createArmour() {
        System.out.println("AbstractFactory: creating MediumArmour");
        return new MediumArmour();
    }

    @Override
    public GameItem createSword() {
        System.out.println("AbstractFactory: creating MediumSword");
        return new MediumSword();
    }

    @Override
    public GameItem createLantern() {
        System.out.println("AbstractFactory: creating MediumLantern");
        return new MediumLantern();
    }

    @Override
    public GameItem createGold() {
        System.out.println("AbstractFactory: creating MediumGold");
            return new MediumGold();
    }
}
