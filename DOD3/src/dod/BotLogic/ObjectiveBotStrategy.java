package dod.BotLogic;

import java.util.ArrayList;

import dod.Communicator.GameCommunicator;
import dod.game.CompassDirection;
import dod.game.Location;

/**
 * Represents an objective Bot which is purely driven to complete the game by gathering gold.
 *
 * @author Benjamin Dring
 */
public class ObjectiveBotStrategy extends PathFindingBot {

    /**
     * The constructor for the objective bot it sets up it's decision making processes and
     * Prepares communication with the game
     *
     * @param comm GameComunicator The communicator to the Game Logic Class
     */
    public ObjectiveBotStrategy(GameCommunicator comm) {
        super(comm);
    }

    @Override
    public void performAction() {
        //gets the player location and tile
        Location playerLocation = getPlayerLocation();
        char tile = getTile(playerLocation);
        //If it's on gold
        if ((tile == 'G') && (!hasRequiredGold())) {
            this.pickupGold();
            getComm().sendMessageToGame("PICKUP");
        }
        //If its standing on a lantern and doesn't already have one it picks it up
        else if ((tile == 'L') && (!hasLantern)) {
            this.hasLantern = true;
            getComm().sendMessageToGame("PICKUP");
        }

        char targetTile;
        //Otherwise it works out if it need to find gold or an exit
        if (hasRequiredGold()) {
            targetTile = 'E';
        } else {
            targetTile = 'G';
        }

        //It then gets the shortest path to its target tile
        ArrayList<CompassDirection> goldPath = getShortestPathToTile(targetTile);
        if (goldPath != null) {
            //If a path has been found it moves the first direction in that path
            getComm().sendMessageToGame("MOVE " + getDirectionCharacter(goldPath.get(0)));
        }
        //If the target tile is not in sight it looks for a lantern instead
        else if (!hasLantern) {
            ArrayList<CompassDirection> lanternPath = getShortestPathToTile('L');
            if (lanternPath != null) {
                //if a lantern path is found then take the first step to it
                getComm().sendMessageToGame("MOVE " + getDirectionCharacter(lanternPath.get(0)));
            }
        }
        //If all else fails it moves randomly
        getComm().sendMessageToGame("MOVE " + getDirectionCharacter(getRandomNonBlockDirection(getPlayerLocation())));
    }
}
