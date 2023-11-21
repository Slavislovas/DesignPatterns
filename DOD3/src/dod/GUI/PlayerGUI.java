package dod.GUI;

import java.awt.GridBagConstraints;
import java.io.Serial;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import dod.Communicator.GameCommunicator;
import dod.chainOfResponsibility.*;
import dod.facadePattern.*;
import dod.game.Location;
import lombok.Getter;
import lombok.Setter;

/**
 * A GUI for a generic player is having functionality that provides a message feed and a game board
 *
 * @author Benjamin
 */
public abstract class PlayerGUI extends MessageFeedGUI {
    @Serial
    private static final long serialVersionUID = 4902461275568435021L;
    private final TextField chatField; //The Text field for talking
    protected Panel gameBoard;
    protected GameCommunicator gameCommunicator; //The communicator for communication to the game
    private String[] lookReply; //Stores the look reply

    @Getter
    @Setter
    private int currentGold;
    private final String name; //The players name

    @Getter
    @Setter
    private boolean hasArmour;
    private final boolean isFinalWindow; //Used to determine how object should die

    @Getter
    private final Label currentGoldLabel;

    @Getter
    private final Label goalLabel;

    //Label to tell the user if they have the sword
    @Getter
    private final Label swordLabel;

    protected MessageHandler nextHandler;

    /**
     * The constructor that sets up the communication and the GUI components
     *
     * @param gameCommunicator The communicator to the game
     * @param name             The player name
     * @param isFinalWindow    indicates if this is the last window or not
     */
    public PlayerGUI(GameCommunicator gameCommunicator, String name, boolean isFinalWindow) {
        super();
        //Sets default values
        this.chatField = uiElementFacade.CreateTextField(null, 29); // set length
        this.gameCommunicator = gameCommunicator;
        //adds itself to gameCommunicator for feedback ability
        //Infinitely deep recursion is solved in two different ways
        //Using threads in the Bot GUI
        //Using the nature of event driven commands in swing in the human GUI
        this.gameCommunicator.addListener(this);
        this.setTitle("Dungeon of Doom");
        this.gameBoard = uiElementFacade.CreatePanel();
        this.currentGold = 0;
        this.name = name;
        this.hasArmour = false;
        this.isFinalWindow = isFinalWindow;

        //Stats labels are set up
        this.currentGoldLabel = uiElementFacade.CreateLabel("GOLD " + currentGold);
        this.goalLabel = uiElementFacade.CreateLabel(null);
        //Uses sword indicator image but starts out invisible
        this.swordLabel = getImageLabel("SwordIndicator.png");
        this.swordLabel.setLabelVisible(false);

        //Grid bag layout is used for game-board
        gameBoard.setPanelLayout(LayoutTypes.GridBag);
        initializeHandlers();
    }

    private void initializeHandlers() {
        DieHandler dieHandler = new DieHandler();
        GoalHandler goalHandler = new GoalHandler();
        ArmorHandler armorHandler = new ArmorHandler();
        SwordHandler swordHandler = new SwordHandler();
        GoldHandler goldHandler = new GoldHandler();

        dieHandler.setNextHandler(goalHandler);
        goalHandler.setNextHandler(armorHandler);
        armorHandler.setNextHandler(swordHandler);
        swordHandler.setNextHandler(goldHandler);

        goldHandler.setNextHandler(null);

        nextHandler = dieHandler;
    }

    /**
     * Says hello to the server.
     */
    protected void sayHello() {
        gameCommunicator.sendMessageToGame("LOOK");
        gameCommunicator.sendMessageToGame("HELLO " + name);
    }

    /**
     * Updates the game board to reflect the stored look reply
     */
    private void updateGameBoard() {
        //Removes all components from the game Board
        this.gameBoard.removeAllElements();
        GridBagConstraints gbc = new GridBagConstraints();
        //Gets the player location
        Location playerLocation = getPlayerLocation();

        int gridX, gridY;
        //Loop through every character in the look reply
        for (int y = 1; y < lookReply.length - 1; y++) {
            gridY = y;
            char[] row = lookReply[y].toCharArray(); //String is taken to a char array
            for (int x = 0; x < row.length; x++) {
                gridX = x;
                Label tile;
                assert playerLocation != null;
                if ((x == playerLocation.getCol()) && (y == playerLocation.getRow())) {
                    //Puts a player in the centre
                    tile = getMatchingLabel(getPlayerCharacter(row[x]));
                } else {
                    //If there is no player than get the picture matching the character
                    tile = getMatchingLabel(row[x]);
                }
                //tile is added
                gameBoard.addLabel(tile, gridX, gridY);
            }
        }
        //Force an update to visibility
        gameBoard.setPanelVisible(false);
        gameBoard.setPanelVisible(true);
    }

    /**
     * Gets the player location assuming it's in the middle of the look reply
     *
     * @return Location The location of the player
     */
    private Location getPlayerLocation() {
        if (lookReply.length == 0) {
            return null;
        } else {
            int row = (int) Math.ceil((lookReply.length - 2) / 2.0);
            int col = (int) (Math.ceil(lookReply[1].length() / 2.0)) - 1;
            return new Location(col, row);
        }
    }

    /**
     * Gets the player character
     *
     * @param floorTile The tile character the player is standing on
     * @return char the player character
     */
    private char getPlayerCharacter(char floorTile) {
        //A character depending on the state of the player
        if (floorTile == 'E') {
            if (hasArmour) {
                return 'K';
            } else {
                return 'Q';
            }
        } else {
            if (hasArmour) {
                return 'R';
            } else {
                return 'P';
            }
        }
    }

