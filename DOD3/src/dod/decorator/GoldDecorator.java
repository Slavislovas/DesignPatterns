package dod.decorator;

import dod.game.Player;

public class GoldDecorator extends PlayerDecorator {

    public GoldDecorator(Player player) {
        super(player);
    }

    @Override
    public void addGold(int gold) {
        System.out.println("Getting extra gold!");
        player.handleRequest("Gold", gold * 10);
    }
}
