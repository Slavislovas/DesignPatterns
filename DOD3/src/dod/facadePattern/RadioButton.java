package dod.facadePattern;

import javax.swing.*;

public abstract class RadioButton {
    abstract JRadioButton asJRadioButton();

    public abstract void setRadioButtonText(String text);

    public abstract void setRadioButtonToolTipText(String text);

    public abstract boolean isRadioButtonSelected();
}
