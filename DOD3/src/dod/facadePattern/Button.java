package dod.facadePattern;

import javax.swing.*;
import java.awt.event.ActionListener;

public abstract class Button {

    abstract JButton asJButton();

    public abstract void setButtonText(String text);

    public abstract void setButtonToolTipText(String text);

    public abstract void addButtonActionListener(ActionListener actionListener);

    public abstract void setButtonIcon(ImageIcon imageIcon);

    public abstract void setButtonContentAreaFilled(boolean value);

    public abstract void setButtonBorderPainted(boolean value);
}
