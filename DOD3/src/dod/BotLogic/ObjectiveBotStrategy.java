package dod.BotLogic;

import dod.command.Command;
import dod.command.MoveCommand;
import dod.command.PickUpCommand;
import dod.Communicator.GameCommunicator;
import dod.game.CompassDirection;
import dod.game.Location;

import java.util.ArrayList;

/**
 * Represents an objective Bot which is purely driven to complete the game by gathering gold.
 *
 * @author Benjamin Dring
 */
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
        setBotStrategy(this);
        System.out.println("Creating Bot with Objective Strategy");
    }

    private ObjectiveBotStrategy(GameCommunicator comm, ObjectiveBotStrategy target) {
        super(comm, target);
        if (target != null) {
            this.command = target.command;
        }
    }

    @Override
    public Bot Clone(GameCommunicator comm) { return new ObjectiveBotStrategy(comm, this); }

    @Override
    public void performAction() {
        //gets the player location and tile
        Location playerLocation = getPlayerLocation();
        char tile = getTile(playerLocation);
        //If it's on gold
        if ((tile == 'G') && (!hasRequiredGold())) {
            command = new PickUpCommand(getComm(), this, 'G');
            this.getCommandInvoker().executeCommand(command);
        }
        //If its standing on a lantern and doesn't already have one it picks it up
        else if ((tile == 'L') && (!hasLantern)) {
            command = new PickUpCommand(getComm(), this, 'L');
            this.getCommandInvoker().executeCommand(command);
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
            command = new MoveCommand(getComm(), this, targetTile, targetDirection.get(0));
            this.getCommandInvoker().executeCommand(command);
        }
        //If the target tile is not in sight it looks for a lantern instead
        else if (!hasLantern) {
            ArrayList<CompassDirection> lanternDirection = getShortestPathToTile('L');
            if (lanternDirection != null) {
                command = new MoveCommand(getComm(), this, 'L', lanternDirection.get(0));
                this.getCommandInvoker().executeCommand(command);
            }
        }
        //If all else fails it moves randomly
        command = new MoveCommand(getComm(), this, 'R', null);
        this.getCommandInvoker().executeCommand(command);
    }
}
