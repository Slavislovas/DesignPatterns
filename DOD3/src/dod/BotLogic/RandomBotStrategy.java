package dod.BotLogic;

import dod.Communicator.GameCommunicator;
import dod.command.Command;
import dod.command.EndTurnCommand;
import dod.command.MoveCommand;
import dod.command.PickUpCommand;
import dod.game.Location;
import lombok.Setter;

/**
 * One of the Bot Logic Classes, this bot moves randomly and picks up items if it needs it.
 *
 * @author Benjamin Dring
 */
@Setter
public class RandomBotStrategy extends Bot {
    private Command command;

    /**
     * The constructor for the random bot it sets up it's decision making processes and
     * Prepares communication with the game
     *
     * @param comm GameComunicator The communicator to the Game Logic Class
     */
    public RandomBotStrategy(GameCommunicator comm) {
        super(comm);
    }

    @Override
    public void performAction() {
        Location playerLocation = getPlayerLocation();
        char tile = getTile(playerLocation);

        //If the user is standing on gold and it needs it then it wants to pick it up
        if ((tile == 'G') && (!hasRequiredGold())) {
            setCommand(new PickUpCommand(getComm(), this, 'G'));
            command.execute();
        }

        //Otherwise we move randomly
        else {
            try {
                //Forms a move command
                setCommand(new MoveCommand(getComm(), this, 'R', null));
                command.execute();
            } catch (NullPointerException e) {
                setCommand(new EndTurnCommand(getComm()));
                command.execute();
            }
        }
    }
}