package dod.facadePattern;

import javax.swing.*;

public abstract class Label{

    abstract JLabel asJLabel();

    public abstract void setLabelText(String text);

    public abstract void setLabelIcon(ImageIcon imageIcon);

    public abstract void setLabelVisible(boolean value);

    public abstract boolean isVisible();

    public abstract String toString();
}
