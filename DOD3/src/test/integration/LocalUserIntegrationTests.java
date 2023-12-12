package test.integration;

import dod.Communicator.LocalGameCommunicator;
import dod.GUI.HumanPlayerGUI;
import dod.LocalUser;
import dod.builder.WeaponType;
import dod.game.CommandException;
import dod.game.GameLogic;
import dod.game.Location;
import dod.game.Player;
import dod.game.Tile;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
public class LocalUserIntegrationTests {
    @InjectMocks
    GameLogic gameLogic = new GameLogic("DOD3/defaultMap");
    @Spy
    Map map = gameLogic.getMap();
    Tile wallTile = new Tile(Tile.TileType.WALL);
    Tile floorTile = new Tile(Tile.TileType.FLOOR);
    Tile exitTile = new Tile(Tile.TileType.EXIT);
    Tile armourTile = new Tile(map.getAbstractFactory().createArmour(map.get_mediator()));
    Tile swordTile = new Tile(map.getAbstractFactory().createSword(map.get_mediator()));
    Tile lanternTile = new Tile(map.getAbstractFactory().createLantern(map.get_mediator()));
    Tile goldTile = new Tile(map.getAbstractFactory().createGold(map.get_mediator()));
    Tile healthTile = new Tile(map.getAbstractFactory().createHealth(map.get_mediator()));
    LocalGameCommunicator localGameCommunicator1 = new LocalGameCommunicator(gameLogic);
    LocalGameCommunicator localGameCommunicator2 = new LocalGameCommunicator(gameLogic);
    LocalUser localUser1 = localGameCommunicator1.getUser();
    Player player1 = gameLogic.getPlayer();
    Player player2 = gameLogic.getPlayerByIndex(1);
    Location initialPlayerLocation;
    double initialPlayer2Hp;
    int initialPlayer2Gold;
    HumanPlayerGUI humanPlayerGUI = new HumanPlayerGUI(localGameCommunicator1, player1.getName(), false);

    Random random;

    public LocalUserIntegrationTests() throws FileNotFoundException, ParseException {
    }

    @BeforeEach
    void init() throws CommandException {
        player1.setLocation(new Location(map.getMapWidth() / 2, map.getMapHeight() / 2));
        player1.handleRequest("Health", 10);
        player1.setGold(0);
        player1.clearItems();
        gameLogic.setTurnSwitch(false);
        player2.setLocation(new Location(-1, -1)); //so that player2 does not hinder movement of player1
        player2.handleRequest("Health", 10);
        player2.setGold(0);
        initialPlayer2Gold = player2.getGold();
        initialPlayer2Hp = player2.getHp();
        initialPlayerLocation = player1.getLocation();
        humanPlayerGUI.getMessageFeedText().setTextAreaText("");
        gameLogic.setPlayer(player1);
        gameLogic.startGame();
        player1.handleRequest("Add AP", 100);
        gameLogic.setRand(Mockito.mock(Random.class));
        random = gameLogic.getRand();
    }

    @Test
    void sendCommand_moveNorth_success(){
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        int initialPlayerAp = player1.remainingAp();
        localUser1.sendCommand("MOVE N");
        assertEquals(initialPlayerLocation.getRow() - 1, player1.getLocation().getRow());
        assertEquals(initialPlayerAp - 1, player1.remainingAp());
    }

    @Test
    void sendCommand_moveEast_success(){
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        int initialPlayerAp = player1.remainingAp();
        localUser1.sendCommand("MOVE E");
        assertEquals(initialPlayerLocation.getCol() + 1, player1.getLocation().getCol());
        assertEquals(initialPlayerAp - 1, player1.remainingAp());
    }

    @Test
    void sendCommand_moveSouth_success(){
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        int initialPlayerAp = player1.remainingAp();
        localUser1.sendCommand("MOVE S");
        assertEquals(initialPlayerLocation.getRow() + 1, player1.getLocation().getRow());
        assertEquals(initialPlayerAp - 1, player1.remainingAp());
    }

    @Test
    void sendCommand_moveWest_success(){
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        int initialPlayerAp = player1.remainingAp();
        localUser1.sendCommand("MOVE W");
        assertEquals(initialPlayerLocation.getCol() - 1, player1.getLocation().getCol());
        assertEquals(initialPlayerAp - 1, player1.remainingAp());
    }

