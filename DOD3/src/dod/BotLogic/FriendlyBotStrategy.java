package dod.BotLogic;

import java.util.ArrayList;

import dod.Communicator.GameCommunicator;
import dod.command.Command;
import dod.command.GiftCommand;
import dod.command.MoveCommand;
import dod.command.PickUpCommand;
import dod.game.CompassDirection;
import dod.game.Location;

public class FriendlyBotStrategy extends PlayerFindingBot {
    private Command command;

    /**
     * The constructor for the friendly bot it sets up it's decision making processes and
     * Prepares communication with the game
     *
     * @param comm GameComunicator The communicator to the Game Logic Class
     */
    public FriendlyBotStrategy(GameCommunicator comm) {
        super(comm);
        System.out.println("Creating Bot with Friendly Strategy");
    }

    public FriendlyBotStrategy(GameCommunicator comm, Bot bot) {
        super(comm);
        System.out.println("Creating Bot with Friendly Strategy");
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
        this.settings = bot.settings;
    }

    private FriendlyBotStrategy(GameCommunicator comm, FriendlyBotStrategy target) {
        super(comm, target);
        if (target != null) {
            this.command = target.command;
        }
    }

    @Override
    public Bot Clone(GameCommunicator comm) { return new FriendlyBotStrategy(comm, this); }

    @Override
    public void performAction() {
        //Gets the player location and tile
        Location playerLocation = getPlayerLocation();
        char tile = getTile(playerLocation);

        //If it's standing on armour pick it up
        if ((tile == 'A') && (!hasArmour)) {
            command = new PickUpCommand(getComm(), this, 'A');
            this.getCommandInvoker().executeCommand(command);
        }

        //It then tries to give gold away
        //Only give if there is gold to give
        if (this.currentGold > 0) {
            //Gets surrounding players
            ArrayList<CompassDirection> surroundingPlayerDirections = getSurroundingPlayerDirections(playerLocation);

            if (!surroundingPlayerDirections.isEmpty()) {
                //If there is a nearby player then give gold to a player at random
                command = new GiftCommand(getComm(), this, surroundingPlayerDirections);
                this.getCommandInvoker().executeCommand(command);
            }

            //Otherwise it finds the nearest player
            ArrayList<CompassDirection> playerPath = getShortestPathToPlayer();

            if (playerPath != null) {
                //If there is a visible player then the bot moves towards them
                command = new MoveCommand(getComm(), this, 'P', playerPath.get(0));
                this.getCommandInvoker().executeCommand(command);
            }
        }

        //Otherwise it picks up the lantern to maximise its player finding ability
        if ((tile == 'L') && (!hasLantern)) {
            command = new PickUpCommand(getComm(), this, 'L');
            this.getCommandInvoker().executeCommand(command);
        }
        //Otherwise Pickup gold even if we have the right amount so it can give it away
        if ((tile == 'G')) {
            command = new PickUpCommand(getComm(), this, 'G');
            this.getCommandInvoker().executeCommand(command);
        }

        //If there is no one to give gold to or we do not have enough gold act objectively by using a target tile
        char targetTile;
        if (hasRequiredGold()) {
            targetTile = 'E';
        } else {
            targetTile = 'G';
        }

        //Gets the shortest path to the target tile
        ArrayList<CompassDirection> targetDirection = getShortestPathToTile(targetTile);
        if (targetDirection != null) {
            //If we find our path follow it
            command = new MoveCommand(getComm(), this, targetTile, targetDirection.get(0));
            this.getCommandInvoker().executeCommand(command);
        }
        //Otherwise go for the lantern if we do not already have it
        else if (!hasLantern) {
            ArrayList<CompassDirection> lanternDirection = getShortestPathToTile('L');
            if (lanternDirection != null) {
                //If a lantern path is found follow it
                command = new MoveCommand(getComm(), this, 'L', lanternDirection.get(0));
                this.getCommandInvoker().executeCommand(command);
            }
        }
        //If all else fails move randomly
        command = new MoveCommand(getComm(), this, 'R', null);
        this.getCommandInvoker().executeCommand(command);
    }
}