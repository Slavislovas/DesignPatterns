package dod.game;

import dod.Composite.Achievement;
import dod.Composite.AchievementComposite;
import dod.Composite.AttackAchievement;
import dod.Composite.GiftAchievement;
import dod.Composite.MovementAchievement;
import dod.GUI.ClientListener;
import dod.GUI.MainMenu;
import dod.abstractfactory.AbstractFactory;
import dod.builder.Weapon;
import dod.decorator.GoldDecorator;
import dod.decorator.HealthDecorator;
import dod.decorator.MessageDecorator;
import dod.factory.MapCreator;
import dod.game.items.GameItem;
import dod.game.items.armour.Armour;
import dod.game.items.sword.Sword;
import dod.game.maps.Map;
import dod.game.items.ItemType;
import dod.iterator.CustomCollection;
import dod.iterator.Iterator;
import dod.iterator.PlayerList;
import dod.observer.Subject;
import dod.singleton.Settings;
import dod.visitor.Visitor;
import lombok.Getter;
import lombok.Setter;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class controls the game logic and other such magic.
 * <p>
 * This has been modified by Benjamin Dring. It's important to note that all player threads have the same object of this class.
 */
@Getter
@Setter
public class GameLogic {
    Map map;

    // Has a player won already?
    private boolean playerWon;

    private boolean gameOver;

    // current player
    private Player player;

    //The following three variables have been implemented by Benjamin Dring
    public CustomCollection<Player> playerList; //This is the list of all players
    int currentPlayerIndex; //The index in playerList of the current player
    private boolean turnSwitch; //Used to lock the class from being accessed during transfer of turns

    private ClientListener serverListener; //For any server listening to this GameLogic //This can be null

    private boolean gameStarted;

    private AbstractFactory gameItemFactory;
    private Subject subject = new Subject();

    private Random rand;

    private Settings settings = Settings.getInstance();
    private AchievementComposite achievements;
    private Visitor visitor;

    /**
     * Constructor that specifies the map which the game should be played on.
     *
     * @param mapFile The name of the file to load the map from.
     * @throws FileNotFoundException , ParseException
     */
    public GameLogic(String mapFile) throws FileNotFoundException,
            ParseException {
        this.map = new MapCreator().factoryMethod(mapFile);
        this.serverListener = null;
        this.gameItemFactory = map.getAbstractFactory();
        this.rand = new Random();
        playerList = new PlayerList();
        setUpAttributes();

        achievements = new AchievementComposite(
                new AchievementComposite( // AGGRESSIVE ACHIEVEMENTS
                        new AchievementComposite( // ADVANCED AGGRESSIVE ACHIEVEMENTS
                                new AttackAchievement("ADVANCED_KILL")
                        ),
                        new AchievementComposite( // BASIC AGGRESSIVE ACHIEVEMENTS
                                new AttackAchievement("BASIC_PICKUP_SWORD"),
                                new AttackAchievement("BASIC_PICKUP_ARMOR")
                        )
                ),
                new AchievementComposite( // PASSIVE ACHIEVEMENTS
                        new AchievementComposite( // BASIC PASSIVE ACHIEVEMENTS
                                new MovementAchievement("BASIC_MOVE_FIRST_TIME"),
                                new GiftAchievement("BASIC_GIFT_GOLD"),
                                new MovementAchievement("BASIC_MOVE_AGAIN")
                        )
                )
        );

        this.visitor = new Visitor();

        // Check if there is enough gold to win
        if (this.map.remainingGold() < this.map.getGoal()) {
            throw new IllegalStateException(
                    "There isn't enough gold on this map for you to win");
        }
    }

    /**
     * Constructor that specified the map which the game should be played on and a listener
     * to receive generic global messages
     *
     * @param mapFile        The name of the file to be loaded from.
     * @param serverListener The listener to relay global messages to
     * @throws FileNotFoundException
     * @throws ParseException
     */
    public GameLogic(String mapFile, ClientListener serverListener) throws FileNotFoundException,
            ParseException {
        this(mapFile);
        this.serverListener = serverListener;
    }

    /**
     * Sets the attributes of the class to their default value
     */
    private void setUpAttributes() {
        this.playerWon = false;
        this.gameOver = false;
        this.currentPlayerIndex = 0;
        this.turnSwitch = false;
        this.gameStarted = false;
    }

