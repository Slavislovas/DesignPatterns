package dod.chainOfResponsibility;

import dod.game.Player;

import java.util.Random;

public class ActionPointHandler implements MessageHandler {
    private static final int DEFAULT_AP = 6;
    private static final int AP_PENALTY_PER_ITEM = 1;
    private MessageHandler nextHandler;

    private final Player player;

    public ActionPointHandler(Player player) {
        this.player = player;
    }

    @Override
    public void handle(String indicator, int value) {
        modifyWeaponChance();
        if (indicator.contains("Reset AP")) {
            final int initialAP = DEFAULT_AP - AP_PENALTY_PER_ITEM * player.getItems().size();

            if (initialAP < 0) {
                player.setAp(0);
            }

            player.setAp(initialAP);
        } else if (indicator.contains("Decrement AP")) {
            player.setAp(player.getAp() - value);
            assert player.getAp() >= 0;
        } else if (indicator.contains("Add AP")) {
            player.setAp(player.getAp() + value);
        } else if (indicator.contains("Zero AP")) {
            player.setAp(value);
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
