package dod.facadePattern;

import javax.swing.*;
import java.awt.*;

public abstract class Panel {

    abstract JPanel asJPanel();

    public abstract void setPanelLayout(String layoutType, Integer rows, Integer columns);

    public abstract void setPanelLayout(String layoutType);

    public abstract void addButton(Button element, int x, int y, int fill, Insets insets);

    public abstract void addButton(Button element, int x, int y, int width);

    public abstract void addButton(Button element, int x, int y);

    public abstract void addButton(Button element);

    public abstract void addPanel(Panel element, int x, int y, int fill, Insets insets);

    public abstract void addPanel(Panel element, int x, int y, int width);

    public abstract void addLabel(Label element, int x, int y);

    public abstract void addLabel(Label element);

    public abstract void addTextField(TextField element, int x, int y, int width);

    public abstract void removeAllElements();

    public abstract void setPanelOpaque(boolean value);

    public abstract void setPanelVisible(boolean value);

    public abstract void addScrollFeed(TextArea textArea, int width, int height);
}
