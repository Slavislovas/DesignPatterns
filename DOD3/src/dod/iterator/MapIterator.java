package dod.iterator;

import dod.game.Location;
import dod.game.maps.Map;

public class MapIterator implements Iterator<Location>{
    private final Map map;
    private int currentX;
    private int currentY;

    public MapIterator(Map map) {
        this.map = map;
        this.currentX = 0;
        this.currentY = 0;
    }

    @Override
    public boolean hasNext() {
        return currentX < map.getMapWidth() && currentY < map.getMapHeight();
    }

    @Override
    public Location next() {
        if (!hasNext()) {
            throw new IllegalStateException("No more locations in the map.");
        }
        Location nextLocation = new Location(currentX, currentY);
        currentX++;
        if (currentX >= map.getMapWidth()) {
            currentX = 0;
            currentY++;
        }
        return nextLocation;
    }

    @Override
    public Location first() {
        currentX = 0;
        currentY = 0;
        return new Location(currentX, currentY);
    }
}
