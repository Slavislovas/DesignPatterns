package dod.facadePattern;

import javax.swing.*;

class TextAreaImplementation extends TextArea {

    private final JTextArea textArea;

    TextAreaImplementation() { textArea = new JTextArea(); }

    @Override
    JTextArea asJTextArea() { return textArea; }

    @Override
    public void setTextAreaText(String text) { textArea.setText(text); }

    @Override
    public void setTextAreaEditable(boolean value) { textArea.setEditable(value); }

    @Override
    public void setTextAreaLineWrap(boolean value) { textArea.setLineWrap(value); }

    @Override
    public void setTextAreaWrapStyleWord(boolean value) { textArea.setWrapStyleWord(value); }

    @Override
    public void appendText(String text) { textArea.append(text); }

    @Override
    public void setTextAreaCaretPosition(int length) { textArea.setCaretPosition(length); }

    @Override
    public int getTextAreaLength() { return textArea.getDocument().getLength(); }

    @Override
    public String toString() { return textArea.getText(); }
}
