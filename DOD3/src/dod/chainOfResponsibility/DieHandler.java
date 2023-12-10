package dod.chainOfResponsibility;

import dod.GUI.PlayerGUI;
import dod.game.Player;

public class DieHandler implements MessageHandler {
    private MessageHandler nextHandler;

    private final Player player;

    public DieHandler(Player player) {
        this.player = player;
    }

    @Override
    public void handle(String indicator, int value) {
        if (indicator.contains("DIE")) {
            player.setHp(value);
        } else if (nextHandler != null) {
            nextHandler.handle(indicator, value);
        }
    }

    @Override
    public void setNextHandler(MessageHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
}
