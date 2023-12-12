package dod.game.items.gold;

import dod.bridgePattern.IItemType;
import dod.game.items.GameItem;
import dod.game.items.GameItemConsumer;
import dod.mediator.IMediator;
import dod.game.items.ItemType;

/**
 * An item to represent gold
 */
public class Gold extends GameItem {

    private int _goldToAdd = 1;

    public Gold() { }

    public Gold(IItemType itemType, IMediator mediator) {
        super(itemType, mediator);
        _goldToAdd += itemType.getValue();
    }

    @Override
    public void processPickUp(GameItemConsumer player) {
        System.out.printf("%d gold added%n", _goldToAdd);
        player.addGold(_goldToAdd);
        mediator.notify(this);
    }

    @Override
    public boolean isRetainable() {
        return false;
    }

    @Override
    public String toString() { return itemType.getName() + " gold pile"; }

    @Override
    public char toChar() {
        return 'G';
    }

    @Override
    public String getType() { return ItemType.GOLD; }

    @Override
    public void act() {
        if(_goldToAdd > 1) {
            _goldToAdd--;
            System.out.println("Gold to get lowered");
        }
    }
}
