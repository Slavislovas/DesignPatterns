package dod.factory;

import dod.game.items.Armour;
import dod.game.items.GameItem;
import dod.game.items.Gold;
import dod.game.items.Health;
import dod.game.items.Lantern;
import dod.game.items.Sword;

public class GameItemFactory extends Factory{

    @Override
    public GameItem createGameItem(char itemType){
        switch (itemType){
            case 'A':
                return new Armour();
            case 'G':
                return new Gold();
            case 'H':
                return new Health();
            case 'L':
                return new Lantern();
            case 'S':
                return new Sword();
            default:
                throw new IllegalArgumentException("Unknown game item: " + itemType);
        }
    }
}
