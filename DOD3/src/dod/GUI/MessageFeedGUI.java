package dod.GUI;

import dod.facadePattern.*;
import lombok.Getter;

import java.io.Serial;

import javax.swing.JFrame;

/**
 * Represents a generic abstract GUI which supplies some common GUI functions including a message feed
 *
 * @author Benjamin Dring
 */
public abstract class MessageFeedGUI extends JFrame implements ClientListener {
    @Serial
    private static final long serialVersionUID = 663205082653528881L;

    protected final IUiElementFacade uiElementFacade;

    protected Canvas canvas;
    private final Panel messageFeed; //The message feed JPanel

    @Getter
    private final TextArea messageFeedText; //The TextArea contained in the JPanel

    /**
     * The constructor of the class that sets up the Container and the messageFeed
     */
    public MessageFeedGUI() {
        uiElementFacade = new UiElementFacade();

        messageFeed = uiElementFacade.CreatePanel();

        //Making the Text Area Scrollable by using a JPanel contain a scrollable around a JTextArea
        messageFeedText = uiElementFacade.CreateTextArea();
        messageFeedText.setTextAreaEditable(false);
        messageFeedText.setTextAreaLineWrap(true);
        messageFeedText.setTextAreaWrapStyleWord(true);



        //Feed is added
        messageFeed.addScrollFeed(messageFeedText, 400, 500);
        messageFeed.setPanelOpaque(false);

        canvas = uiElementFacade.CreateCanvas(getContentPane());
    }

    /**
     * Adds a given message to the message feed
     *
     * @param message the message to be displayed
     */
    public void addMessageToFeed(String message) {
        if (!messageFeedText.toString().isEmpty()) {
            //Adds two new lines and then the message
            messageFeedText.appendText("\n\n" + message);
        } else {
            messageFeedText.appendText(message);
        }
        //Set scroll to the bottom so new messages can be read
        messageFeedText.setTextAreaCaretPosition(messageFeedText.getTextAreaLength());
    }

    /**
     * Accessor for the MessageFeed
     *
     * @return JPanel the MessageFeed
     */
    protected Panel getMessageFeed() {
        return messageFeed;
    }

    /**
     * The class that will fully display the GUI
     */
    abstract public void displayGUI();

    /**
     * Message Feed Text is wiped and restart message is posted
     */
    @Override
    public void restartGame() {
        messageFeedText.setTextAreaText("");
        addMessageToFeed("Game Reset");
    }


}
