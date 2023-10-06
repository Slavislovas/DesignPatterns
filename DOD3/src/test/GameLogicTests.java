package test;

import dod.User;
import dod.game.CommandException;
import dod.game.CompassDirection;
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
public class GameLogicTests {
    @InjectMocks
    GameLogic gameLogic = new GameLogic("DOD3/defaultMap");
    @Spy
    Map map = gameLogic.getMap();
    Tile floorTile = new Tile(Tile.TileType.FLOOR);
    Tile exitTile = new Tile(Tile.TileType.EXIT);
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

    public GameLogicTests() throws FileNotFoundException, ParseException {
    }

    @BeforeEach
    void init(){
        player1.addToAP(1);
        player2.setLocation(new Location(-1, -1)); //so that player2 does not hinder movement for player1 in some test cases
    }


    @Test
    void clientMove_moveNorth_success() throws CommandException {
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        Player player = gameLogic.getPlayer();
        Location prevLocation = player.getLocation();
        gameLogic.startGame();
        gameLogic.clientMove(CompassDirection.NORTH);
        assertEquals(prevLocation.getRow() - 1, gameLogic.getPlayer().getLocation().getRow());
    }

    @Test
    void clientMove_moveEast_success() throws CommandException {
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        Location prevLocation = player1.getLocation();
        gameLogic.startGame();
        gameLogic.clientMove(CompassDirection.EAST);
        assertEquals(prevLocation.getCol() + 1, gameLogic.getPlayer().getLocation().getCol());
    }

    @Test
    void clientMove_moveSouth_success() throws CommandException {
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        Location prevLocation = player1.getLocation();
        gameLogic.startGame();
        gameLogic.clientMove(CompassDirection.SOUTH);
        assertEquals(prevLocation.getRow() + 1, gameLogic.getPlayer().getLocation().getRow());
    }

    @Test
    void clientMove_moveWest_success() throws CommandException {
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        Location prevLocation = player1.getLocation();
        gameLogic.startGame();
        gameLogic.clientMove(CompassDirection.WEST);
        assertEquals(prevLocation.getCol() - 1, gameLogic.getPlayer().getLocation().getCol());
    }

    @Test
    void clientMove_moveNorth_fail_walkIntoWall(){
        Mockito.doReturn(false).when(map).insideMap(any());
        gameLogic.startGame();
        assertThrows(CommandException.class, () ->  gameLogic.clientMove(CompassDirection.NORTH));
    }

    @Test
    void clientMove_moveNorth_fail_walkIntoPlayer(){
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() - 1));
        gameLogic.startGame();
        assertThrows(CommandException.class, () -> gameLogic.clientMove(CompassDirection.NORTH));
    }

    @Test
    void clientMove_moveEast_fail_walkIntoWall(){
        Mockito.doReturn(false).when(map).insideMap(any());
        gameLogic.startGame();
        assertThrows(CommandException.class, () -> gameLogic.clientMove(CompassDirection.EAST));
    }

    @Test
    void clientMove_moveEast_fail_walkIntoPlayer(){
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        player2.setLocation(new Location(player1.getLocation().getCol() + 1, player1.getLocation().getRow()));
        gameLogic.startGame();
        assertThrows(CommandException.class, () -> gameLogic.clientMove(CompassDirection.EAST));
    }

    @Test
    void clientMove_moveSouth_fail_walkIntoWall(){
        Mockito.doReturn(false).when(map).insideMap(any());
        gameLogic.startGame();
        assertThrows(CommandException.class, () -> gameLogic.clientMove(CompassDirection.SOUTH));
    }

    @Test
    void clientMove_moveSouth_fail_walkIntoPlayer(){
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        player2.setLocation(new Location(player1.getLocation().getCol(), player1.getLocation().getRow() + 1));
        gameLogic.startGame();
        assertThrows(CommandException.class, () -> gameLogic.clientMove(CompassDirection.SOUTH));
    }

    @Test
    void clientMove_moveWest_fail_walkIntoWall(){
        Mockito.doReturn(false).when(map).insideMap(any());
        gameLogic.startGame();
        assertThrows(CommandException.class, () -> gameLogic.clientMove(CompassDirection.WEST));
    }

    @Test
    void clientMove_moveWest_fail_walkIntoPlayer(){
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(floorTile).when(map).getMapCell(any());
        player2.setLocation(new Location(player1.getLocation().getCol() - 1, player1.getLocation().getRow()));
        gameLogic.startGame();
        assertThrows(CommandException.class, () -> gameLogic.clientMove(CompassDirection.WEST));
    }

    @Test
    void clientMove_moveOnExitTile_playerWins() throws CommandException {
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(exitTile).when(map).getMapCell(any());
        assertFalse(gameLogic.isPlayerWon());
        player1.addGold(map.getGoal());
        gameLogic.startGame();
        gameLogic.clientMove(CompassDirection.NORTH);
        assertTrue(gameLogic.isPlayerWon());
    }

    @Test
    void clientMove_moveOnExitTile_playerDoesNotWin() throws CommandException {
        Mockito.doReturn(true).when(map).insideMap(any());
        Mockito.doReturn(exitTile).when(map).getMapCell(any());
        gameLogic.startGame();
        gameLogic.clientMove(CompassDirection.NORTH);
        assertFalse(gameLogic.isPlayerWon());
    }
}