    @Test
    void sendCommand_moveOnExitTileWithEnoughGold_userWins_success(){
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(exitTile).when(map).getMapCell(any());
        int initialPlayerAp = player1.remainingAp();
        player1.setGold(map.getGoal());
        assertFalse(localUser1.isDidUserWin());
        assertFalse(gameLogic.isPlayerWon());
        localUser1.sendCommand("MOVE W");
        assertEquals(initialPlayerLocation.getCol() - 1, player1.getLocation().getCol());
        assertEquals(initialPlayerAp - 1, player1.remainingAp());
        assertTrue(localUser1.isDidUserWin());
        assertTrue(gameLogic.isPlayerWon());
        assertTrue(humanPlayerGUI.getMessageFeedText().toString().contains("Game has finished!"));
    }

    @Test
    void sendCommand_moveOnExitTileWithNotEnoughGold_userDoesNotWin_success(){
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(exitTile).when(map).getMapCell(any());
        int initialPlayerAp = player1.remainingAp();
        assertFalse(localUser1.isDidUserWin());
        assertFalse(gameLogic.isPlayerWon());
        localUser1.sendCommand("MOVE W");
        assertEquals(initialPlayerLocation.getCol() - 1, player1.getLocation().getCol());
        assertEquals(initialPlayerAp - 1, player1.remainingAp());
        assertFalse(localUser1.isDidUserWin());
        assertFalse(gameLogic.isPlayerWon());
    }

    @Test
    void sendCommand_move_fail_invalidDirection(){
        localUser1.sendCommand("MOVE ASDFASDASD");
        assertTrue(humanPlayerGUI.getMessageFeedText().toString().contains("invalid direction"));
        assertEquals(initialPlayerLocation.getCol(), player1.getLocation().getCol());
        assertEquals(initialPlayerLocation.getRow(), player1.getLocation().getRow());
    }

    @Test
    void sendCommand_moveNorth_fail_moveIntoWall(){
        Mockito.doReturn(false).when(map).insideMap(any());
        int initialPlayerAp = player1.remainingAp();
        localUser1.sendCommand("MOVE N");
        assertEquals(initialPlayerLocation.getRow(), player1.getLocation().getRow());
        assertEquals(initialPlayerAp , player1.remainingAp());
        assertTrue(humanPlayerGUI.getMessageFeedText().toString().contains("Server: FAIL can't move into a wall"));
    }

    @Test
    void sendCommand_moveEast_fail_moveIntoWall(){
        Mockito.doReturn(false).when(map).insideMap(any());
        int initialPlayerAp = player1.remainingAp();
        localUser1.sendCommand("MOVE E");
        assertEquals(initialPlayerLocation.getCol(), player1.getLocation().getCol());
        assertEquals(initialPlayerAp, player1.remainingAp());
        assertTrue(humanPlayerGUI.getMessageFeedText().toString().contains("Server: FAIL can't move into a wall"));
    }

    @Test
    void sendCommand_moveSouth_fail_moveIntoWall(){
        Mockito.doReturn(false).when(map).insideMap(any());
        int initialPlayerAp = player1.remainingAp();
        localUser1.sendCommand("MOVE S");
        assertEquals(initialPlayerLocation.getRow(), player1.getLocation().getRow());
        assertEquals(initialPlayerAp, player1.remainingAp());
        assertTrue(humanPlayerGUI.getMessageFeedText().toString().contains("Server: FAIL can't move into a wall"));
    }

    @Test
    void sendCommand_moveWest_fail_moveIntoWall(){
        Mockito.doReturn(false).when(map).insideMap(any());
        int initialPlayerAp = player1.remainingAp();
        localUser1.sendCommand("MOVE W");
        assertEquals(initialPlayerLocation.getCol(), player1.getLocation().getCol());
        assertEquals(initialPlayerAp, player1.remainingAp());
        assertTrue(humanPlayerGUI.getMessageFeedText().toString().contains("Server: FAIL can't move into a wall"));
    }

