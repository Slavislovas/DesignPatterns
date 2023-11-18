package dod.command;

import java.util.ArrayList;

public class CommandInvoker {
    private ArrayList<Command> commandHistory = new ArrayList<>();

    public void executeCommand(Command command) {
        command.execute();
        commandHistory.add(command);
    }
}