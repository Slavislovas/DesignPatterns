package dod.facadePattern;

import javax.swing.*;

public abstract class TextField {

    abstract JTextField asJTextField();

    public abstract void setTextFieldText(String text);

    public abstract void setTextFieldColumns(int columns);

    public abstract String toString();
}
