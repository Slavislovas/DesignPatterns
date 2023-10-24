package dod.facadePattern;

import javax.swing.*;

public abstract class CheckBox {

    abstract JCheckBox asJCheckBox();

    public abstract void setCheckBoxText(String text);

    public abstract boolean isCheckBoxSelected();

}
