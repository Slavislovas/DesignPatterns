package dod.game.items.lantern;

import dod.game.items.GameItem;

/**
 * A class to represent a lantern, which the player holds to allow the player to
 * see farther.
 */
public abstract class Lantern extends GameItem {
    @Override
    public char toChar() {
        return 'L';
    }
}
