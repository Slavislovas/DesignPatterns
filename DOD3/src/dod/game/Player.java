package dod.game;

import dod.builder.FistWeaponBuilder;
import dod.builder.Weapon;
import dod.builder.WeaponDirector;
import dod.chainOfResponsibility.*;
import dod.game.items.GameItem;
import dod.game.items.GameItemConsumer;
import dod.game.items.ItemType;
import dod.proxy.WeaponBuilderType;
import dod.iterator.GameItemLinkedList;
import dod.iterator.CustomCollection;
import dod.iterator.Iterator;
import lombok.Getter;
import lombok.Setter;

/**
 * Class representing a player
 * <p>
 * Note: Player variables should be kept as a part of the server functionality
 * in your coursework implementation, they are the game's internal
 * representation of a player, not used by the client side code.
 */
@Getter
@Setter
public class Player implements GameItemConsumer {
    protected MessageHandler nextHandler;
    private String name;

    // Does the player have a default name
    boolean defaultName = true;

    // The player may be "listened to" to interpret updates
    private final PlayerListener listener;

    // Location on the map
    private Location location;

    // How much gold they have, initially zero
    private int gold = 0;

    // Player attribute value things
    @Getter
    @Setter
    private int hp = 3;
    private int ap = 0;

    private Weapon weapon;

    @Getter
    // Items the player has
    CustomCollection<GameItem> items;

    // Constants
    // How far can a player see by default and with a lantern
    private static final int defaultLookDistance = 2;

    /**
     * Constructor for players
     *
     * @param name     the name of the player
     * @param location the location of the player
     * @param listener a player may be "listened to" for updates.
     */
    public Player(String name, Location location, PlayerListener listener) {
        this.name = name;
        this.location = location;

        // By default the player starts with nothing
        this.items = new GameItemLinkedList();

        this.listener = listener;

        WeaponDirector weaponDirector = new WeaponDirector();
        this.weapon = weaponDirector.setBuilder(WeaponBuilderType.FIST).build(null);
        initializeHandlers();

        // Reset the player's AP
        handleRequest("Reset AP", 0);
    }

    private void initializeHandlers() {
        DieHandler dieHandler = new DieHandler(this);
        GoldHandler goldHandler = new GoldHandler(this);
        DamageHandler damageHandler = new DamageHandler(this);
        ActionPointHandler actionPointHandler = new ActionPointHandler(this);
        HealthHandler healthHandler = new HealthHandler(this);

        dieHandler.setNextHandler(actionPointHandler);
        actionPointHandler.setNextHandler(goldHandler);
        goldHandler.setNextHandler(damageHandler);
        damageHandler.setNextHandler(healthHandler);

        healthHandler.setNextHandler(null);

        nextHandler = dieHandler;
    }

    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the player
     *
     * @param name The new name of the player
     * @throws CommandException
     */
    public void setName(String name) throws CommandException {
        if (!this.defaultName) {
            throw new CommandException("player's name already set");
        }

        this.name = name;
        this.defaultName = false;
    }

    @Override
    public void handleRequest(String indicator, int value) {
        nextHandler.handle(indicator, value);
    }

    /**
     * A player is dead if they have 0hp or less
     *
     * @return true if the player is dead, false otherwise
     */
    public boolean isDead() {
        return (this.hp <= 0);
    }

    /**
     * Returns the amount of AP the player has
     *
     * @return The amount of AP the player has
     */
    public int remainingAp() {
        return this.ap;
    }

    /**
     * Calculates the distances the player can see
     *
     * @return the distance visible to the player
     */
    public int lookDistance() {
        int lookDistance = defaultLookDistance;

        // Some items, e.g. the lantern, may increase the look distance
//        for (final GameItem item : this.items) {
//            lookDistance += item.lookDistanceIncrease();
//        }
        for (Iterator<GameItem> iterator = items.getIterator(); iterator.hasNext(); ) {
            lookDistance += iterator.next().lookDistanceIncrease();
        }

        return lookDistance;
    }

    /**
     * Returns true if a player can see a tile, based on the offset from the
     * player
     *
     * @param rowOffset
     * @param colOffset
     * @return true if the player can see the tile with the specified offset.
     */
    public boolean canSeeTile(int rowOffset, int colOffset) {
        // This is based on the Manhattan distance

        final boolean canSeeTile = (Math.abs(rowOffset) + Math.abs(colOffset) <= lookDistance() + 1);
        return canSeeTile;
    }

    /**
     * Check if the player already has a given item type (e.g. any sword, not
     * just "that" sword)
     *
     * @param item An instance of the item to compare with
     * @return true if the player has the item, false otherwise
     */
    public boolean hasItem(GameItem item) {
//        for (final GameItem itemToCompare : this.items) {
//            if (item.getClass() == itemToCompare.getClass()) {
//                return true;
//            }
//        }

        for (Iterator<GameItem> iterator = items.getIterator(); iterator.hasNext(); ) {
            GameItem itemToCompare = iterator.next();
            if (item.getClass() == itemToCompare.getClass()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Gives the item to the player. The player allows the item to act on the
     * player (through this), and keeps it in the item list if it is retainable.
     *
     * @param item the item to pick up
     */
    public void giveItem(GameItem item) {
        if (hasItem(item) && !item.getType().equals(ItemType.SWORD)) {
            throw new IllegalStateException("the player already has this item.");
        }

        // The item may do something to the player straight away
        item.processPickUp(this);

        // See if the item is retained by the player
        if (item.isRetainable()) {
            this.items.add(item);
        }
    }

    /**
     * @param message message to send to the listener
     */
    public void sendMessage(String message) {
        this.listener.update(message);
    }

    /**
     * Handle the start of a player's turn
     */
    public void startTurn() {
        handleRequest("Reset AP", 0);
        this.listener.startTurn();
    }

    /**
     * Handle the end of a player's turn
     */
    public void endTurn() {
        handleRequest("Zero AP", 0);
        this.listener.endTurn();
    }

    /**
     * Handle the player winning
     */
    public void win() {
        this.listener.win();
    }

    /**
     * @author Benjamin Dring
     * Causes the player to issue a look command
     */
    public void look() {
        this.listener.look();
    }

    public void clearItems() {
//        this.items.clear();
        this.items = new GameItemLinkedList();
    }

    @Override
    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    @Override
    public Weapon getWeapon() {
        return this.weapon;
    }
}