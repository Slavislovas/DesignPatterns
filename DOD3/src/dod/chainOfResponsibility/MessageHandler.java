package dod.chainOfResponsibility;

import dod.GUI.PlayerGUI;

public interface MessageHandler {
    void handle(String message, PlayerGUI playerGUI);
    void setNextHandler(MessageHandler nextHandler);
}