    /**
     * Adds a new player to the game.
     * <p>
     * It has been modified by Benjamin Dring to except multiple players
     *
     * @param listener the PlayerListener which will listen on behalf of that player,
     *                 so messages can be sent to the player
     * @return int the UserID which is equivalent to the list index
     */
    public int addPlayer(PlayerListener listener) {
        String playerName = MainMenu.getPlayerName();

        Player player = new Player(playerName, generateRandomStartLocation(),
                listener);

        if (settings.getUserRole(playerName) == "vip") {
            System.out.println("VIP DECOR");
            player = new GoldDecorator(player);
        }
        if (settings.getUserRole(playerName) == "supervip") {
            System.out.println("SUPERVIP DECOR");
            player = new HealthDecorator(player);
            player = new GoldDecorator(player);
        }
        if (settings.getUserRole(playerName) == "admin") {
            System.out.println("ADMIN DECOR");
            player = new HealthDecorator(player);
            player = new MessageDecorator(player);
            player = new GoldDecorator(player);
        }

        playerList.add(player);

        //If this is the only player then set it as the current player
        if (playerList.size() == 1) {
            this.player = playerList.get(0);
        }
        subject.registerObserver(listener);
        //this will be the index of the players location in the list and is considered the USER ID
        return playerList.size() - 1;

    }

    /**
     * Starts a new game of the Dungeon of Dooooooooooooom.
     */
    public void startNewGame() {
        if (this.player == null) {
            throw new IllegalStateException(
                    "FAIL: There is no player on the map");
        }

        startTurn();
    }

    /**
     * Handles the client message HELLO
     * <p>
     * It has been modified by Benjamin Dring to allow the user's who aren't playing to issue the hello command
     * using their User ID.
     *
     * @param newName the name of the player to say hello to
     * @param userID  the ID of the player
     * @return the message to be passed back to the command line
     * @throws CommandException
     */
    public void clientHello(String newName, int userID) throws CommandException {
        assertPlayerExists();

        // Change the player name and then say hello to them
        this.playerList.get(userID).setName(newName);
    }

    /**
     * Handles the client message LOOK Shows the portion of the map that the
     * player can currently see.
     *
     * @return the part of the map that the player can currently see.
     */
    public String clientLook(int playerID) {
        assertPlayerExists();

        Player lookingPlayer = this.playerList.get(playerID);

        // Work out how far the player can see
        final int distance = lookingPlayer.lookDistance();

        String lookReply = "";
        // Iterate through the rows.
        for (int rowOffset = -distance; rowOffset <= distance; ++rowOffset) {
            String line = "";

            // Iterate through the columns.
            for (int colOffset = -distance; colOffset <= distance; ++colOffset) {

                // Work out the location
                final Location location = lookingPlayer.getLocation().atOffset(
                        colOffset, rowOffset);

                int tilePlayerID = getUserIDOfPlayerOnTile(location);

                char content = '?';
                if (!lookingPlayer.canSeeTile(rowOffset, colOffset)) {
                    // It's outside the FoV so we don't know what it is.
                    content = 'X';
                } else if (!this.map.insideMap(location)) {
                    // It's outside the map, so just call it a wall.
                    content = '#';
                    //-1 represents there is no player on the tile
                } else if ((tilePlayerID != -1) && ((rowOffset != 0) || (colOffset != 0))) {
                    //P is a player on a standard tile
                    //Q is a player on an exit tile
                    //R is a player with armour on a standard tile
                    //K is a player with armour on an exit tile
                    if (this.map.getMapCell(location).toChar() == 'E') {
                        if (playerList.get(tilePlayerID).hasItem(gameItemFactory.createArmour(map.get_mediator()))) {
                            content = 'K';
                        } else {
                            content = 'Q';
                        }
                    } else {
                        if (playerList.get(tilePlayerID).hasItem(gameItemFactory.createArmour(map.get_mediator()))) {
                            content = 'R';
                        } else {
                            content = 'P';
                        }
                    }
                } else {
                    // Look up and see what's on the map
                    content = this.map.getMapCell(location).toChar();
                }

                // Add to the line
                line += content;
            }

            // Send a line of the look message
            lookReply += line + System.getProperty("line.separator");
        }

        return lookReply;
    }

