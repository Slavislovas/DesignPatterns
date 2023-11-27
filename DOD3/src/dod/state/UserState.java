package dod.state;

import dod.User;
import dod.game.CommandException;

public interface UserState {
    void startTurn(User user, String[] strings);
    void endTurn(User user);
    void pickup(User user) throws CommandException;
    void attack(User user, String arg) throws CommandException;
    void move(User user, String arg) throws CommandException;
    void gift(User user, String arg) throws CommandException;
    void processCommandAndArgument(User user, String commandString, String arg) throws CommandException;
}
