package dod.game.items;

import dod.bridgePattern.IItemType;
import dod.mediator.IMediator;
import dod.mediator.IMediatorComponent;

/**
 * A class to represent the items on the map.
 * <p>
 * For now, items can only act on the player by changing their look distance,
 * but more features could be added in the future, such as increasing attack
 * points or defence against attack.
 */

public abstract class GameItem implements IMediatorComponent {

    protected IMediator mediator;

    protected IItemType itemType;

    public GameItem() { }

    public GameItem(IItemType type, IMediator mediatr) {
        itemType = type;
        mediator = mediatr;
    }

    /**
     * Process the action of picking up an item.
     *
     * @param player The player who picks up the object
     */
    public void processPickUp(GameItemConsumer player) {
        // By default, do nothing
    }

    /**
     * Checks if the item can be "retained" by the player, i.e., the player
     * doesn't consume it instantly (like health) but holds on to it, like
     * sword.
     *
     * @return true if the item is retained
     */
    public abstract boolean isRetainable();

    /**
     * Allows an item to change the distance a player can see.
     *
     * @returns the increase (or decrease) in the player's look distance.
     */
    public int lookDistanceIncrease() {
        // Only retainable items will be able to affect the distances
        assert isRetainable();

        // Return zero by default
        return 0;
    }

    /**
     * Obtains a character representing the item, used by the textual interface.
     *
     * @return a single character
     */
    public abstract char toChar();
}
