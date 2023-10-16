package test.integration;

import dod.Communicator.LocalGameCommunicator;
import dod.GUI.HumanPlayerGUI;
import dod.LocalUser;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class LocalUserIntegrationTests {
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
    LocalGameCommunicator localGameCommunicator1 = new LocalGameCommunicator(gameLogic);
    LocalGameCommunicator localGameCommunicator2 = new LocalGameCommunicator(gameLogic);
    LocalUser localUser1 = localGameCommunicator1.getUser();
    Player player1 = gameLogic.getPlayer();
    Player player2 = gameLogic.getPlayerByIndex(1);
    Location initialPlayerLocation;
    HumanPlayerGUI humanPlayerGUI = new HumanPlayerGUI(localGameCommunicator1, player1.getName(), false);

    public LocalUserIntegrationTests() throws FileNotFoundException, ParseException {
    }

    @BeforeEach
    void init() throws CommandException {
        player1.setLocation(new Location(map.getMapWidth() / 2, map.getMapHeight() / 2));
        player1.addToAP(10);
        player1.setGold(0);
        player2.setLocation(new Location(-1, -1)); //so that player2 does not hinder movement of player1
        initialPlayerLocation = player1.getLocation();
        humanPlayerGUI.getMessageFeedText().setText("");
        gameLogic.startGame();
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
        assertTrue(humanPlayerGUI.getMessageFeedText().getText().contains("Game has finished!"));
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
    void sendCommand_moveNorth_fail_moveIntoWall(){
        Mockito.doReturn(false).when(map).insideMap(any());
        int initialPlayerAp = player1.remainingAp();
        localUser1.sendCommand("MOVE N");
        assertEquals(initialPlayerLocation.getRow(), player1.getLocation().getRow());
        assertEquals(initialPlayerAp , player1.remainingAp());
        assertTrue(humanPlayerGUI.getMessageFeedText().getText().contains("Server: FAIL can't move into a wall"));
    }

    @Test
    void sendCommand_moveEast_fail_moveIntoWall(){
        Mockito.doReturn(false).when(map).insideMap(any());
        int initialPlayerAp = player1.remainingAp();
        localUser1.sendCommand("MOVE E");
        assertEquals(initialPlayerLocation.getCol(), player1.getLocation().getCol());
        assertEquals(initialPlayerAp, player1.remainingAp());
        assertTrue(humanPlayerGUI.getMessageFeedText().getText().contains("Server: FAIL can't move into a wall"));
    }

    @Test
    void sendCommand_moveSouth_fail_moveIntoWall(){
        Mockito.doReturn(false).when(map).insideMap(any());
        int initialPlayerAp = player1.remainingAp();
        localUser1.sendCommand("MOVE S");
        assertEquals(initialPlayerLocation.getRow(), player1.getLocation().getRow());
        assertEquals(initialPlayerAp, player1.remainingAp());
        assertTrue(humanPlayerGUI.getMessageFeedText().getText().contains("Server: FAIL can't move into a wall"));
    }

    @Test
    void sendCommand_moveWest_fail_moveIntoWall(){
        Mockito.doReturn(false).when(map).insideMap(any());
        int initialPlayerAp = player1.remainingAp();
        localUser1.sendCommand("MOVE W");
        assertEquals(initialPlayerLocation.getCol(), player1.getLocation().getCol());
        assertEquals(initialPlayerAp, player1.remainingAp());
        assertTrue(humanPlayerGUI.getMessageFeedText().getText().contains("Server: FAIL can't move into a wall"));
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
        assertTrue(humanPlayerGUI.getMessageFeedText().getText().contains("Server: FAIL can't move into a player"));
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
        assertTrue(humanPlayerGUI.getMessageFeedText().getText().contains("Server: FAIL can't move into a player"));
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
        assertTrue(humanPlayerGUI.getMessageFeedText().getText().contains("Server: FAIL can't move into a player"));
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
        assertTrue(humanPlayerGUI.getMessageFeedText().getText().contains("Server: FAIL can't move into a player"));
    }

    @Test
    void sendCommand_move_fail_noDirectionSpecified(){
        int initialPlayerAp = player1.remainingAp();
        localUser1.sendCommand("MOVE");
        assertEquals(initialPlayerLocation.getCol(), player1.getLocation().getCol());
        assertEquals(initialPlayerAp, player1.remainingAp());
        assertTrue(humanPlayerGUI.getMessageFeedText().getText().contains("Server: FAIL MOVE needs a direction"));
    }
}
