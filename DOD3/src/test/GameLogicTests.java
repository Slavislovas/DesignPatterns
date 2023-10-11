package test;

import dod.User;
import dod.game.CommandException;
import dod.game.CompassDirection;
import dod.game.GameLogic;
import dod.game.Location;
import dod.game.Player;
import dod.game.Tile;
import dod.game.items.GameItem;
import dod.game.maps.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class GameLogicTests {
    @InjectMocks
    GameLogic gameLogic = new GameLogic("DOD3/defaultMap");
    @Spy
    Map map = gameLogic.getMap();
    Tile floorTile = new Tile(Tile.TileType.FLOOR);
    Tile exitTile = new Tile(Tile.TileType.EXIT);
    Tile armourTile = new Tile(map.getAbstractFactory().createArmour());
    Tile swordTile = new Tile(map.getAbstractFactory().createSword());
    Tile lanternTile = new Tile(map.getAbstractFactory().createLantern());
    Tile goldTile = new Tile(map.getAbstractFactory().createGold());
    Tile healthTile = new Tile(map.getAbstractFactory().createHealth());
    String firstUserReceivedMessage;
    String secondUserReceivedMessage;
    User user1 = new User(gameLogic) {
        @Override
        public void update(String message) {
            firstUserReceivedMessage = message;
        }
    };
    User user2 = new User(gameLogic) {
        @Override
        public void update(String message) {
            secondUserReceivedMessage = message;
        }
    };

    Player player1 = gameLogic.getPlayer();
    Player player2 = gameLogic.getPlayerByIndex(1);

    public GameLogicTests() throws FileNotFoundException, ParseException {
    }

    @BeforeEach
    void init(){
        gameLogic.setRand(Mockito.mock(Random.class));
        player1.addToAP(1);
        player1.clearItems();
        player1.setLocation(new Location(map.getMapHeight() / 2, map.getMapWidth() / 2));
        player2.clearItems();
        player2.setLocation(new Location(-1, -1)); //so that player2 does not hinder movement for player1 in some test cases
        player2.incrementHealth(2);
        gameLogic.startGame();
    }


    @Test
    void clientMove_moveNorth_success() throws CommandException {
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        Player player = gameLogic.getPlayer();
        Location prevLocation = player.getLocation();
        gameLogic.clientMove(CompassDirection.NORTH);
        assertEquals(prevLocation.getRow() - 1, gameLogic.getPlayer().getLocation().getRow());
    }

    @Test
    void clientMove_moveEast_success() throws CommandException {
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        Location prevLocation = player1.getLocation();
        gameLogic.clientMove(CompassDirection.EAST);
        assertEquals(prevLocation.getCol() + 1, gameLogic.getPlayer().getLocation().getCol());
    }

    @Test
    void clientMove_moveSouth_success() throws CommandException {
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        Location prevLocation = player1.getLocation();
        gameLogic.clientMove(CompassDirection.SOUTH);
        assertEquals(prevLocation.getRow() + 1, gameLogic.getPlayer().getLocation().getRow());
    }

    @Test
    void clientMove_moveWest_success() throws CommandException {
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        Location prevLocation = player1.getLocation();
        gameLogic.clientMove(CompassDirection.WEST);
        assertEquals(prevLocation.getCol() - 1, gameLogic.getPlayer().getLocation().getCol());
    }

    @Test
    void clientMove_moveNorth_fail_walkIntoWall(){
        Mockito.doReturn(false).when(map).insideMap(any());
        assertThrows(CommandException.class, () ->  gameLogic.clientMove(CompassDirection.NORTH));
    }

    @Test
    void clientMove_moveNorth_fail_walkIntoPlayer(){
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() - 1));
        assertThrows(CommandException.class, () -> gameLogic.clientMove(CompassDirection.NORTH));
    }

    @Test
    void clientMove_moveEast_fail_walkIntoWall(){
        Mockito.doReturn(false).when(map).insideMap(any());
        assertThrows(CommandException.class, () -> gameLogic.clientMove(CompassDirection.EAST));
    }

    @Test
    void clientMove_moveEast_fail_walkIntoPlayer(){
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        player2.setLocation(new Location(player1.getLocation().getCol() + 1, player1.getLocation().getRow()));
        assertThrows(CommandException.class, () -> gameLogic.clientMove(CompassDirection.EAST));
    }

    @Test
    void clientMove_moveSouth_fail_walkIntoWall(){
        Mockito.doReturn(false).when(map).insideMap(any());
        assertThrows(CommandException.class, () -> gameLogic.clientMove(CompassDirection.SOUTH));
    }

    @Test
    void clientMove_moveSouth_fail_walkIntoPlayer(){
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() + 1));
        assertThrows(CommandException.class, () -> gameLogic.clientMove(CompassDirection.SOUTH));
    }

    @Test
    void clientMove_moveWest_fail_walkIntoWall(){
        Mockito.doReturn(false).when(map).insideMap(any());
        assertThrows(CommandException.class, () -> gameLogic.clientMove(CompassDirection.WEST));
    }

    @Test
    void clientMove_moveWest_fail_walkIntoPlayer(){
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        player2.setLocation(new Location(player1.getLocation().getCol() - 1, player1.getLocation().getRow()));
        assertThrows(CommandException.class, () -> gameLogic.clientMove(CompassDirection.WEST));
    }

    @Test
    void clientMove_fail_playerDoesNotExist(){
        gameLogic.setPlayer(null);
        assertThrows(IllegalStateException.class, () -> gameLogic.clientMove(CompassDirection.NORTH));
        gameLogic.setPlayer(player1);
    }

    @Test
    void clientMove_fail_playerHasAlreadyWon(){
        gameLogic.setPlayerWon(true);
        assertThrows(CommandException.class, () -> gameLogic.clientMove(CompassDirection.NORTH));
        gameLogic.setPlayerWon(false);
    }

    @Test
    void clientMove_fail_playerHasNoAp(){
        int initialAp = player1.remainingAp();
        player1.zeroAP();
        assertThrows(IllegalStateException.class, () -> gameLogic.clientMove(CompassDirection.NORTH));
        player1.addToAP(initialAp);
    }

    @Test
    void clientMove_moveOnExitTile_playerWins() throws CommandException {
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(exitTile).when(map).getMapCell(any());
        assertFalse(gameLogic.isPlayerWon());
        player1.addGold(map.getGoal());
        gameLogic.clientMove(CompassDirection.NORTH);
        assertTrue(gameLogic.isPlayerWon());
    }

    @Test
    void clientMove_moveOnExitTile_playerDoesNotWin() throws CommandException {
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(exitTile).when(map).getMapCell(any());
        gameLogic.clientMove(CompassDirection.NORTH);
        assertFalse(gameLogic.isPlayerWon());
    }

    @Test
    void clientPickup_pickupArmour_success() throws CommandException {
        Mockito.doReturn(armourTile).when(map).getMapCell(any());
        GameItem armour = map.getAbstractFactory().createArmour();
        assertFalse(player1.hasItem(armour));
        gameLogic.clientPickup();
        assertTrue(player1.hasItem(armour));
    }

    @Test
    void clientPickup_pickupSword_success() throws CommandException {
        Mockito.doReturn(swordTile).when(map).getMapCell(any());
        GameItem sword = map.getAbstractFactory().createSword();
        assertFalse(player1.hasItem(sword));
        gameLogic.clientPickup();
        assertTrue(player1.hasItem(sword));
    }

    @Test
    void clientPickup_pickupLantern_success() throws CommandException {
        Mockito.doReturn(lanternTile).when(map).getMapCell(any());
        GameItem lantern = map.getAbstractFactory().createLantern();
        assertFalse(player1.hasItem(lantern));
        gameLogic.clientPickup();
        assertTrue(player1.hasItem(lantern));
    }

    @Test
    void clientPickup_pickupHealth_success() throws CommandException {
        Mockito.doReturn(healthTile).when(map).getMapCell(any());
        int initialHp = player1.getHp();
        player1.damage(1);
        assertEquals(initialHp - 1, player1.getHp());
        gameLogic.clientPickup();
        assertEquals(initialHp, player1.getHp());
    }

    @Test
    void clientPickup_pickupGold_success() throws CommandException {
        Mockito.doReturn(goldTile).when(map).getMapCell(any());
        int initialGold = player1.getGold();
        gameLogic.clientPickup();
        assertEquals(initialGold + 1, player1.getGold());
    }

    @Test
    void clientPickup_fail_noItemOnTile(){
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        assertThrows(CommandException.class, () -> gameLogic.clientPickup());
    }

    @Test
    void clientPickup_fail_playerAlreadyHasSword(){
        Mockito.doReturn(swordTile).when(map).getMapCell(any());
        GameItem sword = map.getAbstractFactory().createSword();
        player1.giveItem(sword);
        assertThrows(CommandException.class, () -> gameLogic.clientPickup());
    }

    @Test
    void clientPickup_fail_playerAlreadyHasArmour(){
        Mockito.doReturn(armourTile).when(map).getMapCell(any());
        GameItem armour = map.getAbstractFactory().createArmour();
        player1.giveItem(armour);
        assertThrows(CommandException.class, () -> gameLogic.clientPickup());
    }

    @Test
    void clientPickup_fail_playerAlreadyHasLantern(){
        Mockito.doReturn(lanternTile).when(map).getMapCell(any());
        GameItem lantern = map.getAbstractFactory().createLantern();
        player1.giveItem(lantern);
        assertThrows(CommandException.class, () -> gameLogic.clientPickup());
    }

    @Test
    void clientPickup_fail_playerDoesNotExist() throws CommandException {
        gameLogic.setPlayer(null);
        assertThrows(IllegalStateException.class, () -> gameLogic.clientPickup());
        gameLogic.setPlayer(player1);
    }

    @Test
    void clientPickup_fail_playerAlreadyWon(){
        gameLogic.setPlayerWon(true);
        assertThrows(CommandException.class, () -> gameLogic.clientPickup());
        gameLogic.setPlayerWon(false);
    }

    @Test
    void clientPickup_fail_playerHasNoAp(){
        int initialAp = player1.remainingAp();
        player1.zeroAP();
        assertThrows(IllegalStateException.class, () -> gameLogic.clientPickup());
        player1.addToAP(initialAp);
    }

    @Test
    void clientAttack_attackNorthWithoutSword_success() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        int initialPlayer2Health = player2.getHp();
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() - 1));
        gameLogic.clientAttack(CompassDirection.NORTH);
        assertEquals(initialPlayer2Health - 1, player2.getHp());
        assertEquals(0, player1.remainingAp());
    }

    @Test
    void clientAttack_attackEastWithoutSword_success() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        int initialPlayer2Health = player2.getHp();
        player2.setLocation(new Location(player1.getLocation().getCol() + 1, player1.getLocation().getRow()));
        gameLogic.clientAttack(CompassDirection.EAST);
        assertEquals(initialPlayer2Health - 1, player2.getHp());
        assertEquals(0, player1.remainingAp());
    }

    @Test
    void clientAttack_attackSouthWithoutSword_success() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        int initialPlayer2Health = player2.getHp();
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() + 1));
        gameLogic.clientAttack(CompassDirection.SOUTH);
        assertEquals(initialPlayer2Health - 1, player2.getHp());
        assertEquals(0, player1.remainingAp());
    }

    @Test
    void clientAttack_attackWestWithoutSword_success() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        int initialPlayer2Health = player2.getHp();
        player2.setLocation(new Location(player1.getLocation().getCol() - 1, player1.getLocation().getRow()));
        gameLogic.clientAttack(CompassDirection.WEST);
        assertEquals(initialPlayer2Health - 1, player2.getHp());
        assertEquals(0, player1.remainingAp());
    }

    @Test
    void clientAttack_attackNorthWithSword_success() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        int initialPlayer2Health = player2.getHp();
        player1.giveItem(map.getAbstractFactory().createSword());
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() - 2));
        gameLogic.clientAttack(CompassDirection.NORTH);
        assertEquals(initialPlayer2Health - 2, player2.getHp());
        assertEquals(0, player1.remainingAp());
    }

    @Test
    void clientAttack_attackEastWithSword_success() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        int initialPlayer2Health = player2.getHp();
        player1.giveItem(map.getAbstractFactory().createSword());
        player2.setLocation(new Location(player1.getLocation().getCol() + 2, player1.getLocation().getRow()));
        gameLogic.clientAttack(CompassDirection.EAST);
        assertEquals(initialPlayer2Health - 2, player2.getHp());
        assertEquals(0, player1.remainingAp());
    }

    @Test
    void clientAttack_attackSouthWithSword_success() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        int initialPlayer2Health = player2.getHp();
        player1.giveItem(map.getAbstractFactory().createSword());
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() + 2));
        gameLogic.clientAttack(CompassDirection.SOUTH);
        assertEquals(initialPlayer2Health - 2, player2.getHp());
        assertEquals(0, player1.remainingAp());
    }

    @Test
    void clientAttack_attackWestWithSword_success() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        int initialPlayer2Health = player2.getHp();
        player1.giveItem(map.getAbstractFactory().createSword());
        player2.setLocation(new Location(player1.getLocation().getCol() - 2, player1.getLocation().getRow()));
        gameLogic.clientAttack(CompassDirection.WEST);
        assertEquals(initialPlayer2Health - 2, player2.getHp());
        assertEquals(0, player1.remainingAp());
    }

    @Test
    void clientAttack_attackNorthWithoutSwordVictimWithArmour_success() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        int initialPlayer2Health = player2.getHp();
        player2.giveItem(map.getAbstractFactory().createArmour());
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() - 1));
        gameLogic.clientAttack(CompassDirection.NORTH);
        assertEquals(initialPlayer2Health, player2.getHp());
        assertEquals(0, player1.remainingAp());
    }

    @Test
    void clientAttack_attackEastWithoutSwordVictimWithArmour_success() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        int initialPlayer2Health = player2.getHp();
        player2.giveItem(map.getAbstractFactory().createArmour());
        player2.setLocation(new Location(player1.getLocation().getCol() + 1, player1.getLocation().getRow()));
        gameLogic.clientAttack(CompassDirection.EAST);
        assertEquals(initialPlayer2Health, player2.getHp());
        assertEquals(0, player1.remainingAp());
    }

    @Test
    void clientAttack_attackSouthWithoutSwordVictimWithArmour_success() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        int initialPlayer2Health = player2.getHp();
        player2.giveItem(map.getAbstractFactory().createArmour());
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() + 1));
        gameLogic.clientAttack(CompassDirection.SOUTH);
        assertEquals(initialPlayer2Health, player2.getHp());
        assertEquals(0, player1.remainingAp());
    }

    @Test
    void clientAttack_attackWestWithoutSwordVictimWithArmour_success() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        int initialPlayer2Health = player2.getHp();
        player2.giveItem(map.getAbstractFactory().createArmour());
        player2.setLocation(new Location(player1.getLocation().getCol() - 1, player1.getLocation().getRow()));
        gameLogic.clientAttack(CompassDirection.WEST);
        assertEquals(initialPlayer2Health, player2.getHp());
        assertEquals(0, player1.remainingAp());
    }

    @Test
    void clientAttack_attackNorthWithSwordVictimWithArmour_success() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        int initialPlayer2Health = player2.getHp();
        player1.giveItem(map.getAbstractFactory().createSword());
        player2.giveItem(map.getAbstractFactory().createArmour());
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() - 2));
        gameLogic.clientAttack(CompassDirection.NORTH);
        assertEquals(initialPlayer2Health - 1, player2.getHp());
        assertEquals(0, player1.remainingAp());
    }

    @Test
    void clientAttack_attackEastWithSwordVictimWithArmour_success() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        int initialPlayer2Health = player2.getHp();
        player1.giveItem(map.getAbstractFactory().createSword());
        player2.giveItem(map.getAbstractFactory().createArmour());
        player2.setLocation(new Location(player1.getLocation().getCol() + 2, player1.getLocation().getRow()));
        gameLogic.clientAttack(CompassDirection.EAST);
        assertEquals(initialPlayer2Health - 1, player2.getHp());
        assertEquals(0, player1.remainingAp());
    }

    @Test
    void clientAttack_attackSouthWithSwordVictimWithArmour_success() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        int initialPlayer2Health = player2.getHp();
        player1.giveItem(map.getAbstractFactory().createSword());
        player2.giveItem(map.getAbstractFactory().createArmour());
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() + 2));
        gameLogic.clientAttack(CompassDirection.SOUTH);
        assertEquals(initialPlayer2Health - 1, player2.getHp());
        assertEquals(0, player1.remainingAp());
    }

    @Test
    void clientAttack_attackWestWithSwordVictimWithArmour_success() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        int initialPlayer2Health = player2.getHp();
        player1.giveItem(map.getAbstractFactory().createSword());
        player2.giveItem(map.getAbstractFactory().createArmour());
        player2.setLocation(new Location(player1.getLocation().getCol() - 2, player1.getLocation().getRow()));
        gameLogic.clientAttack(CompassDirection.WEST);
        assertEquals(initialPlayer2Health - 1, player2.getHp());
        assertEquals(0, player1.remainingAp());
    }

    @Test
    void clientAttack_attackNorth_success_victimDead() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        int initialPlayer2Health = player2.getHp();
        player2.damage(initialPlayer2Health - 1);
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() - 1));
        assertFalse(player2.isDead());
        gameLogic.clientAttack(CompassDirection.NORTH);
        assertTrue(player2.isDead());
        player2.incrementHealth(initialPlayer2Health);
    }

    @Test
    void clientAttack_attackEast_success_victimDead() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        int initialPlayer2Health = player2.getHp();
        player2.damage(initialPlayer2Health - 1);
        player2.setLocation(new Location(player1.getLocation().getCol() + 1, player1.getLocation().getRow()));
        assertFalse(player2.isDead());
        gameLogic.clientAttack(CompassDirection.EAST);
        assertTrue(player2.isDead());
        player2.incrementHealth(initialPlayer2Health);
    }

    @Test
    void clientAttack_attackSouth_success_victimDead() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        int initialPlayer2Health = player2.getHp();
        player2.damage(initialPlayer2Health - 1);
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() + 1));
        assertFalse(player2.isDead());
        gameLogic.clientAttack(CompassDirection.SOUTH);
        assertTrue(player2.isDead());
        player2.incrementHealth(initialPlayer2Health);
    }

    @Test
    void clientAttack_attackWest_success_victimDead() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        int initialPlayer2Health = player2.getHp();
        player2.damage(initialPlayer2Health - 1);
        player2.setLocation(new Location(player1.getLocation().getCol() - 1, player1.getLocation().getRow()));
        assertFalse(player2.isDead());
        gameLogic.clientAttack(CompassDirection.WEST);
        assertTrue(player2.isDead());
        player2.incrementHealth(initialPlayer2Health);
    }

    @Test
    void clientAttack_attackNorth_fail_noPlayerThere() {
        assertThrows(CommandException.class, () -> gameLogic.clientAttack(CompassDirection.NORTH));
    }

    @Test
    void clientAttack_attackWest_fail_noPlayerThere() {
        assertThrows(CommandException.class, () -> gameLogic.clientAttack(CompassDirection.WEST));
    }

    @Test
    void clientAttack_attackSouth_fail_noPlayerThere() {
        assertThrows(CommandException.class, () -> gameLogic.clientAttack(CompassDirection.SOUTH));
    }

    @Test
    void clientAttack_attackEast_fail_noPlayerThere() {
        assertThrows(CommandException.class, () -> gameLogic.clientAttack(CompassDirection.EAST));
    }

    @Test
    void clientAttack_attackNorth_fail_miss(){
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(4);
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() - 1));
        assertThrows(CommandException.class, () -> gameLogic.clientAttack(CompassDirection.NORTH));
    }

    @Test
    void clientAttack_attackEast_fail_miss(){
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(4);
        player2.setLocation(new Location(player1.getLocation().getCol() + 1, player1.getLocation().getRow()));
        assertThrows(CommandException.class, () -> gameLogic.clientAttack(CompassDirection.EAST));
    }

    @Test
    void clientAttack_attackSouth_fail_miss(){
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(4);
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() + 1));
        assertThrows(CommandException.class, () -> gameLogic.clientAttack(CompassDirection.SOUTH));
    }

    @Test
    void clientAttack_attackWest_fail_miss(){
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(4);
        player2.setLocation(new Location(player1.getLocation().getCol() - 1, player1.getLocation().getRow()));
        assertThrows(CommandException.class, () -> gameLogic.clientAttack(CompassDirection.WEST));
    }

    @Test
    void clientAttack_fail_playerDoesNotExist(){
        gameLogic.setPlayer(null);
        assertThrows(IllegalStateException.class, () -> gameLogic.clientAttack(CompassDirection.NORTH));
        gameLogic.setPlayer(player1);
    }

    @Test
    void clientAttack_fail_playerAlreadyWon(){
        gameLogic.setPlayerWon(true);
        assertThrows(CommandException.class, () -> gameLogic.clientAttack(CompassDirection.NORTH));
        gameLogic.setPlayerWon(false);
    }

    @Test
    void clientAttack_fail_playerHasNoAp(){
        int initialAp = player1.remainingAp();
        player1.zeroAP();
        assertThrows(IllegalStateException.class, () -> gameLogic.clientAttack(CompassDirection.NORTH));
        player1.addToAP(initialAp);
    }
}
