package dod.decorator;

import dod.game.Location;
import dod.game.Player;
import dod.game.PlayerListener;

public class VipPlayer extends Player implements PlayerDecorator {

    public VipPlayer(String name, Location location, PlayerListener listener) {
        super(name, location, listener);
    }

    @Override
    public int getGold() {
        return super.getGold() * 2;
    }

    @Override
    public int getHp() {
        return super.getHp() + 10;
    }

    @Override
    public void sendMessage(String message) {
        super.sendMessage("[VIP] " + message);
    }
}
