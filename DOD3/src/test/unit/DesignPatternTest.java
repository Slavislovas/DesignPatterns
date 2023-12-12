package test.unit;

import com.ginsberg.junit.exit.ExpectSystemExitWithStatus;
import dod.Communicator.LocalGameCommunicator;
import dod.GUI.HumanPlayerGUI;
import dod.GUI.PlayerGUI;
import dod.LocalUser;
import dod.abstractfactory.AbstractFactory;
import dod.abstractfactory.DefaultMapGameItemFactory;
import dod.abstractfactory.SMapGameItemFactory;
import dod.abstractfactory.VSMapGameItemFactory;
import dod.bridgePattern.IItemType;
import dod.bridgePattern.SmallItem;
import dod.factory.Creator;
import dod.factory.MapCreator;
import dod.game.GameLogic;
import dod.game.items.gold.Gold;
import dod.game.maps.DefaultMap;
import dod.game.maps.Map;
import dod.game.maps.sMap;
import dod.game.maps.vsMap;
import dod.observer.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Objects;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DesignPatternTest {
    Creator mapCreator;
    Subject subject;
    GameLogic game;
    LocalGameCommunicator localGameCommunicator;
    PlayerGUI playerGUI;

    @BeforeEach
    void init() throws IOException, ParseException {
        mapCreator = new MapCreator();
        subject = new Subject();
        game = new GameLogic("DOD3/defaultMap");
        localGameCommunicator = new LocalGameCommunicator(game);
        playerGUI = new HumanPlayerGUI(localGameCommunicator, "Test", true);
    }

    @Test
    void factory_createDefaultMap_success() throws FileNotFoundException, ParseException {
        Map defaultMap = mapCreator.factoryMethod("DOD3/defaultMap");
        assertTrue(defaultMap instanceof DefaultMap);
    }

    @Test
    void factory_createSMap_success() throws FileNotFoundException, ParseException {
        Map defaultMap = mapCreator.factoryMethod("DOD3/sMap");
        assertTrue(defaultMap instanceof sMap);
    }

    @Test
    void factory_createVSMap_success() throws FileNotFoundException, ParseException {
        Map defaultMap = mapCreator.factoryMethod("DOD3/vsMap");
        assertTrue(defaultMap instanceof vsMap);
    }

    @Test
    void factory_createAnyMap_fail() throws FileNotFoundException, ParseException {
        assertThrows(IllegalArgumentException.class, () ->  mapCreator.factoryMethod("DOD3/non-existent-map"));
    }


    @Test
    void abstractFactory_defaultMapGameItemFactory_success() throws FileNotFoundException, ParseException {
        Map defaultMap = mapCreator.factoryMethod("DOD3/defaultMap");
        var defaultMapGameItemFactory = (DefaultMapGameItemFactory)defaultMap.getAbstractFactory();
        defaultMapGameItemFactory.setRandom(Mockito.mock(Random.class));
        var random = defaultMapGameItemFactory.getRandom();
        Mockito.when(random.nextInt(100)).thenReturn(1);
        assertEquals("Small gold pile", defaultMapGameItemFactory.createGold(defaultMap.get_mediator()).toString());
        assertEquals("Small armour", defaultMapGameItemFactory.createArmour(defaultMap.get_mediator()).toString());
        assertEquals("Small sword", defaultMapGameItemFactory.createSword(defaultMap.get_mediator()).toString());
        assertEquals("Small health potion", defaultMapGameItemFactory.createHealth(defaultMap.get_mediator()).toString());
        assertEquals("Small lantern", defaultMapGameItemFactory.createLantern(defaultMap.get_mediator()).toString());
    }

    @Test
    void abstractFactory_SMapGameItemFactory_success() throws FileNotFoundException, ParseException {
        Map defaultMap = mapCreator.factoryMethod("DOD3/sMap");
        var sMapGameItemFactory = (SMapGameItemFactory)defaultMap.getAbstractFactory();
        assertEquals("Medium gold pile", sMapGameItemFactory.createGold(defaultMap.get_mediator()).toString());
        assertEquals("Medium armour", sMapGameItemFactory.createArmour(defaultMap.get_mediator()).toString());
        assertEquals("Medium sword", sMapGameItemFactory.createSword(defaultMap.get_mediator()).toString());
        assertEquals("Medium health potion", sMapGameItemFactory.createHealth(defaultMap.get_mediator()).toString());
        assertEquals("Medium lantern", sMapGameItemFactory.createLantern(defaultMap.get_mediator()).toString());
    }

    @Test
    void abstractFactory_VSMapGameItemFactory_success() throws FileNotFoundException, ParseException {
        Map defaultMap = mapCreator.factoryMethod("DOD3/vsMap");
        var vsMapGameItemFactory = (VSMapGameItemFactory)defaultMap.getAbstractFactory();
        assertEquals("Large gold pile", vsMapGameItemFactory.createGold(defaultMap.get_mediator()).toString());
        assertEquals("Large armour", vsMapGameItemFactory.createArmour(defaultMap.get_mediator()).toString());
        assertEquals("Large sword", vsMapGameItemFactory.createSword(defaultMap.get_mediator()).toString());
        assertEquals("Large health potion", vsMapGameItemFactory.createHealth(defaultMap.get_mediator()).toString());
        assertEquals("Large lantern", vsMapGameItemFactory.createLantern(defaultMap.get_mediator()).toString());
    }

    @Test
    @ExpectSystemExitWithStatus(42)
    void observer_localUser_dieMessage_success() {
        subject.registerObserver(new LocalUser(game, localGameCommunicator));
        subject.notifyObservers("DIE");
    }

    @Test
    void observer_localUser_equipArmourMessage_success() {
        subject.registerObserver(new LocalUser(game, localGameCommunicator));
        subject.notifyObservers("You equip Armour");
        assertTrue(playerGUI.isHasArmour());
    }

    @Test
    void observer_localUser_equipSwordMessage_success() {
        subject.registerObserver(new LocalUser(game, localGameCommunicator));
        subject.notifyObservers("You equip Sword");
        assertTrue(playerGUI.getSwordLabel().isVisible());
    }

    @Test
    void observer_localUser_goalMessage_success() {
        subject.registerObserver(new LocalUser(game, localGameCommunicator));
        subject.notifyObservers("GOAL: 5");
        assertEquals("GOAL: 5", playerGUI.getGoalLabel().toString());
    }

    @Test
    void observer_localUser_treasureModMessage_success() {
        subject.registerObserver(new LocalUser(game, localGameCommunicator));
        subject.notifyObservers("TREASUREMOD 5");
        assertEquals("GOLD 5", playerGUI.getCurrentGoldLabel().toString());
    }

    @Test
    void observer_localUser_addMessageToFeed_success() {
        subject.registerObserver(new LocalUser(game, localGameCommunicator));
        subject.notifyObservers("[PLEASE LET THIS TEST WORK]");
        assertTrue(playerGUI.getMessageFeedText().toString().contains("[PLEASE LET THIS TEST WORK]"));
    }
}