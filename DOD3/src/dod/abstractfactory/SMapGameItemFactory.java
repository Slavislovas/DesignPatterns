package dod.abstractfactory;

import dod.bridgePattern.MediumItem;
import dod.bridgePattern.SmallItem;
import dod.game.items.GameItem;
import dod.game.items.armour.Armour;
import dod.game.items.gold.Gold;
import dod.game.items.health.Health;
import dod.game.items.lantern.Lantern;
import dod.game.items.sword.Sword;

public class SMapGameItemFactory extends AbstractFactory {

    public SMapGameItemFactory() {
        super(new MediumItem());
    }

    @Override
    public GameItem createHealth() {
        System.out.println("AbstractFactory: creating MediumHealth");
        return new Health(this.itemType);
    }

    @Override
    public GameItem createArmour() {
        System.out.println("AbstractFactory: creating MediumArmour");
        return new Armour(this.itemType);
    }

    @Override
    public GameItem createSword() {
        System.out.println("AbstractFactory: creating MediumSword");
        return new Sword(this.itemType);
    }

    @Override
    public GameItem createLantern() {
        System.out.println("AbstractFactory: creating MediumLantern");
        return new Lantern(this.itemType);
    }

    @Override
    public GameItem createGold() {
        System.out.println("AbstractFactory: creating MediumGold");
            return new Gold(this.itemType);
    }
}
