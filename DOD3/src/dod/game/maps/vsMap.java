package dod.game.maps;

import dod.abstractfactory.AbstractFactory;
import dod.abstractfactory.DefaultMapGameItemFactory;
import dod.abstractfactory.VSMapGameItemFactory;
import dod.game.Location;
import dod.game.Tile;

import java.io.FileNotFoundException;
import java.text.ParseException;

public class vsMap extends Map{
    private AbstractFactory abstractFactory;
    public vsMap(String filename) throws ParseException, FileNotFoundException {
        super(filename);
    }

    @Override
    public AbstractFactory getAbstractFactory() {
        if (this.abstractFactory == null){
            this.abstractFactory = new VSMapGameItemFactory();
        }
        return abstractFactory;
    }

    @Override
    public void dropGold(Location location) {
        Tile tile = getMapCell(location);
        if ((tile.hasItem() == false) && (tile.isExit() == false)) {
            this.map[location.getRow()][location.getCol()] = new Tile(abstractFactory.createGold());
        }
    }
}
