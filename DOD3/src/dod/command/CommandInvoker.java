package dod.command;

import java.util.ArrayList;

public class CommandInvoker {
    private ArrayList<Command> commandHistory = new ArrayList<>();

    public void executeCommand(Command command) {
        System.out.println("Received command which needs to be executed: ");
        System.out.println(command.toString());
        command.execute();
        commandHistory.add(command);
    }
}