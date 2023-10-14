package dod.command;

import dod.BotLogic.Bot;
import dod.Communicator.GameCommunicator;
import dod.game.CompassDirection;

import java.util.ArrayList;
import java.util.Random;

public class GiftCommand implements Command {
    private GameCommunicator comm;
    private Bot bot;
    private ArrayList<CompassDirection> directions;

    public GiftCommand(GameCommunicator comm, Bot bot, ArrayList<CompassDirection> directions) {
        this.comm = comm;
        this.bot = bot;
        this.directions = directions;
    }

    @Override
    public void execute() {
        System.out.println("Executing gift command invoked by " + bot.getName());
        int gold = bot.getCurrentGold() - 1;
        bot.setCurrentGold(gold);

        short randomNumber = (short) (new Random()).nextInt(directions.size());
        comm.sendMessageToGame("GIFT " + bot.getDirectionCharacter(directions.get(randomNumber)));
    }
}