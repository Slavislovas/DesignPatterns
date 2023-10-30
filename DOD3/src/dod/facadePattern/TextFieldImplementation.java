package dod.facadePattern;

import javax.swing.*;

class TextFieldImplementation extends TextField {

    private final JTextField textField;

    TextFieldImplementation() { textField = new JTextField(); }

    @Override
    JTextField asJTextField() { return textField; }

    @Override
    public void setTextFieldText(String text) { textField.setText(text); }

    @Override
    public void setTextFieldColumns(int columns) { textField.setColumns(columns); }

    @Override
    public String toString() { return textField.getText(); }
}
