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

    public AttackCommand(GameCommunicator comm, Bot bot, ArrayList<CompassDirection> directions) {
        this.comm = comm;
        this.bot = bot;
        this.directions = directions;
    }

    @Override
    public void execute() {
        short numberOfNearbyPlayers = (short) directions.size();
        short randomNumber = (short) (new Random()).nextInt(numberOfNearbyPlayers);

        comm.sendMessageToGame("ATTACK " + bot.getDirectionCharacter(directions.get(randomNumber)));
    }
}