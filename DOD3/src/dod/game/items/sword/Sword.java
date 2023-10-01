package dod.game.items.sword;

import dod.game.items.GameItem;

/**
 * A class to represent sword. So far this does nothing, but if attacking is
 * implemented, it could increase the attack potential of a player.
 */
public abstract class Sword extends GameItem {
    @Override
    public char toChar() {
        return 'S';
    }
}
