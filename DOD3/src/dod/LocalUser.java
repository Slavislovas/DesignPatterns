package dod;

import dod.Communicator.LocalGameCommunicator;
import dod.game.GameLogic;

/**
 * Represents a local user and bridges communication between local game communicator and Game Logic
 *
 * @author Benjamin Dring
 */
public final class LocalUser extends User {
    private LocalGameCommunicator localComm; //Uses the object directly for communication

    /**
     * Sets up the user object and adds the player into the game
     *
     * @param game      GameLogic the game logic to be played on
     * @param localComm LocalGameCommunicator the object to relay messages through
     */
    public LocalUser(GameLogic game, LocalGameCommunicator localComm) {
        super(game);
        this.localComm = localComm;
    }

    /**
     * Interprets and sends the command to the Game
     *
     * @param command String the command to be used
     */
    public void sendCommand(String command) {
        processCommand(command);
    }

    @Override
    public void update(String message) {
        localComm.sendMessageFromGame(message);
    }

    @Override
    public String appendTag(String message) {
        message += " LOCAL_USER";
        return message;
    }

    @Override
    public boolean shouldAppendLocalUserTag(){
        return true;
    }
}
