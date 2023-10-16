package test.unit;

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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class GameLogicUnitTests {
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
    User user1 = new User(gameLogic) {
        @Override
        public void update(String message) {
        }
    };
    User user2 = new User(gameLogic) {
        @Override
        public void update(String message) {
        }
    };

    Player player1 = gameLogic.getPlayer();
    Player player2 = gameLogic.getPlayerByIndex(1);
    int player1InitialHp = player1.getHp();
    int player2InitialHp = player2.getHp();
    int player1InitialAp = player1.getAp();
    int player2InitialAp = player2.getAp();

    public GameLogicUnitTests() throws FileNotFoundException, ParseException {
    }

    @BeforeEach
    void init(){
        gameLogic.setRand(Mockito.mock(Random.class));
        gameLogic.setPlayerWon(false);
        gameLogic.setGameOver(false);
        gameLogic.setPlayer(player1);
        player1.setAp(player1InitialAp);
        player1.clearItems();
        player1.setLocation(new Location(map.getMapHeight() / 2, map.getMapWidth() / 2));
        player1.setHp(player1InitialHp);
        player1.setGold(0);
        player2.clearItems();
        player2.setLocation(new Location(-1, -1)); //so that player2 does not hinder movement for player1 in some test cases
        player2.setHp(player2InitialHp);
        player2.setAp(player2InitialAp);
        player2.setGold(0);
        gameLogic.startGame();
    }


    @Test
    void clientMove_moveNorth_success() throws CommandException {
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        gameLogic.clientMove(CompassDirection.NORTH);
    }

    @Test
    void clientMove_moveEast_success() throws CommandException {
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        gameLogic.clientMove(CompassDirection.EAST);
    }

    @Test
    void clientMove_moveSouth_success() throws CommandException {
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        gameLogic.clientMove(CompassDirection.SOUTH);
    }

    @Test
    void clientMove_moveWest_success() throws CommandException {
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        gameLogic.clientMove(CompassDirection.WEST);
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
    }

    @Test
    void clientMove_fail_playerHasAlreadyWon(){
        gameLogic.setPlayerWon(true);
        assertThrows(CommandException.class, () -> gameLogic.clientMove(CompassDirection.NORTH));
    }

    @Test
    void clientMove_fail_playerHasNoAp(){
        player1.zeroAP();
        assertThrows(IllegalStateException.class, () -> gameLogic.clientMove(CompassDirection.NORTH));
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
        gameLogic.clientPickup();
    }

    @Test
    void clientPickup_pickupSword_success() throws CommandException {
        Mockito.doReturn(swordTile).when(map).getMapCell(any());
        gameLogic.clientPickup();
    }

    @Test
    void clientPickup_pickupLantern_success() throws CommandException {
        Mockito.doReturn(lanternTile).when(map).getMapCell(any());
        gameLogic.clientPickup();
    }

    @Test
    void clientPickup_pickupHealth_success() throws CommandException {
        Mockito.doReturn(healthTile).when(map).getMapCell(any());
        gameLogic.clientPickup();
    }

    @Test
    void clientPickup_pickupGold_success() throws CommandException {
        Mockito.doReturn(goldTile).when(map).getMapCell(any());
        gameLogic.clientPickup();
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
    void clientPickup_fail_playerDoesNotExist() {
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
    }

    @Test
    void clientAttack_attackNorthWithoutSword_success() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() - 1));
        gameLogic.clientAttack(CompassDirection.NORTH);
    }

    @Test
    void clientAttack_attackEastWithoutSword_success() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        player2.setLocation(new Location(player1.getLocation().getCol() + 1, player1.getLocation().getRow()));
        gameLogic.clientAttack(CompassDirection.EAST);
    }

    @Test
    void clientAttack_attackSouthWithoutSword_success() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() + 1));
        gameLogic.clientAttack(CompassDirection.SOUTH);
    }

    @Test
    void clientAttack_attackWestWithoutSword_success() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        player2.setLocation(new Location(player1.getLocation().getCol() - 1, player1.getLocation().getRow()));
        gameLogic.clientAttack(CompassDirection.WEST);
    }

    @Test
    void clientAttack_attackNorthWithSword_success() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        player1.giveItem(map.getAbstractFactory().createSword());
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() - 2));
        gameLogic.clientAttack(CompassDirection.NORTH);
    }

    @Test
    void clientAttack_attackEastWithSword_success() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        player1.giveItem(map.getAbstractFactory().createSword());
        player2.setLocation(new Location(player1.getLocation().getCol() + 2, player1.getLocation().getRow()));
        gameLogic.clientAttack(CompassDirection.EAST);
    }

    @Test
    void clientAttack_attackSouthWithSword_success() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        player1.giveItem(map.getAbstractFactory().createSword());
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() + 2));
        gameLogic.clientAttack(CompassDirection.SOUTH);
    }

    @Test
    void clientAttack_attackWestWithSword_success() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        player1.giveItem(map.getAbstractFactory().createSword());
        player2.setLocation(new Location(player1.getLocation().getCol() - 2, player1.getLocation().getRow()));
        gameLogic.clientAttack(CompassDirection.WEST);
    }

    @Test
    void clientAttack_attackNorthWithoutSwordVictimWithArmour_success() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        player2.giveItem(map.getAbstractFactory().createArmour());
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() - 1));
        gameLogic.clientAttack(CompassDirection.NORTH);
    }

    @Test
    void clientAttack_attackEastWithoutSwordVictimWithArmour_success() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        player2.giveItem(map.getAbstractFactory().createArmour());
        player2.setLocation(new Location(player1.getLocation().getCol() + 1, player1.getLocation().getRow()));
        gameLogic.clientAttack(CompassDirection.EAST);
    }

    @Test
    void clientAttack_attackSouthWithoutSwordVictimWithArmour_success() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        player2.giveItem(map.getAbstractFactory().createArmour());
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() + 1));
        gameLogic.clientAttack(CompassDirection.SOUTH);
    }

    @Test
    void clientAttack_attackWestWithoutSwordVictimWithArmour_success() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        player2.giveItem(map.getAbstractFactory().createArmour());
        player2.setLocation(new Location(player1.getLocation().getCol() - 1, player1.getLocation().getRow()));
        gameLogic.clientAttack(CompassDirection.WEST);
    }

    @Test
    void clientAttack_attackNorthWithSwordVictimWithArmour_success() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        player1.giveItem(map.getAbstractFactory().createSword());
        player2.giveItem(map.getAbstractFactory().createArmour());
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() - 2));
        gameLogic.clientAttack(CompassDirection.NORTH);
    }

    @Test
    void clientAttack_attackEastWithSwordVictimWithArmour_success() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        player1.giveItem(map.getAbstractFactory().createSword());
        player2.giveItem(map.getAbstractFactory().createArmour());
        player2.setLocation(new Location(player1.getLocation().getCol() + 2, player1.getLocation().getRow()));
        gameLogic.clientAttack(CompassDirection.EAST);
    }

    @Test
    void clientAttack_attackSouthWithSwordVictimWithArmour_success() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        player1.giveItem(map.getAbstractFactory().createSword());
        player2.giveItem(map.getAbstractFactory().createArmour());
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() + 2));
        gameLogic.clientAttack(CompassDirection.SOUTH);
    }

    @Test
    void clientAttack_attackWestWithSwordVictimWithArmour_success() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        player1.giveItem(map.getAbstractFactory().createSword());
        player2.giveItem(map.getAbstractFactory().createArmour());
        player2.setLocation(new Location(player1.getLocation().getCol() - 2, player1.getLocation().getRow()));
        gameLogic.clientAttack(CompassDirection.WEST);
    }

    @Test
    void clientAttack_attackNorth_success_victimDead() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        player2.setHp(1);
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() - 1));
        gameLogic.clientAttack(CompassDirection.NORTH);
    }

    @Test
    void clientAttack_attackEast_success_victimDead() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        player2.setHp(1);
        player2.setLocation(new Location(player1.getLocation().getCol() + 1, player1.getLocation().getRow()));
        gameLogic.clientAttack(CompassDirection.EAST);
    }

    @Test
    void clientAttack_attackSouth_success_victimDead() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        player2.setHp(1);
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() + 1));
        gameLogic.clientAttack(CompassDirection.SOUTH);
    }

    @Test
    void clientAttack_attackWest_success_victimDead() throws CommandException {
        Mockito.when(gameLogic.getRand().nextInt(5)).thenReturn(1);
        player2.setHp(1);
        player2.setLocation(new Location(player1.getLocation().getCol() - 1, player1.getLocation().getRow()));
        gameLogic.clientAttack(CompassDirection.WEST);
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
    }

    @Test
    void clientAttack_fail_playerAlreadyWon(){
        gameLogic.setPlayerWon(true);
        assertThrows(CommandException.class, () -> gameLogic.clientAttack(CompassDirection.NORTH));
    }

    @Test
    void clientAttack_fail_playerHasNoAp(){
        player1.zeroAP();
        assertThrows(IllegalStateException.class, () -> gameLogic.clientAttack(CompassDirection.NORTH));
    }

    @Test
    void clientGift_giftNorth_success() throws CommandException {
        Mockito.doReturn(2).when(map).getGoal();
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        player1.setGold(1);
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() - 1));
        gameLogic.clientGift(CompassDirection.NORTH);
    }

    @Test
    void clientGift_giftEast_success() throws CommandException {
        Mockito.doReturn(2).when(map).getGoal();
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        player1.setGold(1);
        player2.setLocation(new Location(player1.getLocation().getCol() + 1, player1.getLocation().getRow()));
        gameLogic.clientGift(CompassDirection.EAST);
    }

    @Test
    void clientGift_giftSouth_success() throws CommandException {
        Mockito.doReturn(2).when(map).getGoal();
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        player1.setGold(1);
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() + 1));
        gameLogic.clientGift(CompassDirection.SOUTH);
    }

    @Test
    void clientGift_giftWest_success() throws CommandException {
        Mockito.doReturn(2).when(map).getGoal();
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        player1.setGold(1);
        player2.setLocation(new Location(player1.getLocation().getCol() - 1, player1.getLocation().getRow()));
        gameLogic.clientGift(CompassDirection.WEST);
    }

    @Test
    void clientGift_giftNorth_success_receiverOnExitTileWithEnoughGold() throws CommandException {
        Mockito.doReturn(1).when(map).getGoal();
        Mockito.doReturn(exitTile).when(map).getMapCell(any());
        player1.setGold(1);
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() - 1));
        gameLogic.clientGift(CompassDirection.NORTH);
        assertTrue(gameLogic.isPlayerWon());
        assertTrue(gameLogic.isGameOver());
    }

    @Test
    void clientGift_giftEast_success_receiverOnExitTileWithEnoughGold() throws CommandException {
        Mockito.doReturn(1).when(map).getGoal();
        Mockito.doReturn(exitTile).when(map).getMapCell(any());
        player1.setGold(1);
        player2.setLocation(new Location(player1.getLocation().getCol() + 1, player1.getLocation().getRow()));
        gameLogic.clientGift(CompassDirection.EAST);
        assertTrue(gameLogic.isPlayerWon());
        assertTrue(gameLogic.isGameOver());
    }

    @Test
    void clientGift_giftSouth_success_receiverOnExitTileWithEnoughGold() throws CommandException {
        Mockito.doReturn(1).when(map).getGoal();
        Mockito.doReturn(exitTile).when(map).getMapCell(any());
        player1.setGold(1);
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() + 1));
        gameLogic.clientGift(CompassDirection.SOUTH);
        assertTrue(gameLogic.isPlayerWon());
        assertTrue(gameLogic.isGameOver());
    }

    @Test
    void clientGift_giftWest_success_receiverOnExitTileWithEnoughGold() throws CommandException {
        Mockito.doReturn(1).when(map).getGoal();
        Mockito.doReturn(exitTile).when(map).getMapCell(any());
        player1.setGold(1);
        player2.setLocation(new Location(player1.getLocation().getCol() - 1, player1.getLocation().getRow()));
        gameLogic.clientGift(CompassDirection.WEST);
        assertTrue(gameLogic.isPlayerWon());
        assertTrue(gameLogic.isGameOver());
    }

    @Test
    void clientGift_giftNorth_success_receiverOnExitTileWithNotEnoughGold() throws CommandException {
        Mockito.doReturn(2).when(map).getGoal();
        Mockito.doReturn(exitTile).when(map).getMapCell(any());
        player1.setGold(1);
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() - 1));
        gameLogic.clientGift(CompassDirection.NORTH);
        assertFalse(gameLogic.isPlayerWon());
        assertFalse(gameLogic.isGameOver());
    }

    @Test
    void clientGift_giftEast_success_receiverOnExitTileWithNotEnoughGold() throws CommandException {
        Mockito.doReturn(2).when(map).getGoal();
        Mockito.doReturn(exitTile).when(map).getMapCell(any());
        player1.setGold(1);
        player2.setLocation(new Location(player1.getLocation().getCol() + 1, player1.getLocation().getRow()));
        gameLogic.clientGift(CompassDirection.EAST);
        assertFalse(gameLogic.isPlayerWon());
        assertFalse(gameLogic.isGameOver());
    }

    @Test
    void clientGift_giftSouth_success_receiverOnExitTileWithNotEnoughGold() throws CommandException {
        Mockito.doReturn(2).when(map).getGoal();
        Mockito.doReturn(exitTile).when(map).getMapCell(any());
        player1.setGold(1);
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() + 1));
        gameLogic.clientGift(CompassDirection.SOUTH);
        assertFalse(gameLogic.isPlayerWon());
        assertFalse(gameLogic.isGameOver());
    }

    @Test
    void clientGift_giftWest_success_receiverOnExitTileWithNotEnoughGold() throws CommandException {
        Mockito.doReturn(2).when(map).getGoal();
        Mockito.doReturn(exitTile).when(map).getMapCell(any());
        player1.setGold(1);
        player2.setLocation(new Location(player1.getLocation().getCol() - 1, player1.getLocation().getRow()));
        gameLogic.clientGift(CompassDirection.WEST);
        assertFalse(gameLogic.isPlayerWon());
        assertFalse(gameLogic.isGameOver());
    }

    @Test
    void clientGift_giftNorth_fail_noPlayerThere(){
        assertThrows(CommandException.class, () -> gameLogic.clientGift(CompassDirection.NORTH));
    }

    @Test
    void clientGift_giftEast_fail_noPlayerThere(){
        assertThrows(CommandException.class, () -> gameLogic.clientGift(CompassDirection.EAST));
    }

    @Test
    void clientGift_giftSouth_fail_noPlayerThere(){
        assertThrows(CommandException.class, () -> gameLogic.clientGift(CompassDirection.SOUTH));
    }

    @Test
    void clientGift_giftWest_fail_noPlayerThere(){
        assertThrows(CommandException.class, () -> gameLogic.clientGift(CompassDirection.WEST));
    }

    @Test
    void clientGift_giftNorth_fail_playerHasNoGold(){
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() - 1));
        assertThrows(CommandException.class, () -> gameLogic.clientGift(CompassDirection.NORTH));
    }

    @Test
    void clientGift_giftEast_fail_playerHasNoGold(){
        player2.setLocation(new Location(player1.getLocation().getCol() + 1, player1.getLocation().getRow()));
        assertThrows(CommandException.class, () -> gameLogic.clientGift(CompassDirection.EAST));
    }

    @Test
    void clientGift_giftSouth_fail_playerHasNoGold(){
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() + 1));
        assertThrows(CommandException.class, () -> gameLogic.clientGift(CompassDirection.SOUTH));
    }

    @Test
    void clientGift_giftWest_fail_playerHasNoGold(){
        player2.setLocation(new Location(player1.getLocation().getCol() - 1, player1.getLocation().getRow()));
        assertThrows(CommandException.class, () -> gameLogic.clientGift(CompassDirection.NORTH));
    }
}
