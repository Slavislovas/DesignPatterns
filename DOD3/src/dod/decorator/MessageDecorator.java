package dod.decorator;

import dod.game.Player;

public class MessageDecorator extends PlayerDecorator {

    public MessageDecorator(Player player) {
        super(player);
    }

    @Override
    public void sendMessage(String message) {
        System.out.println("Sending a decorated message!");
        super.sendMessage("[Decorated] " + message); // Decorating the message
    }
}
