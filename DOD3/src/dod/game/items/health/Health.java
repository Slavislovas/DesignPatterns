package dod.game.items.health;

import dod.bridgePattern.IItemType;
import dod.game.items.GameItem;
import dod.game.items.GameItemConsumer;
import dod.mediator.IMediator;
import dod.game.items.ItemType;

/**
 * A class to represent health "potion", which is consumed immediately and gives
 * the player an extra HP.
 */
public class Health extends GameItem {

    private int _healthToAdd = 1;

    public Health() { }

    public Health(IItemType itemType, IMediator mediator) {
        super(itemType, mediator);
        _healthToAdd = itemType.getValue();
    }


    @Override
    public void processPickUp(GameItemConsumer player) {
        System.out.printf("%d health added%n", _healthToAdd);
        player.handleRequest("Health", _healthToAdd);
        mediator.notify(this);
    }

    @Override
    public boolean isRetainable() {
        return false;
    }

    @Override
    public String toString() {
        return itemType.getName() + " health potion";
    }

    @Override
    public char toChar() {
        return 'H';
    }

    @Override
    public String getType() {
        return ItemType.HEALTH;
    }

    @Override
    public void act() {
        if(_healthToAdd > 1) {
            _healthToAdd--;
            System.out.println("Health to get lowered");
        }
    }
}
