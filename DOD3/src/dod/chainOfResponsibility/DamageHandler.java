package dod.chainOfResponsibility;


import dod.game.Player;

import java.util.Random;

public class DamageHandler implements MessageHandler {
    private MessageHandler nextHandler;
    private final Player player;

    public DamageHandler(Player player) {
        this.player = player;
    }

    @Override
    public void handle(String indicator, int value) {
        modifyWeaponChance();
        if (indicator.contains("Damage")) {
            player.setHp(player.getHp() - value);
            player.getListener().damage(value);
        } else if (nextHandler != null) {
            nextHandler.handle(indicator, value);
        }
    }

    @Override
    public void setNextHandler(MessageHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    private void modifyWeaponChance() {
        double chance = Math.random();

        if (chance < 0.1) {
            Random random = new Random();
            int damageAmount = random.nextInt(10) + 1;
            player.getWeapon().setDamage(damageAmount);
        }
    }
}
