package test.unit;

import com.ginsberg.junit.exit.ExpectSystemExitWithStatus;
import dod.Communicator.LocalGameCommunicator;
import dod.GUI.HumanPlayerGUI;
import dod.GUI.PlayerGUI;
import dod.LocalUser;
import dod.abstractfactory.AbstractFactory;
import dod.factory.Creator;
import dod.factory.MapCreator;
import dod.game.GameLogic;
import dod.game.items.armour.HeavyArmour;
import dod.game.items.armour.LightArmour;
import dod.game.items.armour.MediumArmour;
import dod.game.items.gold.LargeGold;
import dod.game.items.gold.MediumGold;
import dod.game.items.gold.SmallGold;
import dod.game.items.health.MediumHealth;
import dod.game.items.health.StrongHealth;
import dod.game.items.health.WeakHealth;
import dod.game.items.lantern.MediumLantern;
import dod.game.items.lantern.StrongLantern;
import dod.game.items.lantern.WeakLantern;
import dod.game.items.sword.MediumSword;
import dod.game.items.sword.StrongSword;
import dod.game.items.sword.WeakSword;
import dod.game.maps.DefaultMap;
import dod.game.maps.Map;
import dod.game.maps.sMap;
import dod.game.maps.vsMap;
import dod.observer.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

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
        AbstractFactory defaultMapGameItemFactory = defaultMap.getAbstractFactory();
        assertTrue(defaultMapGameItemFactory.createGold() instanceof SmallGold);
        assertTrue(defaultMapGameItemFactory.createArmour() instanceof LightArmour);
        assertTrue(defaultMapGameItemFactory.createSword() instanceof WeakSword);
        assertTrue(defaultMapGameItemFactory.createHealth() instanceof WeakHealth);
        assertTrue(defaultMapGameItemFactory.createLantern() instanceof WeakLantern);
    }

    @Test
    void abstractFactory_SMapGameItemFactory_success() throws FileNotFoundException, ParseException {
        Map defaultMap = mapCreator.factoryMethod("DOD3/sMap");
        AbstractFactory defaultMapGameItemFactory = defaultMap.getAbstractFactory();
        assertTrue(defaultMapGameItemFactory.createGold() instanceof MediumGold);
        assertTrue(defaultMapGameItemFactory.createArmour() instanceof MediumArmour);
        assertTrue(defaultMapGameItemFactory.createSword() instanceof MediumSword);
        assertTrue(defaultMapGameItemFactory.createHealth() instanceof MediumHealth);
        assertTrue(defaultMapGameItemFactory.createLantern() instanceof MediumLantern);
    }

    @Test
    void abstractFactory_VSMapGameItemFactory_success() throws FileNotFoundException, ParseException {
        Map defaultMap = mapCreator.factoryMethod("DOD3/vsMap");
        AbstractFactory defaultMapGameItemFactory = defaultMap.getAbstractFactory();
        assertTrue(defaultMapGameItemFactory.createGold() instanceof LargeGold);
        assertTrue(defaultMapGameItemFactory.createArmour() instanceof HeavyArmour);
        assertTrue(defaultMapGameItemFactory.createSword() instanceof StrongSword);
        assertTrue(defaultMapGameItemFactory.createHealth() instanceof StrongHealth);
        assertTrue(defaultMapGameItemFactory.createLantern() instanceof StrongLantern);
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
        assertEquals("GOAL: 5", playerGUI.getGoalLabel().getText());
    }

    @Test
    void observer_localUser_treasureModMessage_success() {
        subject.registerObserver(new LocalUser(game, localGameCommunicator));
        subject.notifyObservers("TREASUREMOD 5");
        assertEquals("GOLD 5", playerGUI.getCurrentGoldLabel().getText());
    }

    @Test
    void observer_localUser_addMessageToFeed_success() {
        subject.registerObserver(new LocalUser(game, localGameCommunicator));
        subject.notifyObservers("[PLEASE LET THIS TEST WORK]");
        assertTrue(playerGUI.getMessageFeedText().getText().contains("[PLEASE LET THIS TEST WORK]"));
    }
}