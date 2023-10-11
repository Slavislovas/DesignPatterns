package dod.BotLogic;

import dod.command.Command;
import dod.command.MoveCommand;
import dod.command.PickUpCommand;
import dod.Communicator.GameCommunicator;
import dod.game.CompassDirection;
import dod.game.Location;
import lombok.Setter;

import java.util.ArrayList;

/**
 * Represents an objective Bot which is purely driven to complete the game by gathering gold.
 *
 * @author Benjamin Dring
 */
@Setter
public class ObjectiveBotStrategy extends PathFindingBot {
    private Command command;

    /**
     * The constructor for the objective bot it sets up it's decision making processes and
     * Prepares communication with the game
     *
     * @param comm GameComunicator The communicator to the Game Logic Class
     */
    public ObjectiveBotStrategy(GameCommunicator comm) {
        super(comm);
        this.hasLantern = false;
    }

    @Override
    public void performAction() {
        //gets the player location and tile
        Location playerLocation = getPlayerLocation();
        char tile = getTile(playerLocation);
        //If it's on gold
        if ((tile == 'G') && (!hasRequiredGold())) {
            setCommand(new PickUpCommand(getComm(), this, 'G'));
            command.execute();
        }
        //If its standing on a lantern and doesn't already have one it picks it up
        else if ((tile == 'L') && (!hasLantern)) {
            setCommand(new PickUpCommand(getComm(), this, 'L'));
            command.execute();
        }

        char targetTile;
        //Otherwise it works out if it need to find gold or an exit
        if (hasRequiredGold()) {
            targetTile = 'E';
        } else {
            targetTile = 'G';
        }

        //It then gets the shortest path to its target tile
        ArrayList<CompassDirection> targetDirection = getShortestPathToTile(targetTile);
        if (targetDirection != null) {
            setCommand(new MoveCommand(getComm(), this, targetTile, targetDirection.get(0)));
            command.execute();
        }
        //If the target tile is not in sight it looks for a lantern instead
        else if (!hasLantern) {
            ArrayList<CompassDirection> lanternDirection = getShortestPathToTile('L');
            if (lanternDirection != null) {
                setCommand(new MoveCommand(getComm(), this, 'L', lanternDirection.get(0)));
                command.execute();
            }
        }
        //If all else fails it moves randomly
        setCommand(new MoveCommand(getComm(), this, 'R', null));
        command.execute();
    }
}
