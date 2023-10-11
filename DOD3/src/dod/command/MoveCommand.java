package dod.command;

import dod.BotLogic.Bot;
import dod.Communicator.GameCommunicator;
import dod.game.CompassDirection;

public class MoveCommand implements Command {
    private GameCommunicator comm;
    private Bot bot;
    private CompassDirection direction;
    private char movementObjective;

    public MoveCommand(GameCommunicator comm, Bot bot, char movementObjective, CompassDirection direction) {
        this.comm = comm;
        this.bot = bot;
        this.direction = direction;
        this.movementObjective = movementObjective;
    }

    @Override
    public void execute() {
        if (movementObjective == 'R') {
            moveRandomly();
        } else if (movementObjective == 'G' || movementObjective == 'E' || movementObjective == 'L') {
            moveTowardObjective();
        }
    }

    private void moveRandomly() {
        CompassDirection randomDirection = bot.getRandomNonBlockDirection(bot.getPlayerLocation());
        comm.sendMessageToGame("MOVE " + bot.getDirectionCharacter(randomDirection));
    }

    private void moveTowardObjective() {
        comm.sendMessageToGame("MOVE " + bot.getDirectionCharacter(direction));
    }
}