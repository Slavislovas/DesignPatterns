package dod.BotLogic;

import java.util.ArrayList;
import java.util.Random;

import dod.Communicator.GameCommunicator;
import dod.command.Command;
import dod.command.PickUpCommand;
import dod.game.CompassDirection;
import dod.game.Location;
import lombok.Getter;
import lombok.Setter;

/**
 * This class represents the Aggressive Bot it will tend to attack players to win.
 *
 * @author Benjamin Dring
 */
@Getter
@Setter
public class AggressiveBotStrategy extends PlayerFindingBot {
    private Command command;

    /**
     * The constructor for the aggressive bot it sets up it's decision making processes and
     * Prepares communication with the game
     *
     * @param comm GameComunicator The communicator to the Game Logic Class
     */
    public AggressiveBotStrategy(GameCommunicator comm) {
        super(comm);
        this.hasSword = false;
    }

    @Override
    public void performAction() {
        //Gets player location and tile
        Location playerLocation = getPlayerLocation();
        char tile = getTile(playerLocation);
        //If it's standing on the sword and doesn't already have it, it then picks it up
        if ((tile == 'S') && (!hasSword)) {
            setCommand(new PickUpCommand(getComm(), this, 'S'));
            command.execute();
        }

        //Gets players around the bot
        ArrayList<CompassDirection> surroundingPlayerDirections = getSurroundingPlayerDirections(playerLocation);
        short numberOfNearbyPlayers = (short) surroundingPlayerDirections.size();

        if (numberOfNearbyPlayers > 0) {
            //If there is a player around then attack one at random
            short randomNumber = (short) (new Random()).nextInt(numberOfNearbyPlayers);
            getComm().sendMessageToGame("ATTACK " + getDirectionCharacter(surroundingPlayerDirections.get(randomNumber)));
        }

        //Otherwise get the shortest path to a player
        ArrayList<CompassDirection> playerPath = getShortestPathToPlayer();

        if (playerPath != null) {
            //If a path is found move in it
            getComm().sendMessageToGame("MOVE " + getDirectionCharacter(playerPath.get(0)));
        }
        //Otherwise if it is standing on a lantern and doesn't already have one pick it up
        if ((tile == 'L') && (!hasLantern)) {
            setCommand(new PickUpCommand(getComm(), this, 'L'));
            command.execute();
        }
        //Otherwise if it is standing on gold and doesn't already have the required gold pick it up
        if ((tile == 'G') && (!hasRequiredGold())) {
            setCommand(new PickUpCommand(getComm(), this, 'G'));
            command.execute();
        }

        //If there is no one to attack act objectively and pick a target tile
        char targetTile;
        if (hasRequiredGold()) {
            targetTile = 'E';
        } else {
            targetTile = 'G';
        }

        ArrayList<CompassDirection> goldPath = getShortestPathToTile(targetTile);
        if (goldPath != null) {
            //If it can get to the target tile move towards it
            getComm().sendMessageToGame("MOVE " + getDirectionCharacter(goldPath.get(0)));
        }
        //Otherwise if it doesn't have the lantern try to find one
        else if (!hasLantern) {
            ArrayList<CompassDirection> lanternPath = getShortestPathToTile('L');
            if (lanternPath != null) {
                getComm().sendMessageToGame("MOVE " + getDirectionCharacter(lanternPath.get(0)));
            }
        }
        //If all else fails then move randomly
        getComm().sendMessageToGame("MOVE " + getDirectionCharacter(getRandomNonBlockDirection(getPlayerLocation())));
    }
}
