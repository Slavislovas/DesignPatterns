package dod;

import dod.game.CommandException;
import dod.game.CompassDirection;
import dod.game.GameLogic;
import dod.game.PlayerListener;
import dod.state.ConnectedState;
import dod.state.UserState;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

/**
 * Represents a generic user and has methods that is common for both local and network users.
 * Some methods are common but implemented in different ways and so are abstract.
 * Much of this code has been adjusted to be compatible with client and servers.
 *
 * @author Benjamin Dring
 */

@Getter
public abstract class User implements PlayerListener {
    public static int autoAsignPlayerNumber = 0; //for when a name is not supplied
    // The game which the command line user will operate on.
    // This is protected to enforce the use of "processCommand".
    protected final GameLogic game;
    //Used to identify users from each other
    protected int userID;
    @Setter
    private UserState currentState;
    private boolean didUserWin; //indicates if a user has won
    @Getter
    @Setter
    private boolean goalSent; //indicates if a goal message has been sent

    /**
     * Sets up the user and adds their character to the game
     *
     * @param game GameLogic the game for the user to interact with
     */
    public User(GameLogic game) {
        this.game = game;

        // Ensures that the instance will listen to the player in the
        // game for messages from the game
        userID = game.addPlayer(this);
        this.didUserWin = false;
        this.goalSent = false;
        this.currentState = new ConnectedState();
    }

    /**
     * Sanitises the given message - there are some characters that we can put
     * in the messages that we don't want in other stuff that we sanitise.
     *
     * @param s The message to be sanitised
     * @return The sanitised message
     */
    public static String sanitiseMessage(String s) {
        return sanitise(s, "[a-zA-Z0-9-_ \\.,:!\\(\\)#]");
    }

    /**
     * Strip out anything that isn't in the specified regex.
     *
     * @param s     The string to be sanitised
     * @param regex The regex to use for sanitisiation
     * @return The sanitised string
     */
    private static String sanitise(String s, String regex) {
        String rv = "";

        for (int i = 0; i < s.length(); i++) {
            final String tmp = s.substring(i, i + 1);

            if (tmp.matches(regex)) {
                rv += tmp;
            }
        }

        return rv;
    }

    /**
     * Informs the user of the beginning of a player's turn
     */
    @Override
    public void startTurn() {
        update("STARTTURN");
    }

    /**
     * Informs the user of the end of a player's turn
     */
    @Override
    public void endTurn() {
        update("ENDTURN");
    }

    /**
     * Informs the user that the player has won
     */
    @Override
    public void win() {
        update("DIE You Won!");
        this.didUserWin = true;
    }

    /**
     * Informs the user that the player's hit points have changed
     */
    @Override
    public void hpChange(int value) {
        update("HP INCREASED BY " + value);
    }

    /**
     * A new method that is called when the user is hit by a player.
     */
    @Override
    public void damage(int value) {
        update("You were hit and lost " + value + " hp.");
    }

    /**
     * Informs the user that the player's gold count has changed
     *
     * @author Benjamin Dring
     */
    @Override
    public void treasureChange(int value) {
        update("TREASUREMOD " + value);
    }

    /**
     * Sends a look reply
     */
    @Override
    public void look() {
        update("LOOKREPLY" + System.getProperty("line.separator")
                + this.game.clientLook(this.userID) + "ENDLOOKREPLY");
    }

    /**
     * Processes a text command from the user.
     *
     * @param commandString the string containing the command and any argument
     */
    public final void processCommand(String commandString) {
        commandString = commandString.toUpperCase();

        final String commandStringSplit[] = commandString.split(" ", 2);
        final String command = commandStringSplit[0];
        final String arg = ((commandStringSplit.length == 2) ? commandStringSplit[1]
                : null);

        if (command.equals("SHOUT")) {
            writeToChat(command, arg);
        } else {
            try {
                currentState.processCommandAndArgument(this, command, arg);
            } catch (final CommandException e) {
                update("FAIL " + e.getMessage());
            }
        }
    }

    private void writeToChat(String command, String arg) {
        if (command.equals("SHOUT")) {
            if (arg == null) {
                return;
            }
            this.game.clientShout(sanitiseMessage(arg), this.userID);
        }
    }

    /**
     * Obtains a compass direction from a string. Used to ensure the correct
     * exception type is thrown, and for consistency between MOVE and ATTACK.
     *
     * @param string the direction string
     * @return the compass direction
     * @throws CommandException
     */
    public CompassDirection getDirection(String string)
            throws CommandException {
        try {
            return CompassDirection.fromString(string);
        } catch (final IllegalArgumentException e) {
            throw new CommandException("invalid direction");
        }
    }

    /**
     * Sends a success message in the event that a command has succeeded
     */
    public void outputSuccess() {
        //I chose to remove this
        //outputMessage("SUCCESS");
    }

    /**
     * Works out if the game is over if it is then messages are sent informing the user so
     *
     * @return boolean Indicates if the game is over
     */
    protected boolean isGameOver() {
        if (game.isGameOver()) {
            if (!didUserWin) {
                update("DIE You Lost");
            }
            update("DIE You Won!");
            return true;
        }
        return false;
    }

}
