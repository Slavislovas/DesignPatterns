package dod.abstractfactory;

import dod.game.items.GameItem;
import dod.game.items.armour.LightArmour;
import dod.game.items.gold.SmallGold;
import dod.game.items.health.WeakHealth;
import dod.game.items.lantern.WeakLantern;
import dod.game.items.sword.WeakSword;

public class DefaultMapGameItemFactory extends AbstractFactory {
    @Override
    public GameItem createHealth() {
        return new WeakHealth();
    }

    @Override
    public GameItem createArmour() {
        return new LightArmour();
    }

    @Override
    public GameItem createSword() {
        return new WeakSword();
    }

    @Override
    public GameItem createLantern() {
        return new WeakLantern();
    }

    @Override
    public GameItem createGold() {
        return new SmallGold();
    }
}
