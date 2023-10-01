package dod.abstractfactory;

import dod.game.items.GameItem;

public abstract class AbstractFactory {
    public abstract GameItem createHealth();
    public abstract GameItem createArmour();
    public abstract GameItem createSword();
    public abstract GameItem createLantern();
    public abstract GameItem createGold();
}
