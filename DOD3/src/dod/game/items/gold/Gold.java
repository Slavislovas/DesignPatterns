package dod.game.items.gold;

import dod.game.items.GameItem;
import dod.game.items.GameItemConsumer;

/**
 * An item to represent gold
 */
public abstract class Gold extends GameItem {
    @Override
    public char toChar() {
        return 'G';
    }
}
