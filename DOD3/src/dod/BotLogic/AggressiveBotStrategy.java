package dod.BotLogic;

import java.util.ArrayList;

import dod.Communicator.GameCommunicator;
import dod.command.AttackCommand;
import dod.command.Command;
import dod.command.MoveCommand;
import dod.command.PickUpCommand;
import dod.game.CompassDirection;
import dod.game.Location;

/**
 * This class represents the Aggressive Bot it will tend to attack players to win.
 *
 * @author Benjamin Dring
 */
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
        setBotStrategy(this);
        System.out.println("Creating Bot with Aggressive Strategy");
    }

    public AggressiveBotStrategy(GameCommunicator comm, Bot bot) {
        super(comm);
        System.out.println("Creating Bot with Aggressive Strategy");
        this.lookReply = bot.lookReply;
        this.goal = bot.goal;
        this.currentGold = bot.currentGold;
        this.hasLantern = bot.hasLantern;
        this.hasArmour = bot.hasArmour;
        this.hasSword = bot.hasSword;
        this.setMyTurn(bot.isMyTurn());
        this.setGameOver(bot.isGameOver());
        this.setUpdatedLook(bot.isUpdatedLook());
        this.setPlayerLocation(bot.getPlayerLocation());
        this.setCommandInvoker(bot.getCommandInvoker());
        this.setBotStrategy(bot.getBotStrategy());
        this.settings = bot.settings;
    }

    private AggressiveBotStrategy(GameCommunicator comm, AggressiveBotStrategy target) {
        super(comm, target);
        if (target != null) {
            this.command = target.command;
        }
    }

    @Override
    public Bot Clone(GameCommunicator comm) { return new AggressiveBotStrategy(comm, this); }

    @Override
    public void performAction() {
        //Gets player location and tile
        Location playerLocation = getPlayerLocation();
        char tile = getTile(playerLocation);
        //If it's standing on the sword and doesn't already have it, it then picks it up
        if ((tile == 'S') && (!hasSword)) {
            command = new PickUpCommand(getComm(), this, 'S');
            this.getCommandInvoker().executeCommand(command);
        }

        //Gets players around the bot
        ArrayList<CompassDirection> surroundingPlayerDirections = getSurroundingPlayerDirections(playerLocation);

        if (!surroundingPlayerDirections.isEmpty()) {
            //If there is a player around then attack one at random
            command = new AttackCommand(getComm(), this, surroundingPlayerDirections);
            this.getCommandInvoker().executeCommand(command);
        }

        //Otherwise get the shortest path to a player
        ArrayList<CompassDirection> playerPath = getShortestPathToPlayer();

        if (playerPath != null) {
            //If a path is found move in it
            command = new MoveCommand(getComm(), this, 'P', playerPath.get(0));
            this.getCommandInvoker().executeCommand(command);
        }
        //Otherwise if it is standing on a lantern and doesn't already have one pick it up
        if ((tile == 'L') && (!hasLantern)) {
            command = new PickUpCommand(getComm(), this, 'L');
            this.getCommandInvoker().executeCommand(command);
        }
        //Otherwise if it is standing on gold and doesn't already have the required gold pick it up
        if ((tile == 'G') && (!hasRequiredGold())) {
            command = new PickUpCommand(getComm(), this, 'G');
            this.getCommandInvoker().executeCommand(command);
        }

        //If there is no one to attack act objectively and pick a target tile
        char targetTile;
        if (hasRequiredGold()) {
            targetTile = 'E';
        } else {
            targetTile = 'G';
        }

        ArrayList<CompassDirection> targetDirection = getShortestPathToTile(targetTile);
        if (targetDirection != null) {
            //If it can get to the target tile move towards it
            command = new MoveCommand(getComm(), this, targetTile, targetDirection.get(0));
            this.getCommandInvoker().executeCommand(command);
        }
        //Otherwise if it doesn't have the lantern try to find one
        else if (!hasLantern) {
            ArrayList<CompassDirection> lanternDirection = getShortestPathToTile('L');
            if (lanternDirection != null) {
                command = new MoveCommand(getComm(), this, 'L', lanternDirection.get(0));
                this.getCommandInvoker().executeCommand(command);
            }
        }
        //If all else fails then move randomly
        command = new MoveCommand(getComm(), this, 'R', null);
        this.getCommandInvoker().executeCommand(command);
    }
}