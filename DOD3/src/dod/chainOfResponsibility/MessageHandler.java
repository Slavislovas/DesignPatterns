package dod.chainOfResponsibility;

public interface MessageHandler {
    void handle(String indicator, int value);
    void setNextHandler(MessageHandler nextHandler);
}
