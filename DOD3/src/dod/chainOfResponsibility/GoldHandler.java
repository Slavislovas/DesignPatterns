package dod.chainOfResponsibility;

import dod.GUI.PlayerGUI;

public class GoldHandler implements MessageHandler {
    private MessageHandler nextHandler;

    @Override
    public void handle(String message, PlayerGUI playerGUI) {
        if (message.startsWith("TREASUREMOD ")) {
            playerGUI.setCurrentGold(playerGUI.getCurrentGold() + playerGUI.getGoldChange(message.substring(12).replace(" ", "")));
            playerGUI.getCurrentGoldLabel().setLabelText("GOLD " + playerGUI.getCurrentGold());
        } else if (nextHandler != null) {
            nextHandler.handle(message, playerGUI);
        }
    }

    @Override
    public void setNextHandler(MessageHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
}
