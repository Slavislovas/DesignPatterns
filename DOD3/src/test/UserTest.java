package test;

import dod.User;
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
public class UserTest {
    @InjectMocks
    GameLogic gameLogic = new GameLogic("DOD3/defaultMap");
    @Spy
    Map map = gameLogic.getMap();
    Tile floorTile = new Tile(Tile.TileType.FLOOR);
    Tile exitTile = new Tile(Tile.TileType.EXIT);
    String moveIntoWallErrorMessage = "FAIL can't move into a wall";
    String movieIntoPlayerErrorMessage = "FAIL can't move into a player";
    String moveNeedsDirectionErrorMessage = "FAIL MOVE needs a direction";
    String firstUserReceivedErrorMessage;
    String secondUserReceivedErrorMessage;
    //using abstract User class because command processing for LocalUser and NetworkUser is done here
    //all actions need to be done by this user
    User user1 = new User(gameLogic) {
        @Override
        public void update(String message) {
            firstUserReceivedErrorMessage = message;
        }
    };;
    //needed to test interactions between users
    User user2 = new User(gameLogic) {
        @Override
        public void update(String message) {
            secondUserReceivedErrorMessage = message;
        }
    };;

    Player player1 = gameLogic.getPlayer();
    Player player2 = gameLogic.getPlayerByIndex(1);

    public UserTest() throws FileNotFoundException, ParseException {
    }

    @BeforeEach
    void init(){
        player1.addToAP(1);
        player2.setLocation(new Location(-1, -1)); //so that player2 does not hinder movement for player1 in some test cases
    }


    @Test
    void processCommand_moveNorth_success(){
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        Player player = gameLogic.getPlayer();
        Location prevLocation = player.getLocation();
        gameLogic.startGame();
        user1.processCommand("MOVE N");
        assertEquals(prevLocation.getRow() - 1, gameLogic.getPlayer().getLocation().getRow());
    }

    @Test
    void processCommand_moveEast_success(){
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        Location prevLocation = player1.getLocation();
        gameLogic.startGame();
        user1.processCommand("MOVE E");
        assertEquals(prevLocation.getCol() + 1, gameLogic.getPlayer().getLocation().getCol());
    }

    @Test
    void processCommand_moveSouth_success(){
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        Location prevLocation = player1.getLocation();
        gameLogic.startGame();
        user1.processCommand("MOVE S");
        assertEquals(prevLocation.getRow() + 1, gameLogic.getPlayer().getLocation().getRow());
    }

    @Test
    void processCommand_moveWest_success(){
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        Location prevLocation = player1.getLocation();
        gameLogic.startGame();
        user1.processCommand("MOVE W");
        assertEquals(prevLocation.getCol() - 1, gameLogic.getPlayer().getLocation().getCol());
    }

    @Test
    void processCommand_moveNorth_fail_walkIntoWall(){
        Mockito.doReturn(false).when(map).insideMap(any());
        gameLogic.startGame();
        user1.processCommand("MOVE N");
        assertEquals(moveIntoWallErrorMessage, firstUserReceivedErrorMessage);
    }

    @Test
    void processCommand_moveNorth_fail_walkIntoPlayer(){
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() - 1));
        gameLogic.startGame();
        user1.processCommand("MOVE N");
        assertEquals(movieIntoPlayerErrorMessage, firstUserReceivedErrorMessage);
    }

    @Test
    void processCommand_moveEast_fail_walkIntoWall(){
        Mockito.doReturn(false).when(map).insideMap(any());
        gameLogic.startGame();
        user1.processCommand("MOVE E");
        assertEquals(moveIntoWallErrorMessage, firstUserReceivedErrorMessage);
    }

    @Test
    void processCommand_moveEast_fail_walkIntoPlayer(){
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        player2.setLocation(new Location(player1.getLocation().getCol() + 1, player1.getLocation().getRow()));
        gameLogic.startGame();
        user1.processCommand("MOVE E");
        assertEquals(movieIntoPlayerErrorMessage, firstUserReceivedErrorMessage);
    }

    @Test
    void processCommand_moveSouth_fail_walkIntoWall(){
        Mockito.doReturn(false).when(map).insideMap(any());
        gameLogic.startGame();
        user1.processCommand("MOVE S");
        assertEquals(moveIntoWallErrorMessage, firstUserReceivedErrorMessage);
    }

    @Test
    void processCommand_moveSouth_fail_walkIntoPlayer(){
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() + 1));
        gameLogic.startGame();
        user1.processCommand("MOVE S");
        assertEquals(movieIntoPlayerErrorMessage, firstUserReceivedErrorMessage);
    }

    @Test
    void processCommand_moveWest_fail_walkIntoWall(){
        Mockito.doReturn(false).when(map).insideMap(any());
        gameLogic.startGame();
        user1.processCommand("MOVE W");
        assertEquals(moveIntoWallErrorMessage, firstUserReceivedErrorMessage);
    }

    @Test
    void processCommand_moveWest_fail_walkIntoPlayer(){
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        player2.setLocation(new Location(player1.getLocation().getCol() - 1, player1.getLocation().getRow()));
        gameLogic.startGame();
        user1.processCommand("MOVE W");
        assertEquals(movieIntoPlayerErrorMessage, firstUserReceivedErrorMessage);
    }

    @Test
    void processCommand_moveOnExitTile_playerWins(){
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(exitTile).when(map).getMapCell(any());
        assertFalse(gameLogic.isPlayerWon());
        player1.addGold(map.getGoal());
        gameLogic.startGame();
        user1.processCommand("MOVE N");
        assertTrue(gameLogic.isPlayerWon());
    }

    @Test
    void processCommand_moveOnExitTile_playerDoesNotWin(){
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(exitTile).when(map).getMapCell(any());
        gameLogic.startGame();
        user1.processCommand("MOVE N");
        assertFalse(gameLogic.isPlayerWon());
    }

    @Test
    void processCommand_move_fail_noDirectionSpecified(){
        gameLogic.startGame();
        user1.processCommand("MOVE");
        assertEquals(moveNeedsDirectionErrorMessage, firstUserReceivedErrorMessage);
    }
}
