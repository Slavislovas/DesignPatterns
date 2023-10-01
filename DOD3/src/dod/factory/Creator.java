package dod.factory;

import dod.game.maps.Map;

import java.io.FileNotFoundException;
import java.text.ParseException;

public abstract class Creator {
    public abstract Map factoryMethod(String identifier) throws FileNotFoundException, ParseException;
}
