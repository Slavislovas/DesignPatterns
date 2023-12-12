package dod.abstractfactory;

import dod.bridgePattern.IItemType;
import dod.game.items.GameItem;
import dod.mediator.IMediator;

public abstract class AbstractFactory {

    protected IItemType itemType;

    public AbstractFactory(IItemType type) {
        itemType = type;
    }

    public abstract GameItem createHealth(IMediator mediator);
    public abstract GameItem createArmour(IMediator mediator);
    public abstract GameItem createSword(IMediator mediator);
    public abstract GameItem createLantern(IMediator mediator);
    public abstract GameItem createGold(IMediator mediator);
}
