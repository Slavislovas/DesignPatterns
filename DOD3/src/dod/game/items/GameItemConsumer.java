package dod.game.items;

import dod.builder.Weapon;

/**
 * An interface implemented by anything that can "consume an item". This allows
 * the item to act on the player, e.g. giving the player gold, hp and taking
 * away the AP.
 * <p>
 * Future items could do more, e.g. kill the player instantly.
 */
public interface GameItemConsumer {
    void handleRequest(String indicator, int value);
    String getName();
    Weapon getWeapon();
    void setWeapon(Weapon weapon);
}