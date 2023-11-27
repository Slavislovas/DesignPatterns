package dod.state;

import dod.User;
import dod.game.CommandException;

public class TakingTurnState implements UserState {
    @Override
    public void startTurn(User user, String[] strings) {

    }

    @Override
    public void endTurn(User user) {
        user.getGame().newTurn();
        user.setCurrentState(new WaitingForTurnState());
    }

    @Override
    public void pickup(User user) throws CommandException {
        user.getGame().clientPickup();
        user.getGame().lookAll();
        user.outputSuccess();
    }

    @Override
    public void attack(User user, String arg) throws CommandException {
        user.getGame().clientAttack(user.getDirection(arg));
        user.getGame().lookAll();
        user.outputSuccess();
    }

    @Override
    public void move(User user, String arg) throws CommandException {
        user.getGame().clientMove(user.getDirection(arg));
        user.getGame().lookAll();
        user.outputSuccess();
    }

    @Override
    public void gift(User user, String arg) throws CommandException {
        user.getGame().clientGift(user.getDirection(arg));
        user.outputSuccess();
    }

    @Override
    public void processCommand(User user, String commandString, String arg) throws CommandException {
        switch (commandString) {
            case "PICKUP" -> {
                if (arg != null) {
                    throw new CommandException("PICKUP does not take an argument");
                }
                pickup(user);
            }
            case "MOVE" -> {
                if (arg == null) {
                    throw new CommandException("MOVE needs a direction");
                }
                move(user, arg);
            }
            case "ATTACK" -> {
                if (arg == null) {
                    throw new CommandException("ATTACK needs a direction");
                }
                attack(user, arg);
            }
            case "GIFT" -> {
                if (arg == null) {
                    throw new CommandException("GIFT needs a direction");
                }
                gift(user, arg);
            }
            case "ENDTURN" -> endTurn(user);
            default -> throw new CommandException("invalid command");
        }
    }
}
