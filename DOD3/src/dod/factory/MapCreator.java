package dod.factory;

import dod.game.maps.DefaultMap;
import dod.game.maps.Map;
import dod.game.maps.sMap;
import dod.game.maps.vsMap;

import java.io.FileNotFoundException;
import java.text.ParseException;

public class MapCreator extends Creator{
    @Override
    public Map factoryMethod(String identifier) throws FileNotFoundException, ParseException {
        switch (identifier){
            case "DOD3/defaultMap":
                return new DefaultMap(identifier);
            case "DOD3/sMap":
                return new sMap(identifier);
            case "DOD3/vsMap":
                return new vsMap(identifier);
            default:
                throw new IllegalArgumentException("Map: " + identifier + " does not exist");
        }
    }
}
