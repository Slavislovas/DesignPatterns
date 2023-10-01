package dod.game;

import dod.observer.GameObserver;

/**
 * An interface implemented by classes which will
 * "listen to the player in the game"
 * <p>
 * This may seem an over the top way of handling printing to the command line,
 * but will be invaluable over a network, especially as there may be multiple
 * clients!
 */
public interface PlayerListener extends GameObserver {

    /**
     * Informs the listener of the beginning of a player's turn
     */
    public void startTurn();

    /**
     * Informs the listener of the end of a player's turn
     */
    public void endTurn();

    /**
     * Informs the listener that the player has won
     */
    public void win();

    /**
     * Informs the listener that the player has had a change in HP
     *
     * @param value the amount by which the hp has changed
     */
    public void hpChange(int value);

    /**
     * Informs the listener that the player has had a change in amount of gold
     * carried
     *
     * @param value the amount of gold gained or lost
     */
    public void treasureChange(int value);

    /**
     * @param hpLoss the amount of health that was lost
     * @author Benjamin Dring
     * Informs the user that they have lost health due to an attack.
     */
    public void damage(int hpLoss);

    /**
     * @author Benjamin Dring
     * Informs the user to perform a look
     */
    public void look();

}
