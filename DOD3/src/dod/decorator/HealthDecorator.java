package dod.decorator;

import dod.game.Player;

public class HealthDecorator extends PlayerDecorator {

    public HealthDecorator(Player player) {
        super(player);
    }

    public void incrementHealth(int hp) {
        super.incrementHealth(hp + 20); // Adding 20 as a bonus
    }
}
