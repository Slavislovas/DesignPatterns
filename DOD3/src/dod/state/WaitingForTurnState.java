package dod.state;

import dod.User;
import dod.game.CommandException;

public class WaitingForTurnState implements UserState {

    @Override
    public void startTurn(User user, String[] strings) {
        user.setCurrentState(new TakingTurnState());
        user.startTurn();
        final String command = String.join(" ", strings);
        user.processCommand(command);
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
    public void processCommandAndArgument(User user, String commandString, String arg) throws CommandException {
        if ((user.getGame().isPlayerTurn(user.getUserID())) && (user.getGame().isGameStarted())) {
            String[] strings = {commandString, arg};
            startTurn(user, strings);
        } else if (commandString.equals("DIE")) {
            user.setCurrentState(new GameOverState());
            String[] strings = {commandString, arg};
            final String command = String.join(" ", strings);
            user.processCommand(command);
        }
        else if (!user.getGame().isGameStarted()) {
            throw new CommandException("Game has not started");
        } else if (!user.getGame().isPlayerTurn(user.getUserID())) {
            throw new CommandException("It is not your turn");
        } else {
            throw new CommandException("invalid command");
        }
    }
}
