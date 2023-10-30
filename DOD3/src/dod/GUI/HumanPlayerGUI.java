package dod.GUI;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.Serial;

import dod.Communicator.GameCommunicator;
import dod.facadePattern.Button;
import dod.facadePattern.LayoutTypes;
import dod.facadePattern.Panel;
import lombok.Getter;

/**
 * Represents the Dungeon of Doom 3 GUI for the Human Player.
 * The displayGUI() method will completely display the GUI with all of its functionality
 *
 * @author Benjamin Dring
 */
@Getter
public class HumanPlayerGUI extends PlayerGUI {
    @Serial
    private static final long serialVersionUID = -9146521400683926566L;

    private Action selectedAction; //Enum for use with controller

    /**
     * The constructor of the class that set up the Human GUI ready for display
     *
     * @param gameCommunicator GameCommunicator The communicator object for the GUI to communicate to the Game
     * @param name             String The user's name to be used in game
     * @param isFinalWindow    Boolean indicating whether this is the last window or not
     */
    public HumanPlayerGUI(GameCommunicator gameCommunicator, String name, boolean isFinalWindow) {
        super(gameCommunicator, name, isFinalWindow);
        this.selectedAction = Action.Move; //defaults to move
        sayHello();
    }

    /**
     * Displays the Human Player GUI
     */
    @Override
    public void displayGUI() {
        canvas.setCanvasLayout(LayoutTypes.Flow); //Uses flow layout
        //Adds individual components

        canvas.addPanel(gameBoard);

        canvas.addPanel(getController());

        canvas.addPanel(getMessenger());

        //Size is set and components are shown
        this.setSize(1800, 750);
        this.setVisible(true);
    }

    /**
     * Gets the controller JPanel including all the buttons functionality
     *
     * @return JPanel The controller ready to be inserted into a container
     */
    private Panel getController() {
        var controller = uiElementFacade.CreatePanel();
        controller.setPanelLayout(LayoutTypes.GridBag); //Uses grid bag layout
        //Padding is added
        var fill = GridBagConstraints.NONE;
        var insets = new Insets(5, 5, 5, 5);

        //Controller components are then added

        controller.addPanel(getDPad(), 0, 0, fill, insets);

        controller.addPanel(getActionKeys(), 1, 0, fill, insets);

        //Controller is returned
        return controller;
    }

    /**
     * Gets the D Pad used in movement and control it also contains the statistic labels
     *
     * @return JPanel the D Pad ready to be inserted into a container
     */
    private Panel getDPad() {
        var dpad = uiElementFacade.CreatePanel();
        dpad.setPanelLayout(LayoutTypes.GridBag); //Uses grid bag layout

        //Buttons are created using Images
        //Buttons are then added to JPanel
        var north = getImageButton("North.png");
        dpad.addButton(north, 1, 0);

        var west = getImageButton("West.png");
        dpad.addButton(west, 0, 1);

        var pickup = getImageButton("PickUp.png");
        dpad.addButton(pickup, 1, 1);

        var east = getImageButton("East.png");
        dpad.addButton(east, 2, 1);

        //Goal Label is added
        dpad.addLabel(getGoalLabel(), 2, 2);

        var south = getImageButton("South.png");
        dpad.addButton(south, 1, 2);

        //Current gold label is added
        dpad.addLabel(getCurrentGoldLabel(), 0, 2);

        dpad.addLabel(getSwordLabel(), 0, 3);

        dpad.addButton(getQuitButton(), 1, 3);

        //Buttons functions are made using different directional characters
        //getActionString() is used to determine the currently selected action
        //String is then sent as a command to the game
        north.addButtonActionListener(actionListener -> gameCommunicator.sendMessageToGame(getActionString() + " N"));

        south.addButtonActionListener(actionListener -> gameCommunicator.sendMessageToGame(getActionString() + " S"));

        east.addButtonActionListener(actionListener -> gameCommunicator.sendMessageToGame(getActionString() + " E"));

        west.addButtonActionListener(actionListener -> gameCommunicator.sendMessageToGame(getActionString() + " W"));

        //Simple pickup command
        pickup.addButtonActionListener(actionListener -> gameCommunicator.sendMessageToGame("PICKUP"));

        //DPad is returned
        return dpad;
    }

    /**
     * Gets the action keys which allows the user to specify their desired action
     *
     * @return JPanel The action keys JPanel ready to be inserted into the Container
     */
    private Panel getActionKeys() {
        var actionKeys = uiElementFacade.CreatePanel();
        actionKeys.setPanelLayout(LayoutTypes.Grid, 4, 0); //Uses grid layout of a single column with 4 rows

        //Buttons are made using images
        //sFilename indicates selected version
        final var move = getImageButton("sMove.png"); //Move is selected by default
        final var attack = getImageButton("Attack.png");
        final var gift = getImageButton("Gift.png");

        //Button functions are added
        move.addButtonActionListener(actionListener -> {
            //Selected action is changed
            selectedAction = Action.Move;
            setImageButton("sMove.png", move); //Move is selected
            setImageButton("Attack.png", attack);
            setImageButton("Gift.png", gift);
        });

        attack.addButtonActionListener(actionListener -> {
            //Selected action is changed
            selectedAction = Action.Attack;
            setImageButton("Move.png", move);
            setImageButton("sAttack.png", attack); //Attack is selected
            setImageButton("Gift.png", gift);
        });

        gift.addButtonActionListener(actionListener -> {
            //Selected action is changed
            selectedAction = Action.Gift;
            setImageButton("Move.png", move);
            setImageButton("Attack.png", attack);
            setImageButton("sGift.png", gift); //Gift is selected
        });

        //Keys are added

        actionKeys.addButton(move);
        actionKeys.addButton(attack);
        actionKeys.addButton(gift);
        actionKeys.addButton(getEndTurnButton());

        //JPanel is returned
        return actionKeys;
    }

    /**
     * Gets the end turn button for ending a players turn
     *
     * @return Button The end turn button
     */
    private Button getEndTurnButton() {
        //Creates a button using the image
        var endTurnButton = getImageButton("EndTurn.png");
        endTurnButton.setButtonToolTipText("End Turn");
        //Adds the function
        endTurnButton.addButtonActionListener(actionListener -> {
            //Sends an ENDTURN Command
            gameCommunicator.sendMessageToGame("ENDTURN");
        });
        //Button is returned
        return endTurnButton;
    }

    /**
     * An Enum which represents the three types of actions
     *
     * @author Benjamin Dring
     */
    private enum Action {
        Move, Attack, Gift
    }

    /**
     * Translates the selectedAction attribute into a String that can be sent as a command to the Game
     *
     * @return String the string format for selectedAction to be sent to the game
     */
    private String getActionString() {
        //Uses simple switch statement
        //returns make breaks unneeded
        return switch (selectedAction) {
            case Move -> "MOVE";
            case Attack -> "ATTACK";
            default -> "GIFT"; //Defaults to gift
        };
    }


}
