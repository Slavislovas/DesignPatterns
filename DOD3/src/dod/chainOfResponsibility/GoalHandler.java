package dod.chainOfResponsibility;

import dod.GUI.PlayerGUI;

public class GoalHandler implements MessageHandler {
    private MessageHandler nextHandler;

    @Override
    public void handle(String message, PlayerGUI playerGUI) {
        if (message.startsWith("GOAL")) {
            playerGUI.getGoalLabel().setLabelText(message);
        } else if (nextHandler != null) {
            nextHandler.handle(message, playerGUI);
        }
    }

    @Override
    public void setNextHandler(MessageHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
}