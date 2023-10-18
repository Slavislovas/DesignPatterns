package dod.abstractfactory;

import dod.bridgePattern.IItemType;
import dod.game.items.GameItem;

public abstract class AbstractFactory {

    protected IItemType itemType;

    public AbstractFactory(IItemType type) {
        itemType = type;
    }

    public abstract GameItem createHealth();
    public abstract GameItem createArmour();
    public abstract GameItem createSword();
    public abstract GameItem createLantern();
    public abstract GameItem createGold();
}
