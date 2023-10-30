package dod.abstractfactory;

import dod.bridgePattern.LargeItem;
import dod.game.items.GameItem;
import dod.game.items.armour.Armour;
import dod.game.items.gold.Gold;
import dod.game.items.health.Health;
import dod.game.items.lantern.Lantern;
import dod.game.items.sword.Sword;

public class VSMapGameItemFactory extends AbstractFactory {

    public VSMapGameItemFactory() {
        super(new LargeItem());
    }

    @Override
    public GameItem createHealth() {
        System.out.println("AbstractFactory: creating StrongHealth");
        return new Health(this.itemType);
    }

    @Override
    public GameItem createArmour() {
        System.out.println("AbstractFactory: creating HeavyArmour");
        return new Armour(this.itemType);
    }

    @Override
    public GameItem createSword() {
        System.out.println("AbstractFactory: creating StrongSword");
        return new Sword(this.itemType);
    }

    @Override
    public GameItem createLantern() {
        System.out.println("AbstractFactory: creating StrongLantern");
            return new Lantern(this.itemType);
    }

    @Override
    public GameItem createGold() {
        return new Gold(this.itemType);
    }
}
