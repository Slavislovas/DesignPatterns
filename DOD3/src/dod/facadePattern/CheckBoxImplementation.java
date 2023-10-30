package dod.facadePattern;

import javax.swing.*;

class CheckBoxImplementation extends CheckBox {

    private final JCheckBox checkBox;

    CheckBoxImplementation() { checkBox = new JCheckBox(); }

    @Override
    JCheckBox asJCheckBox() { return checkBox; }

    @Override
    public void setCheckBoxText(String text) { checkBox.setText(text); }

    @Override
    public boolean isCheckBoxSelected() { return checkBox.isSelected(); }

    @Override
    public void setCheckBoxSelected(boolean value) { checkBox.setSelected(value); }

    @Override
    public String toString() { return checkBox.getText(); }
}
