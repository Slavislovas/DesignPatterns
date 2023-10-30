package dod.facadePattern;

import javax.swing.*;

class RadioButtonImplementation extends RadioButton {

    private final JRadioButton radioButton;

    RadioButtonImplementation() { radioButton = new JRadioButton(); }

    @Override
    JRadioButton asJRadioButton() { return radioButton; }

    @Override
    public void setRadioButtonText(String text) { radioButton.setText(text); }

    @Override
    public void setRadioButtonToolTipText(String text) { radioButton.setToolTipText(text); }

    @Override
    public boolean isRadioButtonSelected() { return radioButton.isSelected(); }
}
