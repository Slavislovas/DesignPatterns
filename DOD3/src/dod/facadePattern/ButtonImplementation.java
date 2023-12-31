package dod.facadePattern;

import javax.swing.*;
import java.awt.event.ActionListener;

class ButtonImplementation extends Button {
    private final JButton button;

    ButtonImplementation() { button = new JButton(); }

    @Override
    JButton asJButton() { return button; }

    @Override
    public void setButtonText(String text) { button.setText(text); }

    @Override
    public void setButtonToolTipText(String text) { button.setToolTipText(text); }

    @Override
    public void addButtonActionListener(ActionListener actionListener) { button.addActionListener(actionListener); }

    @Override
    public ActionListener[] getButtonActionListeners() { return button.getActionListeners(); }

    @Override
    public void setButtonIcon(ImageIcon imageIcon) { button.setIcon(imageIcon); }

    @Override
    public void setButtonContentAreaFilled(boolean value) { button.setContentAreaFilled(value); }

    @Override
    public boolean isButtonContentAreaFilled() { return button.isContentAreaFilled(); }

    @Override
    public void setButtonBorderPainted(boolean value) { button.setBorderPainted(value); }

    @Override
    public boolean isButtonBorderPainted() { return button.isBorderPainted(); }

    @Override
    public String toString() { return button.getText(); }

    @Override
    public String tooltipToString() { return button.getToolTipText(); }
}
