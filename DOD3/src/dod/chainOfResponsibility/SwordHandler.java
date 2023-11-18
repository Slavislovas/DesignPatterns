package dod.chainOfResponsibility;

import dod.GUI.PlayerGUI;

public class SwordHandler implements MessageHandler {
    private MessageHandler nextHandler;

    @Override
    public void handle(String message, PlayerGUI playerGUI) {
        if (message.equals("You equip Sword")) {
            playerGUI.getSwordLabel().setLabelVisible(true);
        } else if (nextHandler != null) {
            nextHandler.handle(message, playerGUI);
        }
    }

    @Override
    public void setNextHandler(MessageHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
}