    /**
     * Gets the messenger Panel
     *
     * @return Panel the messenger Panel
     */
    protected Panel getMessenger() {
        //Panel using the grid bag layout
        var messenger = uiElementFacade.CreatePanel();
        messenger.setPanelLayout(LayoutTypes.GridBag);

        //Adds the message feed from the superclass
        messenger.addPanel(getMessageFeed(), 0, 0, 2);

        //Adds the text field chat field
        messenger.addTextField(chatField, 0, 1, 1);

        //Creation of a send button
        var sendButton = uiElementFacade.CreateButton("Send", null, actionListener -> {
            //Sends the message and wipes the text field
            sendChatMessage(chatField.toString());
            chatField.setTextFieldText("");
        });

        messenger.addButton(sendButton, 1, 1, 1);

        return messenger;
    }

    /**
     * Gets a quit button with built-in functionality
     *
     * @return Button The Quit Button
     */
    protected Button getQuitButton() {
        //uses a set Image
        var quitButton = getImageButton("Quit.png");
        quitButton.setButtonToolTipText("Quit Game");
        quitButton.addButtonActionListener(actionListener -> {
            // Sends a die message and exits
            die("DIE You Quit");
        });
        return quitButton;
    }

    /**
     * Gets a button with an image using a given file name
     *
     * @param imageFileName String the Image File Name
     * @return Button a button with the associated file name
     */
    protected Button getImageButton(String imageFileName) {
        var button = uiElementFacade.CreateButton(null, null, null);
        setImageButton(imageFileName, button);
        //Standardised format to make it visually appealing
        button.setButtonContentAreaFilled(false);
        button.setButtonBorderPainted(false);
        return button;
    }

    /**
     * Given a button this function will modify it by changing its image to that of a given file name
     *
     * @param imageFileName String The file name of the new image
     * @param button        Button The button to receive the new image
     */
    protected void setImageButton(String imageFileName, Button button) {
        //Image is taken from file
        ImageIcon imageIcon = new ImageIcon(getClass().getResource(imageFileName), "");
        button.setButtonIcon(imageIcon);
    }

    /**
     * Gets a Label with an image using a given file name
     *
     * @param imageFileName String the Image File Name
     * @return Label a label with the associated file name
     */
    protected Label getImageLabel(String imageFileName) {
        var label = uiElementFacade.CreateLabel(null);
        //Image is taken from file
        ImageIcon imageIcon = new ImageIcon(getClass().getResource(imageFileName), "");
        label.setLabelIcon(imageIcon);
        return label;
    }

    //Interprets the message from the game
    @Override
    public void pushMessage(String message) {
        if (message.startsWith(("LOOKREPLY"))) {
            lookReply = message.split(System.getProperty("line.separator"));
            //The game board is updated
            updateGameBoard();
            //Class allows subclasses to handle the look reply also
            handelLookReply(this.lookReply);
        } else {
            nextHandler.handle(message, this);

            //Allows subclasses to read the message if it's needed
            handelMessage(message);
            //Checks to see if the message is a server of player message and formats it correctly
            if (!message.startsWith("[")) {
                addMessageToFeed("Server: " + message);
            } else {
                addMessageToFeed(message);
            }
        }
    }

    /**
     * Called whenever a message is received it is designed to be optionally overridden
     * By default is does nothing
     *
     * @param message String The message that was received
     */
    protected void handelMessage(String message) {}

    /**
     * Called whenever a look reply is received it is designed to be optionally overridden
     * By default is does nothing
     *
     * @param lookReply String[] The look reply that was received
     */
    protected void handelLookReply(String[] lookReply) { }

    /**
     * Reads of the gold change as a string and returns it as an integer
     *
     * @param treasureMod The string for which contains the gold change
     * @return int The parsed string, if the string was not a number 0 is returned
     */
    public int getGoldChange(String treasureMod) {
        try {
            return Integer.parseInt(treasureMod);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Displays a message and kills the window. If it is the final window it also exits the program.
     *
     * @param message String The message to be displayed
     */
    public void die(String message) {
        JOptionPane.showMessageDialog(null, message.substring(3));
        //Window is removed
        this.dispose();
        if (this.isFinalWindow) {
            //Exits the program
            System.exit(42);
        }
    }

    /**
     * Given a character a Label with the matching image is returned
     *
     * @param Tile char The Tile Character
     * @return Label with an image of the matching character, if the character is not recognised a black space is seen instead
     */
    private Label getMatchingLabel(char Tile) {
        String fileName;
        //Case statement for all file names
        switch (Tile) {
            case '.': {
                fileName = "Floor";
                break;
            }
            case 'G': {
                fileName = "Gold";
                break;
            }
            case 'E': {
                fileName = "Exit";
                break;
            }
            case '#': {
                fileName = "Wall";
                break;
            }
            case 'A': {
                fileName = "Armour";
                break;
            }
            case 'S': {
                fileName = "Sword";
                break;
            }
            case 'H': {
                fileName = "Health";
                break;
            }
            case 'P': {
                fileName = "Player";
                break;
            }
            case 'Q': {
                fileName = "ExitPlayer";
                break;
            }
            case 'R': {
                fileName = "aPlayer";
                break;
            }
            case 'K': {
                fileName = "aExitPlayer";
                break;
            }
            case 'L': {
                fileName = "Lantern";
                break;
            }
            //Has a default one for X and any extra tiles that does not exist yet
            default:
                fileName = "BlackSpace";
        }
        //File Extension is added and the Label is formed
        return getImageLabel(fileName + ".png");
    }

    /**
     * Sends a chat message to the game
     *
     * @param message String the message to be sent
     */
    private void sendChatMessage(String message) {
        //It will not send an empty message - only contains spaces
        if (!message.replace(" ", "").isEmpty()) {
            gameCommunicator.sendMessageToGame("SHOUT " + message);
        }
    }
}