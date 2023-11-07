package dod.BotLogic;

import dod.Communicator.GameCommunicator;
import dod.Communicator.LocalGameCommunicator;
import dod.GUI.BotPlayerGUI;
import dod.command.Command;
import dod.command.EndTurnCommand;
import dod.command.MoveCommand;
import dod.command.PickUpCommand;
import dod.game.Location;
import dod.strategy.BotStrategy;
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
        setBotStrategy(this);
        System.out.println("Creating Bot with Random Strategy");
    }

    private RandomBotStrategy(GameCommunicator comm, RandomBotStrategy target) {
        super(comm, target);
        if (target != null) {
            this.command = target.command;
        }
    }

    @Override
    public Bot Clone(GameCommunicator comm) {
        return new RandomBotStrategy(comm, this);
    }

    @Override
    public void performAction() {
        Location playerLocation = getPlayerLocation();
        char tile = getTile(playerLocation);

        if(changeBotStrategy())
            getBotStrategy().performAction();

        //If the user is standing on gold and it needs it then it wants to pick it up
        if ((tile == 'G') && (!hasRequiredGold())) {
            if (goldLeftToCollect() <= 2) {
                var gameLogic = getComm().GetGameLogic();
                if (gameLogic != null) {
                    var localGameCommunicator = new LocalGameCommunicator(gameLogic);
                    var clone = this.Clone(localGameCommunicator);
                    System.out.println("Bot was copied.");
                    System.out.println("Original bot hash code: " + this.hashCode());
                    System.out.println("Clone bot hash code: " + clone.hashCode());
                    System.out.println("Original bot location hash code: " + this.getPlayerLocation().hashCode());
                    System.out.println("Cloned bot location hash code: " + clone.getPlayerLocation().hashCode());
                    System.out.println("Original bot current gold: " + this.getCurrentGold());
                    System.out.println("Cloned bot current gold: " + clone.getCurrentGold());
                    var originalCurrentGold = this.getCurrentGold();

                    this.setCurrentGold(6);
                    System.out.println("Original bot current gold: " + this.getCurrentGold());
                    System.out.println("Cloned bot current gold: " + clone.getCurrentGold());

                    this.setCurrentGold(originalCurrentGold);

                    new BotPlayerGUI(localGameCommunicator, "Random Bot (Clone)", false, clone);
                }
            }
            command = new PickUpCommand(getComm(), this, 'G');
            this.getCommandInvoker().executeCommand(command);
        } else if ((tile == 'S') && (!hasSword)) {
            command = new PickUpCommand(getComm(), this, 'S');
            this.getCommandInvoker().executeCommand(command);
        }
        //Otherwise we move randomly
        else {
            try {
                //Forms a move command
                command = new MoveCommand(getComm(), this, 'R', null);
                this.getCommandInvoker().executeCommand(command);
            } catch (NullPointerException e) {
                command = new EndTurnCommand(getComm());
                this.getCommandInvoker().executeCommand(command);
            }
        }
    }
}