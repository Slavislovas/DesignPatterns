package dod.factory;

import dod.game.items.GameItem;

public abstract class Factory {
    abstract GameItem createGameItem(char gameItemType);
}
