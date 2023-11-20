package dod.command;

import dod.BotLogic.Bot;
import dod.Communicator.GameCommunicator;
import dod.game.CompassDirection;

import java.util.ArrayList;
import java.util.Random;

public class AttackCommand implements Command {
    private GameCommunicator comm;
    private Bot bot;
    private ArrayList<CompassDirection> directions;
    private Random random;

    public AttackCommand(GameCommunicator comm, Bot bot, ArrayList<CompassDirection> directions) {
        this.comm = comm;
        this.bot = bot;
        this.directions = directions;
        this.random = new Random();
    }

    @Override
    public void execute() {
        System.out.println("Executing attack command invoked by " + bot.getName());
        short numberOfNearbyPlayers = (short) directions.size();
        short randomNumber = (short) random.nextInt(numberOfNearbyPlayers);

        comm.sendMessageToGame("ATTACK " + bot.getDirectionCharacter(directions.get(randomNumber)));
    }
}
