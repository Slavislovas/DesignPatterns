package dod.command;

import dod.BotLogic.Bot;
import dod.Communicator.GameCommunicator;

public class PickUpCommand implements Command {
    private GameCommunicator comm;
    private char tileType;
    private Bot bot;

    public PickUpCommand(GameCommunicator comm, Bot bot, char tileType) {
        this.comm = comm;
        this.bot = bot;
        this.tileType = tileType;
    }

    @Override
    public void execute() {
        System.out.println("Executing pick up command invoked by " + bot.getName());
        if (tileType == 'G') {
            bot.pickupGold();
        } else if (tileType == 'L') {
            bot.setHasLantern(true);
        } else if (tileType == 'S') {
            bot.setHasSword(true);
        } else if (tileType == 'A') {
            bot.setHasArmour(true);
        }

        comm.sendMessageToGame("PICKUP");
    }
}
