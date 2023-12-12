package dod.game.maps;

import dod.abstractfactory.AbstractFactory;
import dod.abstractfactory.DefaultMapGameItemFactory;
import dod.game.Location;
import dod.game.Tile;

import java.io.FileNotFoundException;
import java.text.ParseException;

public class DefaultMap extends Map{
    private AbstractFactory abstractFactory;
    public DefaultMap(String filename) throws ParseException, FileNotFoundException {
        super(filename);
    }

    @Override
    public AbstractFactory getAbstractFactory() {
        if (this.abstractFactory == null){
            this.abstractFactory = new DefaultMapGameItemFactory();
        }
        return abstractFactory;
    }

    @Override
    public void dropGold(Location location) {
        Tile tile = getMapCell(location);
        if ((!tile.hasItem()) && (!tile.isExit())) {
            this.map[location.getRow()][location.getCol()] = new Tile(abstractFactory.createGold(get_mediator()));
        }
    }
}