    @Test
    void sendCommand_moveNorth_fail_moveIntoPlayer(){
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() - 1));
        int initialPlayerAp = player1.remainingAp();
        localUser1.sendCommand("MOVE N");
        assertEquals(initialPlayerLocation.getRow(), player1.getLocation().getRow());
        assertEquals(initialPlayerAp, player1.remainingAp());
        assertTrue(humanPlayerGUI.getMessageFeedText().toString().contains("Server: FAIL can't move into a player"));
    }

    @Test
    void sendCommand_moveEast_fail_moveIntoPlayer(){
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        player2.setLocation(new Location(player1.getLocation().getCol() + 1, player1.getLocation().getRow()));
        int initialPlayerAp = player1.remainingAp();
        localUser1.sendCommand("MOVE E");
        assertEquals(initialPlayerLocation.getCol(), player1.getLocation().getCol());
        assertEquals(initialPlayerAp, player1.remainingAp());
        assertTrue(humanPlayerGUI.getMessageFeedText().toString().contains("Server: FAIL can't move into a player"));
    }

    @Test
    void sendCommand_moveSouth_fail_moveIntoPlayer(){
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() + 1));
        int initialPlayerAp = player1.remainingAp();
        localUser1.sendCommand("MOVE S");
        assertEquals(initialPlayerLocation.getRow(), player1.getLocation().getRow());
        assertEquals(initialPlayerAp, player1.remainingAp());
        assertTrue(humanPlayerGUI.getMessageFeedText().toString().contains("Server: FAIL can't move into a player"));
    }

    @Test
    void sendCommand_moveWest_fail_moveIntoPlayer(){
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        player2.setLocation(new Location(player1.getLocation().getCol() - 1, player1.getLocation().getRow()));
        int initialPlayerAp = player1.remainingAp();
        localUser1.sendCommand("MOVE W");
        assertEquals(initialPlayerLocation.getCol(), player1.getLocation().getCol());
        assertEquals(initialPlayerAp, player1.remainingAp());
        assertTrue(humanPlayerGUI.getMessageFeedText().toString().contains("Server: FAIL can't move into a player"));
    }

    @Test
    void sendCommand_move_fail_noDirectionSpecified(){
        int initialPlayerAp = player1.remainingAp();
        localUser1.sendCommand("MOVE");
        assertEquals(initialPlayerLocation.getCol(), player1.getLocation().getCol());
        assertEquals(initialPlayerAp, player1.remainingAp());
        assertTrue(humanPlayerGUI.getMessageFeedText().toString().contains("Server: FAIL MOVE needs a direction"));
    }

    @Test
    void sendCommand_hello_fail_noArgument(){
        localUser1.sendCommand("HELLO");
        assertTrue(humanPlayerGUI.getMessageFeedText().toString().contains("Server: FAIL HELLO needs an argument"));
    }

    @Test
    void sendCommand_look_fail_argumentPassed(){
        localUser1.sendCommand("LOOK TEST");
        assertTrue(humanPlayerGUI.getMessageFeedText().toString().contains("Server: FAIL LOOK does not take an argument"));
    }

    @Test
    void sendCommand_die_success(){
        assertNotEquals(0, player1.getHp());
        localUser1.sendCommand("DIE");
        assertEquals(0, player1.getHp());
    }

    @Test
    void sendCommand_shout_success(){
        localUser1.sendCommand("SHOUT PLEASELETTTHISTESTWORK");
        assertTrue(humanPlayerGUI.getMessageFeedText().toString().contains("PLEASELETTTHISTESTWORK"));
    }

    @Test
    void sendCommand_shout_fail_noArgument(){
        localUser1.sendCommand("SHOUT");
        assertTrue(humanPlayerGUI.getMessageFeedText().toString().contains("Server: FAIL need something to shout"));
    }

    @Test
    void sendCommand_pickup_fail_gameNotStarted(){
        gameLogic.setGameStarted(false);
        localUser1.sendCommand("PICKUP");
        assertTrue(humanPlayerGUI.getMessageFeedText().toString().contains("Server: FAIL Game has not started"));
    }

    @Test
    void sendCommand_pickup_fail_notPlayerTurn(){
        gameLogic.setTurnSwitch(true);
        localUser1.sendCommand("PICKUP");
        assertTrue(humanPlayerGUI.getMessageFeedText().toString().contains("Server: FAIL It is not your turn"));
    }

    @Test
    void sendCommand_pickup_fail_argumentPassed(){
        localUser1.sendCommand("PICKUP TEST");
        assertTrue(humanPlayerGUI.getMessageFeedText().toString().contains("Server: FAIL PICKUP does not take an argument"));
    }

    @Test
    void sendCommand_pickup_fail_nothingToPickup(){
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        localUser1.sendCommand("PICKUP");
        assertTrue(humanPlayerGUI.getMessageFeedText().toString().contains("nothing to pick up"));
    }

    @Test
    void sendCommand_pickup_fail_alreadyHasItem(){
        Mockito.doReturn(swordTile).when(map).getMapCell(any());
        player1.giveItem(map.getAbstractFactory().createSword(map.get_mediator()));
        localUser1.sendCommand("PICKUP");
        assertTrue(humanPlayerGUI.getMessageFeedText().toString().contains("already have item"));
    }

    @Test
    void sendCommand_pickup_success_pickupSword(){
        Mockito.doReturn(swordTile).when(map).getMapCell(any());
        assertFalse(player1.hasItem(map.getAbstractFactory().createSword(map.get_mediator())));
        assertNotEquals(player1.getWeapon().getType(), WeaponType.SWORD);
        localUser1.sendCommand("PICKUP");
        assertTrue(player1.hasItem(map.getAbstractFactory().createSword(map.get_mediator())));
        assertEquals(player1.getWeapon().getType(), WeaponType.SWORD);
    }

    @Test
    void sendCommand_pickup_success_pickupArmour(){
        Mockito.doReturn(armourTile).when(map).getMapCell(any());
        assertFalse(player1.hasItem(map.getAbstractFactory().createArmour(map.get_mediator())));
        localUser1.sendCommand("PICKUP");
        assertTrue(player1.hasItem(map.getAbstractFactory().createArmour(map.get_mediator())));
    }

    @Test
    void sendCommand_attack_fail_noArgument(){
        localUser1.sendCommand("ATTACK");
        assertTrue(humanPlayerGUI.getMessageFeedText().toString().contains("Server: FAIL ATTACK needs a direction"));
    }

    @Test
    void sendCommand_attack_fail_missed(){
        Mockito.when(random.nextInt(anyInt())).thenReturn(4);
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() - 1));
        localUser1.sendCommand("ATTACK N");
        assertTrue(humanPlayerGUI.getMessageFeedText().toString().contains("You Missed the target"));
    }

    @Test
    void sendCommand_attack_fail_noPlayerThere(){
        localUser1.sendCommand("ATTACK N");
        assertTrue(humanPlayerGUI.getMessageFeedText().toString().contains("There is no player there"));
    }

    @Test
    void sendCommand_attack_success(){
        Mockito.when(random.nextInt(anyInt())).thenReturn(2);
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() - 1));
        assertEquals(initialPlayer2Hp, player2.getHp());
        localUser1.sendCommand("ATTACK N");
        assertEquals(initialPlayer2Hp - 1, player2.getHp());
    }

    @Test
    void sendCommand_attack_success_victimDead(){
        Mockito.when(random.nextInt(anyInt())).thenReturn(2);
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() - 1));
        player2.setHp(1);
        localUser1.sendCommand("ATTACK N");
        assertEquals(0, player2.getHp());
        assertTrue(humanPlayerGUI.getMessageFeedText().toString().contains("The player has died"));
    }


    @Test
    void sendCommand_gift_fail_noArgument(){
        localUser1.sendCommand("GIFT");
        assertTrue(humanPlayerGUI.getMessageFeedText().toString().contains("Server: FAIL GIFT needs a direction"));
    }

    @Test
    void sendCommand_gift_fail_noPlayerThere(){
        localUser1.sendCommand("GIFT N");
        assertTrue(humanPlayerGUI.getMessageFeedText().toString().contains("There is no player there."));
    }

    @Test
    void sendCommand_gift_fail_noGold(){
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() - 1));
        localUser1.sendCommand("GIFT N");
        assertTrue(humanPlayerGUI.getMessageFeedText().toString().contains("You Have no Gold"));
    }

    @Test
    void sendCommand_gift_success(){
        assertEquals(initialPlayer2Gold, player2.getGold());
        player1.setGold(1);
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() - 1));
        localUser1.sendCommand("GIFT N");
        assertEquals(initialPlayer2Gold + 1, player2.getGold());
        assertEquals(0, player1.getGold());
    }

    @Test
    void sendCommand_gift_success_receiverWins(){
        Mockito.doReturn(0).when(map).getGoal();
        Mockito.doReturn(exitTile).when(map).getMapCell(any());
        assertEquals(initialPlayer2Gold, player2.getGold());
        player1.setGold(1);
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() - 1));
        localUser1.sendCommand("GIFT N");
        assertEquals(initialPlayer2Gold + 1, player2.getGold());
        assertEquals(0, player1.getGold());
        assertTrue(gameLogic.isGameOver());
    }

    @Test
    void sendCommand_endTurn_success(){
        localUser1.sendCommand("ENDTURN");
        assertNotEquals(player1, gameLogic.getPlayer());
    }

    @Test
    void sendCommand_setPlayerPos_fail_noArgument(){
        localUser1.sendCommand("SETPLAYERPOS");
        assertTrue(humanPlayerGUI.getMessageFeedText().toString().contains("need a position"));
        assertEquals(initialPlayerLocation.getCol(), player1.getLocation().getCol());
        assertEquals(initialPlayerLocation.getRow(), player1.getLocation().getRow());
    }

    @Test
    void sendCommand_setPlayerPos_fail_invalidPosition(){
        localUser1.sendCommand("SETPLAYERPOS -100 -100");
        assertTrue(humanPlayerGUI.getMessageFeedText().toString().contains("invalid position"));
        assertEquals(initialPlayerLocation.getCol(), player1.getLocation().getCol());
        assertEquals(initialPlayerLocation.getRow(), player1.getLocation().getRow());
    }

    @Test
    void sendCommand_setPlayerPos_fail_notWalkableTile(){
        Mockito.doReturn(wallTile).when(map).getMapCell(any());
        localUser1.sendCommand("SETPLAYERPOS" + " " + (initialPlayerLocation.getCol() + 1) + " " + (initialPlayerLocation.getRow() + 1));
        assertTrue(humanPlayerGUI.getMessageFeedText().toString().contains("cannot walk on this tile"));
        assertEquals(initialPlayerLocation.getCol(), player1.getLocation().getCol());
        assertEquals(initialPlayerLocation.getRow(), player1.getLocation().getRow());
    }

    @Test
    void sendCommand_setPlayerPos_fail_onlyOneCoordinateSent(){
        localUser1.sendCommand("SETPLAYERPOS" + " " + (initialPlayerLocation.getCol() + 1));
        assertTrue(humanPlayerGUI.getMessageFeedText().toString().contains("need two co-ordinates"));
        assertEquals(initialPlayerLocation.getCol(), player1.getLocation().getCol());
        assertEquals(initialPlayerLocation.getRow(), player1.getLocation().getRow());
    }

    @Test
    void sendCommand_setPlayerPos_fail_coordinatesNotIntegers(){
        localUser1.sendCommand("SETPLAYERPOS AS DD");
        assertTrue(humanPlayerGUI.getMessageFeedText().toString().contains("co-ordinates must be integers"));
        assertEquals(initialPlayerLocation.getCol(), player1.getLocation().getCol());
        assertEquals(initialPlayerLocation.getRow(), player1.getLocation().getRow());
    }

    @Test
    void sendCommand_setPlayerPos_success(){
        localUser1.sendCommand("SETPLAYERPOS" + " " + (initialPlayerLocation.getCol() + 1) + " " + (initialPlayerLocation.getRow() + 1));
        assertEquals(initialPlayerLocation.getCol() + 1, player1.getLocation().getCol());
        assertEquals(initialPlayerLocation.getRow() + 1, player1.getLocation().getRow());
    }

    @Test
    void sendCommand_fail_invalidCommand(){
        localUser1.sendCommand("INVALIDCOMMAND");
        assertTrue(humanPlayerGUI.getMessageFeedText().toString().contains("invalid command"));
    }
}
