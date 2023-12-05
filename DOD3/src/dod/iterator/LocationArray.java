package dod.iterator;

import dod.game.Location;

public class LocationArray implements CustomCollection<Location>{
    private final Location[] locations;
    private int size;

    public LocationArray(int size) {
        this.locations = new Location[size];
        this.size = 0;
    }

    @Override
    public Iterator<Location> getIterator() {
        System.out.println("LocationArray: getting iterator");
        return new ArrayIterator<>(locations);
    }

    @Override
    public void add(Location location) {
        if (size == locations.length) {
            // Optionally, you could throw an exception or implement a resizing strategy here
            System.out.println("Array is full. Cannot add more items.");
            return;
        }
        locations[size++] = location;
    }

    @Override
    public void set(Location element, int index) {
        if (index < 0 || index >= locations.length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (locations[index] == null){
            size++;
        }
        locations[index] = element;
    }

    @Override
    public void remove(Location location) {
        int index = findIndex(location);
        if (index != -1) {
            removeItemAt(index);
        }
    }

    private void removeItemAt(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        int numToMove = size - index - 1;
        if (numToMove > 0) {
            System.arraycopy(locations, index + 1, locations, index, numToMove);
        }
        locations[--size] = null;
    }

    @Override
    public boolean contains(Location location) {
        return findIndex(location) != -1;
    }

    private int findIndex(Location location) {
        for (int i = 0; i < size; i++) {
            if (locations[i].equals(location)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Location get(int index) {
        return locations[index];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}
