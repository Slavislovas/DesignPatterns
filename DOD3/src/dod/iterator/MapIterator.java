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
        for (int x = currentX; x < map.getMapWidth(); x++){
            for (int y = currentY; y < map.getMapHeight(); y++){
                if (map.getMapCell(new Location(x, y)).isWalkable()){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Location next() {
        if (!hasNext()) {
            throw new IllegalStateException("No more locations in the map.");
        }

        Location nextLocation = new Location(currentX, currentY);
        while (!map.getMapCell(nextLocation).isWalkable()){
            currentX++;
            if (currentX >= map.getMapWidth()) {
                currentX = 0;
                currentY++;
            }
            nextLocation = new Location(currentX, currentY);
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