    /**
     * Handles the client message MOVE
     * <p>
     * Move the player in the specified direction - assuming there isn't a wall
     * in the way
     *
     * @param direction The direction (NESW) to move the player
     * @return An indicator of the success or failure of the movement.
     * @throws CommandException
     */
    public void clientMove(CompassDirection direction) throws CommandException {
        assertPlayerExists();
        ensureNoWinner();
        assertPlayerAP();

        // Work out where the move would take the player
        final Location location = this.player.getLocation().atCompassDirection(direction);

        // Ensure that the movement is within the bounds of the map and not
        // into a wall
        if (!this.map.insideMap(location)
                || !this.map.getMapCell(location).isWalkable()) {
            throw new CommandException("can't move into a wall");
        }

        /**
         * @author Benjamin Dring
         * Checks to see if a player is there now
         *
         */
        if (isPlayerOnTile(location)) {
            throw new CommandException("can't move into a player");
        }

        /**
         * End of modification
         */
        // Costs one action point
        player.handleRequest("Decrement AP", 1);

        // Move the player
        this.player.setLocation(location);

        achievements.unlock("BASIC_MOVE_FIRST_TIME");

        // Notify the client of the success
        advanceTurn();

    }

    /**
     * Handles the client message ATTACK
     * <p>
     * This has been implemented by Benjamin Dring
     *
     * @param direction The direction in which to attack
     * @return A message indicating the success or failure of the attack
     * @throws CommandException
     * @author Benjamin Dring
     */
    public void clientAttack(CompassDirection direction)
            throws CommandException {
        assertPlayerExists();
        ensureNoWinner();
        assertPlayerAP();

        Weapon playerWeapon = this.player.getWeapon();
        int victimUserId = findPlayerIdInDirectionAndRange(direction, playerWeapon.getRange());
        if (victimUserId < 0) {
            throw new CommandException("There is no player there.");
        }

        //AP is zeroed
        this.player.handleRequest("Zero AP", 0);

        Player victimPlayer = playerList.get(victimUserId);
        //We randomly decide if it hits
        if (rand.nextInt(5) < 3) //3 of 4 chance is a hit as 5 is exclusive
        {
            //if it hits we get the victim player from the list
            int damage = playerWeapon.getDamage();
            if (victimPlayer.hasItem(gameItemFactory.createArmour(map.get_mediator()))) {
                //minus one for victim having armour
                damage--;
            }
            //Damage player and display message to attacker
            victimPlayer.handleRequest("Damage", damage);
            this.player.sendMessage("You hit your target for " + damage + " hp with weapon " + playerWeapon.getType().name());
            victimPlayer.sendMessage("A player hit you for " + damage + " hp with weapon " + playerWeapon.getType().name());
        } else {
            advanceTurn();
            victimPlayer.sendMessage("A player tried and failed to hit you");
            //If it misses throw exception
            throw new CommandException("You Missed the target.");
        }

        if (victimPlayer.isDead()) {
            map.dropGold(victimPlayer.getLocation());
            this.player.sendMessage("The player has died");
            victimPlayer.sendMessage("DIE You were killed by a player");
            achievements.unlock("ADVANCED_KILL");
        }
        advanceTurn();
        subject.notifyObservers(String.format("%s attacked in direction %s", this.player.getName(), direction));
    }

    private int findPlayerIdInDirectionAndRange(CompassDirection direction, int range) {
        Location location;
        for(int i = 1; i <= range; i++) {
            location = this.player.getLocation().atCompassDirection(direction, i);
            int victimId = getUserIDOfPlayerOnTile(location);
            if (victimId != -1){
                return victimId;
            }
        }
        return -1;
    }

