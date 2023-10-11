package dod.command;

import dod.Communicator.GameCommunicator;

public class EndTurnCommand implements Command {
    private GameCommunicator comm;

    public EndTurnCommand(GameCommunicator comm) {
        this.comm = comm;
    }

    @Override
    public void execute() {
        comm.sendMessageToGame("ENDTURN");
    }
}
