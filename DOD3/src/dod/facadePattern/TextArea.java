package dod.facadePattern;

import javax.swing.*;

public abstract class TextArea {

    abstract JTextArea asJTextArea();

    public abstract void setTextAreaText(String text);

    public abstract void setTextAreaEditable(boolean value);

    public abstract void setTextAreaLineWrap(boolean value);

    public abstract void setTextAreaWrapStyleWord(boolean value);

    public abstract void appendText(String text);

    public abstract void setTextAreaCaretPosition(int length);

    public abstract int getTextAreaLength();

    public abstract String toString();

}
