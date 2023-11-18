package dod.GUI;

import dod.BotLogic.*;
import dod.Communicator.GameCommunicator;
import dod.Communicator.LocalGameCommunicator;
import dod.Communicator.NetworkGameCommunicator;
import dod.Server;
import dod.facadePattern.*;
import dod.facadePattern.Canvas;
import dod.facadePattern.TextField;
import dod.game.GameLogic;
import dod.singleton.Settings;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serial;
import java.net.UnknownHostException;
import java.text.ParseException;


/**
 * A GUI class that displays the Selection Menus that allows the user to set up their desired session of Dungeon of Doom 3
 *
 * @author Benjamin Dring
 */
public class MainMenu extends JFrame {

    @Serial
    private static final long serialVersionUID = 2304527111075624043L;

    private final IUiElementFacade uiElementFacade;

    private final Canvas canvas;

    private final Settings settings = Settings.getInstance();
    /**
     * Constructor of the class that sets up the JFrame Container
     */
    public MainMenu() {
        uiElementFacade = new UiElementFacade();
        canvas = uiElementFacade.CreateCanvas(getContentPane());
        canvas.setCanvasLayout(LayoutTypes.GridBag); //All menus use the grid bag layout
        canvas.setCanvasBackground(Color.BLACK); //Starts off with a black background
    }

    /**
     * Displays the Title Menu and gives all buttons the required functionality
     */
    public void displayTitleMenu() {
        this.setSize(1000, 300); //first menu is larger than the others
        canvas.removeAllElements(); //removes any previous components
        this.setTitle("DUNGEON OF DOOM");

        var fill = GridBagConstraints.NONE;
        var insets = new Insets(5, 5, 5, 5);

        //The two buttons are stored in a Grid Layout Panel
        var startButtons = uiElementFacade.CreatePanel();
        startButtons.setPanelLayout(LayoutTypes.Grid, 1, 2);

        //Create and Add multiplayer button
        var networkButton = uiElementFacade.CreateButton("Multiplayer", "Play over a network", actionListener -> displayNetworkGameMenu());
        startButtons.addButton(networkButton, 0, 1, fill, insets);

        //Create and Add Single button
        var localButton = uiElementFacade.CreateButton("Single Player", "Play locally", actionListener -> displayLocalSettingsMenu());
        startButtons.addButton(localButton, 1, 1, fill, insets);


        //Add the buttons panel to the container
        canvas.addPanel(startButtons, 0, 1, fill, insets);

        //Creates and adds the title picture Label to the container
        var titlePicture = uiElementFacade.CreateLabel(null);
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("Title.png"), "");
        titlePicture.setLabelIcon(imageIcon);
        canvas.addLabel(titlePicture, 0, 0, 1, fill, insets);