    /**
     * Gives gold in a certain direction from the current player
     *
     * @param direction the direction to give gold
     * @throws CommandException
     */
    public void clientGift(CompassDirection direction) throws CommandException {
        //Gets the location of the given direction
        final Location location = this.player.getLocation().atCompassDirection(direction);

        //gets the User ID (playerListIndex) of the player if its -1 then there is no player so we throw an exception
        int recieverUserID = getUserIDOfPlayerOnTile(location);
        if (recieverUserID < 0) {
            throw new CommandException("There is no player there.");
        }
        if (this.player.getGold() <= 0) {
            throw new CommandException("You Have no Gold");
        }

        Player recieverPlayer = playerList.get(recieverUserID);

        // Costs one action point
        player.handleRequest("Decrement AP", 1);

        this.player.handleRequest("Gold", -1);
        recieverPlayer.handleRequest("Gold", 1);
        recieverPlayer.sendMessage("You were given 1 gold by " + this.player.getName());
        achievements.unlock("BASIC_GIFT_GOLD");

        //We need to check if the receiver has now won
        if ((recieverPlayer.getGold() >= this.map.getGoal())
                && (this.map.getMapCell(recieverPlayer.getLocation()).isExit())) {

            //if it has we end the game and set up everythin
            assert (!this.playerWon);

            this.playerWon = true;
            this.gameOver = true;

            lookAll();

            recieverPlayer.win();
            this.player.sendMessage("DIE YOU GAVE UP THE GAME");
        }
        lookAll();
        subject.notifyObservers(String.format("%s gifted gold", this.player.getName()));
	}

    /**
     * Handles the client message PICKUP. Generally it decrements AP, and gives
     * the player the item that they picked up Also removes the item from the
     * map
     *
     * @return A message indicating the success or failure of the action of
     * picking up.
     * @throws CommandException
     */
    public void clientPickup() throws CommandException {
        assertPlayerExists();
        ensureNoWinner();
        assertPlayerAP();

        final Tile playersTile = this.map.getMapCell(this.player.getLocation());

        // Check that there is something to pick up
        if (!playersTile.hasItem()) {
            throw new CommandException("nothing to pick up");
        }

        // Get the item
        final GameItem item = playersTile.getItem();

        if (this.player.hasItem(item) && !item.getType().equals(ItemType.SWORD)) {
            throw new CommandException("already have item");
        }

        this.player.giveItem(item);
        playersTile.removeItem();

        if (item instanceof Armour) {
            this.player.sendMessage("You equip Armour");
            achievements.unlock("BASIC_PICKUP_ARMOR");
        } else if (item instanceof Sword) {
            this.player.sendMessage("You equip Sword");
            achievements.unlock("BASIC_PICKUP_SWORD");
        }

        advanceTurn();
        subject.notifyObservers(String.format("%s picked up %s", this.player.getName(), item));
    }

    /**
     * Returns the current message to the client. Note that this becomes
     * important when using multiple clients across a network, where this could
     * send to multiple players
     * <p>
     * This has been modified by Benjamin Dring to shout to all players
     *
     * @param message The message to be shouted
     */
    public void clientShout(String message, int speakerID) {
        System.out.println("1.   | "+playerList.get(speakerID).getName());
        message = "[" + playerList.get(speakerID).getName() + "] " + message;
        sendToAll(message);
    }

    /**
     * Sends a single message to all players and listening server
     *
     * @param message String message to be sent
     */
    public void sendToAll(String message) {
        if (serverListener != null) {
            //Sends server message if it exists
            serverListener.pushMessage(message);
        }
        //Loops through every player and sends a message
        for (int userID = 0; userID < playerList.size(); userID++) {
            playerList.get(userID).sendMessage(message);
        }
    }

    /**
     * Handles the client message ENDTURN
     * <p>
     * Just sets the AP to zero and advances as normal.
     *
     * @return A message indicating the status of ending a turn (currently
     * always successful).
     */
    private void clientEndTurn() {
        assertPlayerExists();
        this.player.endTurn();

    }

    /**
     * Sets the player's position. This is used as a cheating or debug command.
     * It is particularly useful for testing, as it gets rounds the randomness
     * of the player start position.
     *
     * @param col the column of the location to put the player
     * @param row the row to location to put the player
     * @throws CommandException
     */
    public void setPlayerPosition(int col, int row) throws CommandException {
        assertPlayerExists();
        final Location location = new Location(col, row);

        if (!this.map.insideMap(location)) {
            throw new CommandException("invalid position");
        }

        if (!this.map.getMapCell(location).isWalkable()) {
            throw new CommandException("cannot walk on this tile");
        }

        this.player.setLocation(location);
    }

    /**
     * Passes the goal back
     *
     * @return the current goal
     */
    public int getGoal() {
        return this.map.getGoal();
    }

