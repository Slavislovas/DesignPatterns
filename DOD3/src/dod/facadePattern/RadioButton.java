package dod.facadePattern;

import javax.swing.*;

public abstract class RadioButton {
    abstract JRadioButton asJRadioButton();

    public abstract void setRadioButtonText(String text);

    public abstract void setRadioButtonToolTipText(String text);

    public abstract String getRadioButtonToolTipText();

    public abstract void setRadioButtonSelected(boolean value);

    public abstract boolean isRadioButtonSelected();

    public abstract String toString();
}