        //Update container
        updateMenu();
    }

    /**
     * Displays the Menu for single player mode
     */
    private void displayLocalSettingsMenu() {
        //Color is reset, menu is resized and title is changed
        this.setSize(450, 300);
        canvas.setCanvasBackground(null);
        this.setTitle("Local Settings");
        //Previous parts are removed
        canvas.removeAllElements();

        //Text "Map Name" is added
        var mapLabel = uiElementFacade.CreateLabel("Map Name");
        canvas.addLabel(mapLabel, 0, 0);

        //Text field for map name is added defaultMap is entered automatically
        final var mapTextField = getNewTextField("DOD3/defaultMap");
        canvas.addTextField(mapTextField, 1, 0);

        //Text "Name" is added
        var nameLabel = uiElementFacade.CreateLabel("Name");
        canvas.addLabel(nameLabel, 0, 1);

        //Text Field for the players name is added it is blank by default
        final var nameTextField = getNewTextField("");
        canvas.addTextField(nameTextField, 1, 1);

        //Create the Bot Button
        var botButton = uiElementFacade.CreateButton("Play as Bot", null, actionListener -> {
            try {
                //Game and Communicator is made
                GameLogic game = new GameLogic(mapTextField.toString()); //Map name is taken from the text field
                //Bot Selection Menu is displayed
                name = nameTextField.toString();
                displayBotSelectionMenu(new LocalGameCommunicator(game), nameTextField.toString(), false); //Name is taken from the text field
                //Game is started
                game.startGame();
                //Catch statements send error message using JOptionPanes, they are located here so the user may enter the details again
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Map File not Found");
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, "Map File is corrupted or formatted incorrectly");
            }
        });
        //Add the Bot Button
        canvas.addButton(botButton, 1, 2);

        //Create the Human Button
        var humanButton = uiElementFacade.CreateButton("Play as Human", null, actionListener -> {
            try {
                //Game and Communicator is made
                GameLogic game = new GameLogic(mapTextField.toString()); //Map name is taken from the text field
                //GUI is displayed
                name = nameTextField.toString();
                displayGameGUI(new HumanPlayerGUI(new LocalGameCommunicator(game), nameTextField.toString(), true)); //Name is taken from the text field
                //Game is started
                game.startGame();
                //Catch statements send error message using JOptionPanes, they are located here so the user may enter the details again
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Map File not Found");
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, "Map File is corrupted or formatted incorrectly");
            }
        });
        //Human Button is added
        canvas.addButton(humanButton, 0, 2);

        //Update the menu
        updateMenu();
    }

    /**
     * Displays the menu for playing over a network
     */
    private void displayNetworkGameMenu() {
        //Background is reset and screen is resized and title is changed
        canvas.setCanvasBackground(null);
        this.setSize(450, 300);
        this.setTitle("Network game");
        //Previous components are removed
        canvas.removeAllElements();

        //Host Button is Created
        var hostButton = uiElementFacade.CreateButton("Host Game", "Host the Game over a network", actionListener -> {
            //New menu is displayed
            displayStartServerMenu();
        });
        //Host Button is Added
        canvas.addButton(hostButton, 0, 0);
        //Join Button is Created
        var joinButton = uiElementFacade.CreateButton("Join Game", "Join a Game over a network", actionListener -> {
            //New menu is displayed
            displayJoinServerMenu();
        });
        //Join Button is Added
        canvas.addButton(joinButton, 0, 1);

        //Menu is updated
        updateMenu();
    }

    /**
     * Displays the menu to start the server
     */
    private void displayStartServerMenu() {
        //Previous components are removed
        canvas.removeAllElements();
        //Title is set
        this.setTitle("Start Server");

        var portLabel = uiElementFacade.CreateLabel("Port"); //Port Text
        final var portTextField = getNewTextField(""); //Text field for the Port Number
        final var mapLabel = uiElementFacade.CreateLabel("Map Name"); //"Map Name" text
        final var mapTextField = getNewTextField("DOD3/defaultMap"); //Text field for the Map name
        final var localPlayCheckBox = uiElementFacade.CreateCheckBox("Play on Server"); //Check box to indicate if the user wants to play on the server

        //Button to start the server
        final var startButton = uiElementFacade.CreateButton("Start Server", null, actionListener -> {
            //Port is converted
            int port = convertStringToPortNumber(portTextField.toString());

            //If port is less than 0 then we show an error message and nothing more is happened
            if ((port < 0) || (port >= 65535)) {
                JOptionPane.showMessageDialog(null, "Invalid Port"); //error message
                return;
            }

            try {
                //Server is created using the map name from the field and the converted port number and the created game is returned
                //This also opens the Server GUI
                GameLogic game = startServerGUI(mapTextField.toString(), port);
                //If the user wants to play on the server then a LocalGameCommunicator is made and the user plays on the game locally
                //The next menu is then displayed asking details of the player
                if (localPlayCheckBox.isCheckBoxSelected()) {
                    displayPlayNetworkGameMenu(new LocalGameCommunicator(game), true);
                } else {
                    //Otherwise the menu is simply hidden as it is no longer needed
                    hideMenu();
                }
            }
            //Catch statements send error message using JOptionPanes, they are located here so the user may enter the details again
            catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Map File not Found");
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, "Map File is corrupted or formatted incorrectly");
            }
        });

        //The components are then added to the container in the designed format

        canvas.addLabel(portLabel, 0, 0);

        canvas.addTextField(portTextField, 1, 0);

        canvas.addLabel(mapLabel, 0, 1);

        canvas.addTextField(mapTextField, 1, 1);

        canvas.addCheckBox(localPlayCheckBox, 0, 2, 2);

        canvas.addButton(startButton, 0, 3, 0);

        //Menu is updated
        updateMenu();
    }

    /**
     * The Join Menu to allow the user to join a Dungeon of Doom 3 Server
     */
    private void displayJoinServerMenu() {
        //Components are removed
        canvas.removeAllElements();
        //Title is set
        this.setTitle("Join Server");

        var ipAddressLabel = uiElementFacade.CreateLabel("IP Address"); //"IP Address" Text
        final var ipAddressTextField = getNewTextField(""); //Text Field for the user to enter the IP Address
        var portLabel = uiElementFacade.CreateLabel("Port"); //"Port" Text
        final var portTextField = getNewTextField(""); //Text field for the user to enter the Port Number
        //Button to join the server
        final var joinButton = uiElementFacade.CreateButton("Join Server", null, actionListener -> {
            //The port is converted from the text field
            int port = convertStringToPortNumber(portTextField.toString());

            //If the port is invalid display error message and do nothing more
            if (port < 0) {
                JOptionPane.showMessageDialog(null, "Invalid Port");
                return;
            }

            try {
                //New game communicator id made using the converted port and the ip address in the text field
                GameCommunicator comm = new NetworkGameCommunicator(ipAddressTextField.toString(), port);
                //If no exception is thrown the next menu is displayed which allows the player to select how they want to play
                displayPlayNetworkGameMenu(comm, false);
                //Catch statements send error message using JOptionPanes, they are located here so the user may enter the details again
            } catch (UnknownHostException e) {
                JOptionPane.showMessageDialog(null, "Server not found.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Network Error");
            }
        });

        //Components are added

        canvas.addLabel(ipAddressLabel, 0, 0);

        canvas.addLabel(portLabel, 0, 1);

        canvas.addTextField(ipAddressTextField, 1, 0);

        canvas.addTextField(portTextField, 1, 1);

        canvas.addButton(joinButton, 0, 2, 2);

        //Menu is updated
        updateMenu();
    }

    public static String name = "";

    public static String getPlayerName() {
        return MainMenu.name;
    }

    /**
     * Displays the Menu for joining the game
     *
     * @param comm     GameCommunication The communication object for the player
     * @param isServer Boolean indicates if the player is also running the server
     */
    private void displayPlayNetworkGameMenu(final GameCommunicator comm, final boolean isServer) {
        //Removes all the components from the container
        canvas.removeAllElements();
        //Set the title
        this.setTitle("Play Game");

        var nameLabel = uiElementFacade.CreateLabel("Name"); //"Name" text
        final var nameTextField = getNewTextField(""); //Text Field for the player name


        //Button to play as a Bot
        final var botButton = uiElementFacade.CreateButton("Play as Bot", null, actionListener -> {
            //Bot Selection menu is displayed
            name = nameTextField.toString();
            displayBotSelectionMenu(comm, nameTextField.toString(), isServer);
        });

        //Button to play as a human
        final var humanButton = uiElementFacade.CreateButton("Play as Human", null, actionListener -> {
            //Human GUI is displayed
            name = nameTextField.toString();
            displayGameGUI(new HumanPlayerGUI(comm, nameTextField.toString(), !isServer));
        });

        //Components are added

        canvas.addLabel(nameLabel, 0, 0);

        canvas.addTextField(nameTextField, 1, 0);

        canvas.addButton(botButton, 0, 1);

        canvas.addButton(humanButton, 1, 1);

        //Menu is updated
        updateMenu();
    }

    /**
     * Display the Bot Selection menu where the user can choose the Bot they want to use
     *
     * @param comm     GameCommunicator The Communication object used for the player
     * @param name     String The Name of the player
     * @param isServer Boolean indicates if the player is also running the server
     */
    private void displayBotSelectionMenu(final GameCommunicator comm, final String name, final boolean isServer) {
        //Components are removed
        canvas.removeAllElements();
        //Title is set
        this.setTitle("Choose your Bot AI");

        //Radio Button for each bot type with a description in the tool tip
        final var randomBotButton = uiElementFacade.CreateRadioButton("Baldrick");
        randomBotButton.setRadioButtonToolTipText("Likes to run in circles");
        final var objectiveBotButton = uiElementFacade.CreateRadioButton("Wes");
        objectiveBotButton.setRadioButtonToolTipText("Determined to get out as fast as possible");
        final var aggressiveBotButton = uiElementFacade.CreateRadioButton("Ledeon");
        aggressiveBotButton.setRadioButtonToolTipText("Not a force to be reckoned with");
        final var friendlyBotButton = uiElementFacade.CreateRadioButton("Alison");
        friendlyBotButton.setRadioButtonToolTipText("Strangely helpful, We're not sure she knows how to win");

        //Create play button
        final var playButton = uiElementFacade.CreateButton("Play", "Start Game", actionListener -> {
            Bot bot;
            //Bot Type Radio buttons determines the dynamic type of bot
            if (randomBotButton.isRadioButtonSelected()) {
                bot = new RandomBotStrategy(comm);
            } else if (objectiveBotButton.isRadioButtonSelected()) {
                bot = new ObjectiveBotStrategy(comm);
            } else if (aggressiveBotButton.isRadioButtonSelected()) {
                bot = new AggressiveBotStrategy(comm);
            } else if (friendlyBotButton.isRadioButtonSelected()) {
                bot = new FriendlyBotStrategy(comm);
            } else {
                //If none of them are selected display error
                JOptionPane.showMessageDialog(null, "Please select a bot AI");
                return;
            }
            //Bot Player is created using the newly created bot and the other parameters passed into this function
            displayGameGUI(new BotPlayerGUI(comm, name, !isServer, bot));
        });

        //All radio buttons are added to the button group
        var AIBotButtons = uiElementFacade.CreateButtonGroup();
        AIBotButtons.addRadioButton(randomBotButton, true);
        AIBotButtons.addRadioButton(objectiveBotButton, false);
        AIBotButtons.addRadioButton(aggressiveBotButton, false);
        AIBotButtons.addRadioButton(friendlyBotButton, false);

        //Components are added into the container

        canvas.addRadioButton(randomBotButton, 0, 0);

        canvas.addRadioButton(objectiveBotButton, 1, 0);

        canvas.addRadioButton(friendlyBotButton, 1, 1);

        canvas.addRadioButton(aggressiveBotButton, 0, 1);

        canvas.addButton(playButton, 0, 2, 2);

        //Menu is updated
        updateMenu();
    }

    /**
     * Displays a given Game GUI
     *
     * @param gui MessageFeedGUI The Game GUI to be displayed
     */
    private void displayGameGUI(MessageFeedGUI gui) {
        //Menu is hidden
        hideMenu();
        //Menu doesn't exit the program
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Display function is called
        gui.displayGUI();
    }

    /**
     * Hides the menu from view
     */
    private void hideMenu() {
        //All components are removed
        canvas.removeAllElements();
        //Container is removed
        this.setVisible(false);
    }

    /**
     * Updates the menu
     */
    private void updateMenu() {
        //Update functions are called
        this.setVisible(true);
        canvas.repaintCanvas();
    }

    /**
     * Gets a new standardised Test Field
     *
     * @param defaultText String the default text to be places in the text field
     * @return TextField the standardised text field
     */
    private TextField getNewTextField(String defaultText) {
        //Uses standardised text box size
        var textField = uiElementFacade.CreateTextField(null, settings.getTextBoxSize());
        textField.setTextFieldText(defaultText);
        return textField;
    }

    /**
     * Starts the server and displays the server GUI
     *
     * @param mapName String The name of the map file
     * @param port    int The port number
     * @return GameLogic The game created in the launching of their server
     * @throws ParseException ParseException throwing
     * @throws FileNotFoundException FileNotFoundException throwing
     */
    private GameLogic startServerGUI(String mapName, int port) throws ParseException, FileNotFoundException {
        //GUI class is created
        ServerGUI gui = new ServerGUI();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Game is created
        GameLogic game = new GameLogic(mapName, gui);
        //Game is added to the GUI
        gui.addGame(game);
        //Server class is created and started
        Server server = new Server(game, port);
        server.start();
        //gui is displayed
        gui.displayGUI();
        return game;
    }

    /**
     * Converts a string to an int
     *
     * @param portString The String that represents the port number
     * @return int The converted string as an int, it returns -1 if the string is in a number format
     */
    private int convertStringToPortNumber(String portString) {
        try {
            //String is parsed
            return Integer.parseInt(portString);
        } catch (NumberFormatException e) {
            //If parse fails then -1 is returned
            return -1;
        }

    }

}
