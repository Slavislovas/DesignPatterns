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
        return new Health(this.itemType);
    }

    @Override
    public GameItem createArmour() {
        return new Armour(this.itemType);
    }

    @Override
    public GameItem createSword() {
        return new Sword(this.itemType);
    }

    @Override
    public GameItem createLantern() {
            return new Lantern(this.itemType);
    }

    @Override
    public GameItem createGold() {
        return new Gold(this.itemType);
    }
}
