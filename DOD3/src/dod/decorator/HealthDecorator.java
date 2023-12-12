package dod.decorator;

import dod.game.Player;

public class HealthDecorator extends PlayerDecorator {

    public HealthDecorator(Player player) {
        super(player);
    }

    public void incrementHealth(int hp) {
        super.handleRequest("Health", hp + 20);
    }
}
