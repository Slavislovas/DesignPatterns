package dod.command;

import dod.Communicator.GameCommunicator;

public class EndTurnCommand implements Command {
    private GameCommunicator comm;

    public EndTurnCommand(GameCommunicator comm) {
        this.comm = comm;
    }

    @Override
    public void execute() {
        System.out.println("Executing end turn command");
        comm.sendMessageToGame("ENDTURN");
    }
}
