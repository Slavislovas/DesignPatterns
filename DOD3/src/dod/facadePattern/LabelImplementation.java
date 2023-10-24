package dod.facadePattern;

import javax.swing.*;

class LabelImplementation extends Label {

    private final JLabel label;

    LabelImplementation() { label = new JLabel(); }

    JLabel asJLabel() { return label; }

    @Override
    public void setLabelText(String text) { label.setText(text); }

    @Override
    public void setLabelIcon(ImageIcon imageIcon) { label.setIcon(imageIcon); }

    @Override
    public void setLabelVisible(boolean value) { label.setVisible(value); }

    @Override
    public boolean isVisible() { return label.isVisible(); }

    @Override
    public String toString() { return label.getText(); }
}
