package dod.chainOfResponsibility;

import dod.game.Player;

public class HealthHandler implements MessageHandler {
    private MessageHandler nextHandler;

    private final Player player;

    public HealthHandler(Player player) {
        this.player = player;
    }

    @Override
    public void handle(String indicator, int value) {
        damagePlayerChance();
        if (indicator.contains("HP")) {
            player.setHp(player.getHp() + value);
            player.getListener().hpChange(value);
        } else if (nextHandler != null) {
            nextHandler.handle(indicator, value);
        }
    }

    @Override
    public void setNextHandler(MessageHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    private void damagePlayerChance() {
        double chance = Math.random();

        if (chance < 0.2 && player.getHp() > 1) {
            int damageAmount = 1;
            player.setHp(player.getHp() - damageAmount);
        }
    }
}
