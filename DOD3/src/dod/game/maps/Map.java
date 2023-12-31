package dod.game.maps;

import dod.abstractfactory.AbstractFactory;
import dod.game.Location;
import dod.game.Tile;
import dod.game.items.gold.Gold;
import dod.mediator.IMediator;
import dod.mediator.ItemMediator;
import lombok.Getter;
import dod.iterator.IterableCollection;
import dod.iterator.Iterator;
import dod.iterator.MapIterator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class containing the map used by the game engine. Allows for reading in ASCII
 * art maps from a file, or just using the default map.
 * <p>
 * You are not responsible for making this more robust - you are only required
 * to perform error checking on the extra code that you write to network the
 * client and server.
 */
public abstract class Map implements IterableCollection<Location> {
    @Getter
    private final IMediator _mediator = new ItemMediator();

    // The name of the map
    private String name;

    // The tiles of the map, stored in row-major order, i.e. [row][col]
    Tile map[][];

    // The number of gold required to win
    private int goal;

    // The lines containing the name and goal, and rest of the map
    public static final int NAMELINE = 0;
    public static final int GOALLINE = 1;
    public static final int MAPBEGINLINE = 2;

    // Minimum number of lines
    public static final int MINLINES = 3;


    /**
     * Creates a map from the file specified. Note that this is not robust...
     *
     * @param filename The name of the file to load the map from
     * @throws ParseException
     * @throws FileNotFoundException
     */
    public Map(String filename) throws ParseException, FileNotFoundException {
        loadMap(loadMapLines(filename));
    }

    @Override
    public Iterator<Location> getIterator(){
        return new MapIterator(this);
    }

    private List<String> loadMapLines(String filename)  throws ParseException, FileNotFoundException {
       return readFile(filename);
    }

    private void loadMap(List<String> lines) throws ParseException, FileNotFoundException {
        // Good programmers always check this...
        if (lines.size() < MINLINES) {
            throw new ParseException(
                    "a map file must contain at least three lines",
                    lines.size());
        }

        // The first line should always be the name of the map.
        parseMapName(lines.get(NAMELINE));

        // The second line should be the goal.
        parseMapGoal(lines.get(GOALLINE));

        // Read in the map data from the file
        readMap(lines);
    }

    public abstract AbstractFactory getAbstractFactory();

    /**
     * @return The width of the map
     */
    public int getMapWidth() {
        return this.map[0].length;
    }

    /**
     * @return The height of the map
     */
    public int getMapHeight() {
        return this.map.length;
    }

    /**
     * Returns the contents of the cell of the map at the location
     *
     * @return the contents of the cell
     */
    public Tile getMapCell(Location location) {
        return this.map[location.getRow()][location.getCol()];
    }

    /**
     * Drops gold on the ground if there isn't already an item there
     *
     * @param location The location where gold is to be dropped
     * @author Benjamin Dring
     */
    public abstract void dropGold(Location location);


    /**
     * @return The amount of gold required to win on this map
     */
    public int getGoal() {
        return this.goal;
    }

    /**
     * Used to check if a location is a valid location in the map
     *
     * @return true if the location is valid, false otherwise
     */
    public boolean insideMap(Location location) {
        if ((location.getCol() < 0) || (location.getCol() >= getMapWidth())
                || (location.getRow() < 0)
                || (location.getRow() >= getMapHeight())) {
            return false;
        }
        return true;
    }

    /**
     * @return The name of the map
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the amount of gold that has not been picked up on the map
     */
    public int remainingGold() {
        int goldCount = 0;

        for (final Tile[] tileRow : this.map) {
            for (final Tile tile : tileRow) {
                if (tile.hasItem()) {
                    if (tile.getItem() instanceof Gold) {
                        goldCount++;
                    }
                }
            }
        }
        return goldCount;
    }

    /**
     * Reads in a file and returns a List of Strings. This makes life slightly
     * easier.
     *
     * @param filename The name of the file to read the map from
     * @return A List of lines in the file
     * @throws FileNotFoundException
     */
    public List<String> readFile(String filename) throws FileNotFoundException {
        Scanner scanner = null;

        final List<String> lines = new ArrayList<String>();
        try {
            // Use a scanner to read all the lines
            scanner = new Scanner(new FileReader(filename));

            // Store all the lines in the file
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }

        return lines;
    }

    /**
     * Reads in the map data from the file, storing the tiles to this.map
     *
     * @param lines All the lines of text from the map file
     * @throws ParseException
     * @throws IllegalStateException
     */
    public void readMap(List<String> lines) throws ParseException,
            IllegalStateException {
        // Read the rest of the map
        final int mapWidth = lines.get(MAPBEGINLINE).length();
        final int mapHeight = lines.size() - MAPBEGINLINE;

        this.map = new Tile[mapHeight][mapWidth];

        for (int row = 0; row < mapHeight; row++) {
            final int lineNum = row + MAPBEGINLINE;
            final String line = lines.get(lineNum);

            if (line.length() != mapWidth) {
                throw new ParseException("all lines must be the same length",
                        lineNum);
            }

            for (int col = 0; col < line.length(); col++) {
                // Just use the character representation in the input file.

                try {
                    this.map[row][col] = Tile.fromChar(line.charAt(col), this);
                } catch (final IllegalArgumentException e) {
                    throw new ParseException("Invalid character (col:" + col
                            + ")", lineNum);
                }
            }
        }

    }

    /**
     * Obtains the map name from the first line of the map file
     *
     * @param firstLine the first line of the map file
     * @throws ParseException
     */
    public void parseMapName(String firstLine) throws ParseException {
        this.name = getStringAfterTag(firstLine, "name", NAMELINE);
    }

    /**
     * Obtains the goal (number of cold to collect) from the second line of the
     * map file
     *
     * @param secondLine the second line of the map file
     * @throws ParseException
     */
    public void parseMapGoal(String secondLine) throws ParseException {
        final String goalString = getStringAfterTag(secondLine, "win", GOALLINE);

        try {
            this.goal = Integer.parseInt(goalString);
        } catch (final NumberFormatException e) {
            throw new ParseException("map goal should be an integer", GOALLINE);
        }
    }

    /**
     * A helper method to process a line in a file of the format <tag>
     * <argument> Returns the argument, if the tag is correct
     *
     * @param line    the line of the file to process
     * @param tag     the expected tag
     * @param lineNum the line number, used to create an effective ParseException
     * @return the argument after the tag
     * @throws ParseException
     */
    private String getStringAfterTag(String line, String tag, int lineNum)
            throws ParseException {
        // The first space will be after the tag
        final int firstSpace = line.indexOf(" ");

        // There may not be a first space
        if (firstSpace == -1) {
            throw new ParseException(tag + " not specified in file; the " + tag
                    + " should be be preceded with \"" + tag + "\"", lineNum);
        }

        // Check that the tag is the first "word"
        if (line.substring(0, firstSpace).equals(tag)) {
            return line.substring(firstSpace + 1);
        } else {
            throw new ParseException("The map" + tag
                    + "should be preceded with \"" + tag + "\"", lineNum);
        }
    }
}
