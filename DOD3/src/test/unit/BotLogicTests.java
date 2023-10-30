package test.unit;

import dod.BotLogic.AggressiveBotStrategy;
import dod.BotLogic.Bot;
import dod.BotLogic.FriendlyBotStrategy;
import dod.BotLogic.RandomBotStrategy;
import dod.Communicator.GameCommunicator;
import dod.game.CompassDirection;
import dod.game.GameLogic;
import dod.game.Location;
import dod.strategy.BotStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class BotLogicTests {


    private Bot bot;

    @BeforeEach
    void init(){
        bot = Mockito.spy(new Bot(Mockito.mock(GameCommunicator.class)) {
            @Override
            public Bot Clone(GameCommunicator comm) {
                return null;
            }

            @Override
            public void performAction() {

            }
        });
        bot.setBotStrategy(Mockito.mock(BotStrategy.class));
        bot.setPlayerLocation(new Location(0, 0));
        bot.setLookReply(new char[][] {});
    }

    private static Stream<Arguments> messageStream(){
        return Stream.of(
                Arguments.of("goalTest", false),
                Arguments.of("stArtTuRnTest", true),
                Arguments.of("EnDTuRnTest", false)
        );
    }

    @ParameterizedTest
    @MethodSource("messageStream")
    void handleMessage_success(String message, boolean turn) {
        assertFalse(bot.isMyTurn());
        bot.handelMessage(message);
        assertEquals(bot.isMyTurn(), turn);
    }

    @Test
    void giveLookReply_is_null(){
        String[] array = { "test" };
        bot.giveLookReply(array);
        assertNull(bot.getLookReply());
        assertNull(bot.getPlayerLocation());
        assertTrue(bot.isUpdatedLook());
    }

    @Test
    void die_success(){
        assertFalse(bot.isGameOver());
        bot.die();
        assertTrue(bot.isGameOver());
    }

    @Test
    void hasRequiredGold_success(){
        bot.setCurrentGold(5);
        bot.setGoal(3);
        assertTrue(bot.hasRequiredGold());
    }

    @Test
    void goldLeftToCollect_equals_3(){
        bot.setCurrentGold(2);
        bot.setGoal(5);
        assertEquals(bot.goldLeftToCollect(), 3);
    }

    private static Stream<Arguments> compassDirectionStream(){
        return Stream.of(
                Arguments.of(CompassDirection.NORTH, "N"),
                Arguments.of(CompassDirection.EAST, "E"),
                Arguments.of(CompassDirection.SOUTH, "S"),
                Arguments.of(CompassDirection.WEST, "W")
        );
    }

    @ParameterizedTest
    @MethodSource("compassDirectionStream")
    void handleMessage_success(CompassDirection direction, String result) {
        assertEquals(bot.getDirectionCharacter(direction), result);
    }

    @Test
    void doesBlock_equals_true(){
        var location = new Location(1, 1);
        Mockito.doReturn('X').when(bot).getTile(location);
        assertTrue(bot.doesBlock(location));
    }

    @Test
    void pickupGold_success(){
        bot.setCurrentGold(5);
        bot.pickupGold();
        assertEquals(bot.getCurrentGold(), 6);
    }

    @Test
    void changeBotStrategy_to_Friendly(){
        Mockito.doReturn(true).when(bot).isPlayerNearby();
        bot.setCurrentGold(5);
        bot.setGoal(3);
        var result = bot.changeBotStrategy();
        assertTrue(bot.getBotStrategy() instanceof FriendlyBotStrategy);
        assertTrue(result);
    }

    @Test
    void changeBotStrategy_to_Aggressive(){
        Mockito.doReturn(true).when(bot).isPlayerNearby();
        bot.setCurrentGold(1);
        bot.setGoal(3);
        bot.setHasSword(true);
        var result = bot.changeBotStrategy();
        assertTrue(bot.getBotStrategy() instanceof AggressiveBotStrategy);
        assertTrue(result);
    }

    @Test
    void clone_is_deep(){
        bot = new RandomBotStrategy(bot.getComm());
        bot.setHasLantern(true);
        bot.setCurrentGold(1);
        bot.setGoal(3);
        var clone = bot.Clone(new GameCommunicator() {
            @Override
            public void sendMessageToGame(String message) { }

            @Override
            public GameLogic GetGameLogic() { return null; }
        });
        assertNotSame(bot.hashCode(), clone.hashCode());
        assertNotSame(bot.getComm(), clone.getComm());
        assertEquals(bot.isHasLantern(), clone.isHasLantern());
        assertEquals(bot.getCurrentGold(), clone.getCurrentGold());
        assertEquals(bot.getGoal(), clone.getGoal());
    }
}