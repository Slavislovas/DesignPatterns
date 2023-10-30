package dod.facadePattern;

import javax.swing.*;
import java.awt.*;

class PanelImplementation extends Panel {

    private final JPanel panel;

    PanelImplementation() { panel = new JPanel(); }

    JPanel asJPanel() { return panel; }

    @Override
    public void setPanelLayout(String layoutType, Integer rows, Integer columns) {
        var layout = switch (layoutType) {
            case LayoutTypes.Grid -> {
                if (rows == null || columns == null) {
                    rows = 1;
                    columns = 0;
                }
                yield new GridLayout(rows, columns);
            }
            case LayoutTypes.GridBag -> new GridBagLayout();
            case LayoutTypes.Flow -> new FlowLayout();
            default -> throw new IllegalArgumentException("Layout type is not supported");
        };
        panel.setLayout(layout);
    }

    @Override
    public void setPanelLayout(String layoutType) {
        var layout = switch (layoutType) {
            case LayoutTypes.Grid -> new GridLayout();
            case LayoutTypes.GridBag -> new GridBagLayout();
            case LayoutTypes.Flow -> new FlowLayout();
            default -> throw new IllegalArgumentException("Layout type is not supported");
        };
        panel.setLayout(layout);
    }

    @Override
    public void addButton(Button element, int x, int y, int fill, Insets insets) {
        if (panel.getLayout() instanceof GridBagLayout)
        {
            var constrains = new GridBagConstraints();
            constrains.gridx = x;
            constrains.gridy = y;
            constrains.fill = fill;
            constrains.insets = insets;
            panel.add(element.asJButton(), constrains);
            return;
        }
        panel.add(element.asJButton());
    }

    @Override
    public void addButton(Button element, int x, int y, int width) {
        if (panel.getLayout() instanceof GridBagLayout)
        {
            var constrains = new GridBagConstraints();
            constrains.gridx = x;
            constrains.gridy = y;
            constrains.gridwidth = width;
            panel.add(element.asJButton(), constrains);
            return;
        }
        panel.add(element.asJButton());
    }

    @Override
    public void addButton(Button element, int x, int y) {
        if (panel.getLayout() instanceof GridBagLayout)
        {
            var constrains = new GridBagConstraints();
            constrains.gridx = x;
            constrains.gridy = y;
            panel.add(element.asJButton(), constrains);
            return;
        }
        panel.add(element.asJButton());
    }

    @Override
    public void addButton(Button element) { panel.add(element.asJButton()); }

    @Override
    public void addPanel(Panel element, int x, int y, int fill, Insets insets) {
        if (panel.getLayout() instanceof GridBagLayout)
        {
            var constrains = new GridBagConstraints();
            constrains.gridx = x;
            constrains.gridy = y;
            constrains.fill = fill;
            constrains.insets = insets;
            panel.add(element.asJPanel(), constrains);
            return;
        }
        panel.add(element.asJPanel());
    }

    @Override
    public void addPanel(Panel element, int x, int y, int width) {
        if (panel.getLayout() instanceof GridBagLayout)
        {
            var constrains = new GridBagConstraints();
            constrains.gridx = x;
            constrains.gridy = y;
            constrains.gridwidth = width;
            panel.add(element.asJPanel(), constrains);
            return;
        }
        panel.add(element.asJPanel());
    }

    @Override
    public void addLabel(Label element, int x, int y) {
        if (panel.getLayout() instanceof GridBagLayout)
        {
            var constrains = new GridBagConstraints();
            constrains.gridx = x;
            constrains.gridy = y;
            panel.add(element.asJLabel(), constrains);
            return;
        }
        panel.add(element.asJLabel());
    }

    @Override
    public void addLabel(Label element) { panel.add(element.asJLabel()); }

    @Override
    public void addTextField(TextField element, int x, int y, int width) {
        if (panel.getLayout() instanceof GridBagLayout)
        {
            var constrains = new GridBagConstraints();
            constrains.gridx = x;
            constrains.gridy = y;
            constrains.gridwidth = width;
            panel.add(element.asJTextField(), constrains);
            return;
        }
        panel.add(element.asJTextField());
    }

    @Override
    public void removeAllElements() { panel.removeAll(); }

    @Override
    public void setPanelOpaque(boolean value) { panel.setOpaque(value); }

    @Override
    public void setPanelVisible(boolean value) { panel.setVisible(value); }

    @Override
    public void addScrollFeed(TextArea textArea, int width, int height) {
        var scrollFeed = new JScrollPane(textArea.asJTextArea(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollFeed.setPreferredSize(new Dimension(400, 500));

        panel.add(scrollFeed);
    }
}
