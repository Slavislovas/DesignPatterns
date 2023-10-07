package dod.singleton;

import dod.game.CompassDirection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Settings {
    private static Settings instance;
    private final Map<String, String> userRoles;
    private final ArrayList<CompassDirection> possibleDirections = new ArrayList<CompassDirection>();

    private Settings() {
        userRoles = new HashMap<>();
        // Initial roles
        userRoles.put("test1", "vip");
        userRoles.put("test2", "supervip");
        userRoles.put("test3", "admin");

        possibleDirections.add(CompassDirection.NORTH);
        possibleDirections.add(CompassDirection.SOUTH);
        possibleDirections.add(CompassDirection.EAST);
        possibleDirections.add(CompassDirection.WEST);
    }

    public static synchronized Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    public String getUserRole(String name) {
        return userRoles.get(name);
    }

    public ArrayList<CompassDirection> getPossibleDirections() {
        return possibleDirections;
    }
}