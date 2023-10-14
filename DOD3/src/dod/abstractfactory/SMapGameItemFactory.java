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

            return new MediumGold();

    }
}
