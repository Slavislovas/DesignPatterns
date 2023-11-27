package dod.state;

import dod.User;
import dod.game.CommandException;

public class GameOverState implements UserState {
    @Override
    public void startTurn(User user, String[] strings) {

    }

    @Override
    public void endTurn(User user) {

    }

    @Override
    public void pickup(User user) throws CommandException {

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
        if (commandString.equals("DIE")) {
            user.getGame().die(user.getUserID());
            user.getGame().lookAll();
        } else {
            throw new CommandException("invalid command");
        }
    }
}
