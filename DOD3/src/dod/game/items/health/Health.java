package dod.game.items.health;

import dod.game.items.GameItem;
import dod.game.items.GameItemConsumer;

/**
 * A class to represent health "potion", which is consumed immediately and gives
 * the player an extra HP.
 */
public abstract class Health extends GameItem {
    @Override
    public char toChar() {
        return 'H';
    }
}
