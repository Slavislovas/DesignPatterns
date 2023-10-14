package dod.decorator;

import dod.game.Player;

public abstract class PlayerDecorator extends Player {
    protected Player player;

    public PlayerDecorator(Player player) {
        super(player.getName(), player.getLocation(), player.getListener());
        this.player = player;
    }

    public void addGold(int gold) {
        player.addGold(gold);
    }

    public void incrementHealth(int hp) {
        player.incrementHealth(hp);
    }

    public void sendMessage(String message) {
        player.sendMessage(message);
    }
}