    /**
     * Generates a randomised start location
     * <p>
     * This has been modified by Benjamin Dring to consider other players
     *
     * @return a random location where a player can start
     */
    private Location generateRandomStartLocation() {
        if (!atLeastOneWalkablelLocation()) {
            throw new IllegalStateException(
                    "There is no free tile available for the player to be placed");
        }

        while (true) {
            // Generate a random location
            final int randomRow = rand.nextInt(this.map.getMapHeight());
            final int randomCol = rand.nextInt(this.map.getMapWidth());

            final Location location = new Location(randomCol, randomRow);

            //it now checks if the player is on that tile when randomly generating the location
            if (this.map.getMapCell(location).isWalkable() && (!isPlayerOnTile(location))) {
                // If it's not a wall or a player then we can put them there
                return location;
            }
        }
    }

    /**
     * Searches a possible tile to use by the player, i.e. non-wall. The map is
     * traversed from (0,0) to (maxY,MaxX).
     * <p>
     * This has been modified by Benjamin Dring to consider other players
     *
     * @return true if there is at least one non-wall location, false otherwise
     */
//    private boolean atLeastOneWalkablelLocation() {
//        for (int x = 0; x < this.map.getMapWidth(); x++) {
//            for (int y = 0; y < this.map.getMapHeight(); y++) {
//
//                Location location = new Location(x, y);
//
//                //it now checks to see if there is a player on the location
//                if (this.map.getMapCell(location).isWalkable() && (!isPlayerOnTile(location))) {
//                    // If it's not a wall then we can put them there
//                    return true;
//                }
//            }
//        }
//
//        return false;
//    }

