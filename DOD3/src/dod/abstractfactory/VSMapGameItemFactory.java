package dod.abstractfactory;

import dod.game.items.GameItem;
import dod.game.items.armour.HeavyArmour;
import dod.game.items.gold.LargeGold;
import dod.game.items.health.StrongHealth;
import dod.game.items.lantern.StrongLantern;
import dod.game.items.sword.StrongSword;

public class VSMapGameItemFactory extends AbstractFactory {
    @Override
    public GameItem createHealth() {
        System.out.println("AbstractFactory: creating StrongHealth");
        return new StrongHealth();
    }

    @Override
    public GameItem createArmour() {
        System.out.println("AbstractFactory: creating HeavyArmour");
        return new HeavyArmour();
    }

    @Override
    public GameItem createSword() {
        System.out.println("AbstractFactory: creating StrongSword");
        return new StrongSword();
    }

    @Override
    public GameItem createLantern() {
        System.out.println("AbstractFactory: creating StrongLantern");
            return new StrongLantern();
    }

    @Override
    public GameItem createGold() {
        return new LargeGold();
    }
}
