package dod.state;

import dod.User;
import dod.game.CommandException;

import static dod.User.autoAsignPlayerNumber;
import static dod.User.sanitiseMessage;

public class ConnectedState implements UserState {
    @Override
    public void startTurn(User user, String[] strings) {
    }

    @Override
    public void endTurn(User user) {

    }

    @Override
    public void pickup(User user) {

    }

    @Override
    public void attack(User user, String arg) {

    }

    @Override
    public void move(User user, String arg) {

    }

    @Override
    public void gift(User user, String arg) {

    }

    @Override
    public void processCommand(User user, String commandString, String arg) throws CommandException {
        if (commandString.equals("HELLO")) {
            if (arg == null) {
                throw new CommandException("HELLO needs an argument");
            }

            String name = sanitiseMessage(arg);

            if (name.replace(" ", "").equals("")) {
                name = "Player " + (++autoAsignPlayerNumber);
            }

            user.getGame().clientHello(name, user.getUserID());
            user.update("HELLO " + name);

            if (!user.isGoalSent()) {
                user.setGoalSent(true);
                user.update("GOAL " + user.getGame().getGoal());
            }
            user.setCurrentState(new WaitingForTurnState());
            user.getGame().sendToAll(name + " has joined the game.");
        } else if (commandString.equals("LOOK")) {
            if (arg != null) {
                throw new CommandException("LOOK doesn't take argument");
            }
            user.getGame().lookAll();
        } else {
            throw new CommandException("Invalid command in ConnectedState");
        }
    }
}