    private boolean atLeastOneWalkablelLocation() {
        for (Iterator<Location> iterator = map.getIterator(); iterator.hasNext();){
            Location location = iterator.next();
            if (!isPlayerOnTile(location)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Ensures a player has been added to the map. Otherwise, an exception is
     * raised. In a multiplayer scenario, this could ensure a player by given ID
     * exists.
     *
     * @throws RuntimeException
     */
    private void assertPlayerExists() throws RuntimeException {
        if (this.player == null) {
            throw new IllegalStateException(": Player has not been added.");
        }
    }

    /**
     * Ensures a player has enough AP, otherwise a runtime error is raised,
     * since the turn should have been advanced. In a multiplayer example, this
     * is still a bug, since the server should have checked whose turn it was.
     *
     * @throws RuntimeException
     */
    private void assertPlayerAP() throws RuntimeException {
        if (this.player.remainingAp() == 0) {
            throw new IllegalStateException("Player has 0 ap");
        }
    }

    /**
     * Ensure that no player has won the game. Throws a CommandException if
     * someone has one, preventing the command from executing
     *
     * @throws CommandException
     */
    public void ensureNoWinner() throws CommandException {
        if (this.playerWon) {
            throw new CommandException("loose");
        }
    }

    /**
     * This doesn't really do anything as yet, other than reset the player AP.
     * It should do more when the game is multiplayer
     */
    private void startTurn() {
        this.player.startTurn();
    }

    /**
     * Once a player has performed an action the game needs to move onto the
     * next turn to do this the game needs to check for a win and then test to
     * see if the current player has more AP left.
     * <p>
     * Has been modified by Benajmin Dring to allow multiple players
     * <p>
     * Note that in this implementation we currently playing this as a single
     * player game so the next turn will always be the current player so we
     * simply start their turn again.
     */
    private void advanceTurn() {
        // Check if the player has won
        if ((this.player.getGold() >= this.map.getGoal())
                && (this.map.getMapCell(this.player.getLocation()).isExit())) {

            // Player should not be able to move if they have won
            assert (!this.playerWon);

            this.playerWon = true;
            this.gameOver = true;

            lookAll();
            subject.notifyObservers(String.format("Game has finished! %s has won", this.player.getName()));
            for (Iterator<Achievement> iterator = achievements.getIterator(); iterator.hasNext();) {
                Achievement achievement = iterator.next();
                player.getListener().update("Achievement: " +
                        achievement.getIdentifier() + " completion status: " +
                        achievement.isUnlocked());
            }
            this.achievements.accept(visitor);
            player.getListener().update("Your total score for achievements: " + visitor.getTotalPoints());
            this.player.win();
        } else {
            //Now newTurn is called instead for dead players and when there is no Ap left
            if (this.player.isDead()) {
                map.dropGold(this.player.getLocation());
                subject.notifyObservers(String.format("%s is dead!", this.player.getName()));
                this.achievements.accept(visitor);
                player.getListener().update("Your total score for achievements: " + visitor.getTotalPoints());
                newTurn();
                lookAll();
            }
            if (this.player.remainingAp() == 0) {
                subject.notifyObservers(String.format("%s has no turns!", this.player.getName()));
                newTurn();
            }
        }
    }

    /**
     * Checks if a player is on the tile
     *
     * @param location the location to check
     * @return boolean true indicates a player is on the tile
     */
    private boolean isPlayerOnTile(Location location) {
        //Just translates getUserIDOfPlayerOnTile into a boolean
        return (getUserIDOfPlayerOnTile(location) != -1);
    }

    /**
     * Gets the User Id of the player on a given location, Dead Players are not condiered here
     *
     * @param location The location that the player is located
     * @return the userId (list index) of the player on the tile it returns -1 if there is no player on the tile
     */
    private int getUserIDOfPlayerOnTile(Location location) {
//        for (int index = 0; index < playerList.size(); index++) {
//            Location playerLocation = playerList.get(index).getLocation();
//            //checks to see if the player is alive and on the same place
//            if ((!playerList.get(index).isDead()) &&
//                    (location.getCol() == playerLocation.getCol()) &&
//                    (location.getRow() == playerLocation.getRow())) {
//                return index;
//            }
//        }
        for (Iterator<Player> iterator = playerList.getIterator(); iterator.hasNext();){
            Location playerLocation = iterator.next().getLocation();
            if ((location.getCol() == playerLocation.getCol()) &&
                    (location.getRow() == playerLocation.getRow())) {
                return player.getListener().userId();
            }
        }
        return -1;
    }

    /**
     * Checks if the UserID is the Id of the user who's turn it is
     *
     * @param userID The User ID
     * @return boolean true indicates it is the user's turn
     */
    public boolean isPlayerTurn(int userID) {
        //Players can't act during a turn switch otherwise they may do an action with no AP
        return ((userID == currentPlayerIndex) && (!turnSwitch));
    }

    /**
     * Ends the players turn switched to the next player and starts that players turn
     */
    public void newTurn() {
        if (isAlivePlayer()) {

            turnSwitch = true; //locks the class to prevent accesses, it is implemented in isPlayerTyrn()

            // Force the end of turn
            clientEndTurn();

            boolean nextPlayerChosen = false; //Indicates to see if a player has been chosen

            while (!nextPlayerChosen) {
                //Move onto the next player
                if (++currentPlayerIndex >= playerList.size()) {
                    currentPlayerIndex = 0;
                }
                //Only pick them if they're not dead, if they are dead loop back through
                nextPlayerChosen = !playerList.get(currentPlayerIndex).isDead();
            }

            player = playerList.get(currentPlayerIndex); //player is changed
            startTurn();
            //unlocks class
            turnSwitch = false;
        } else {
            setUpAttributes();
            serverListener.restartGame();
        }
    }

    /**
     * Sets a given user's health to 0
     *
     * @param userID The ID of the user to be killed
     */
    public void die(int userID) {
        this.playerList.get(userID).handleRequest("Kill", 0);
        map.dropGold(this.playerList.get(userID).getLocation()); //gold is dropped
        if (isPlayerTurn(userID)) {
            newTurn();
        }
    }

    /**
     * Checks to see if there is any living players left if there isn't then the game is ended.
     */
    private boolean isAlivePlayer() {
        return playerList.getIterator().hasNext();
//        for (Player player : playerList) {
//            if (!player.isDead()) {
//                return true;
//            }
//        }
//        return false;
    }

    /**
     * Performs the look reply to every player
     */
    public void lookAll() {
        for (Iterator<Player> iterator = playerList.getIterator(); iterator.hasNext();){
            iterator.next().look();
        }
//        for (Player player : playerList) {
//            player.look();
//        }
    }

    /**
     * Starts the game
     */
    public void startGame() {
        if (player != null) {
            this.player.startTurn();
        }
        this.gameStarted = true;
    }

    public Player getPlayerByIndex(int index){
        return playerList.get(index);
    }
}
