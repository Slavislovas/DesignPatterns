package dod.facadePattern;

import javax.swing.*;
import java.awt.*;

public abstract class Canvas {

    public abstract void setCanvasLayout(String layoutType);

    public abstract void removeAllElements();

    public abstract void setCanvasBackground(Color color);

    public abstract void addLabel(Label element, int x, int y, int width, int fill, Insets insets);

    public abstract void addLabel(Label element, int x, int y);

    public abstract void addPanel(Panel element, int x, int y, int fill, Insets insets);

    public abstract void addPanel(Panel element, int x, int y);

    public abstract void addPanel(Panel element);

    public abstract void addButton(Button element, int x, int y, int width);

    public abstract void addButton(Button element, int x, int y);

    public abstract void addTextField(TextField element, int x, int y);

    public abstract void addCheckBox(CheckBox element, int x, int y, int width);

    public abstract void addRadioButton(RadioButton element, int x, int y);

    public abstract void repaintCanvas();
}
