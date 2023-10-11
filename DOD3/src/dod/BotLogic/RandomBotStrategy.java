package dod.BotLogic;

import dod.Communicator.GameCommunicator;
import dod.game.Location;

/**
 * One of the Bot Logic Classes, this bot moves randomly and picks up items if it needs it.
 *
 * @author Benjamin Dring
 */
public class RandomBotStrategy extends Bot {

    /**
     * The constructor for the random bot it sets up it's decision making processes and
     * Prepares communication with the game
     *
     * @param comm GameComunicator The communicator to the Game Logic Class
     */
    public RandomBotStrategy(GameCommunicator comm) {
        super(comm);
    }

    @Override
    public void performAction() {
        Location playerLocation = getPlayerLocation();
        char tile = getTile(playerLocation);

        //If the user is standing on gold and it needs it then it wants to pick it up
        if ((tile == 'G') && (!hasRequiredGold())) {
            this.pickupGold();
            getComm().sendMessageToGame("PICKUP");
        }

        //Otherwise we move randomly
        else {
            try {
                //Forms a move command
                getComm().sendMessageToGame("MOVE " + getDirectionCharacter(getRandomNonBlockDirection(playerLocation)));
            } catch (NullPointerException e) {
                //If there are no directions just end the turn
                getComm().sendMessageToGame("ENDTURN");
            }
        }
    }
}