package dod.facadePattern;

import javax.swing.*;
import java.awt.event.ActionListener;

public abstract class Button {

    abstract JButton asJButton();

    public abstract void setButtonText(String text);

    public abstract void setButtonToolTipText(String text);

    public abstract void addButtonActionListener(ActionListener actionListener);

    public abstract ActionListener[] getButtonActionListeners();

    public abstract void setButtonIcon(ImageIcon imageIcon);

    public abstract void setButtonContentAreaFilled(boolean value);

    public abstract boolean isButtonContentAreaFilled();

    public abstract void setButtonBorderPainted(boolean value);

    public abstract boolean isButtonBorderPainted();

    public abstract String toString();

    public abstract String tooltipToString();
}
