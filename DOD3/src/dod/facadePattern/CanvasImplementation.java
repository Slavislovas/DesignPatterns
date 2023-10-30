package dod.facadePattern;

import java.awt.*;

class CanvasImplementation extends Canvas {
    private final Container canvas;

    CanvasImplementation(Container container) { canvas = container; }

    @Override
    public void setCanvasLayout(String layoutType) {
        var layout = switch (layoutType) {
            case LayoutTypes.Grid -> new GridLayout();
            case LayoutTypes.GridBag -> new GridBagLayout();
            case LayoutTypes.Flow -> new FlowLayout();
            default -> throw new IllegalArgumentException("Layout type is not supported");
        };
        canvas.setLayout(layout);
    }

    @Override
    public void removeAllElements() { canvas.removeAll(); }

    @Override
    public void setCanvasBackground(Color color) { canvas.setBackground(color); }

    @Override
    public void addLabel(Label element, int x, int y, int width, int fill, Insets insets) {
        if (canvas.getLayout() instanceof GridBagLayout)
        {
            var constrains = new GridBagConstraints();
            constrains.gridx = x;
            constrains.gridy = y;
            constrains.gridwidth = width;
            constrains.fill = fill;
            constrains.insets = insets;
            canvas.add(element.asJLabel(), constrains);
            return;
        }
        canvas.add(element.asJLabel());
    }

    @Override
    public void addLabel(Label element, int x, int y) {
        if (canvas.getLayout() instanceof GridBagLayout)
        {
            var constrains = new GridBagConstraints();
            constrains.gridx = x;
            constrains.gridy = y;
            canvas.add(element.asJLabel(), constrains);
            return;
        }
        canvas.add(element.asJLabel());
    }

    @Override
    public void addPanel(Panel element, int x, int y, int fill, Insets insets) {
        if (canvas.getLayout() instanceof GridBagLayout)
        {
            var constrains = new GridBagConstraints();
            constrains.gridx = x;
            constrains.gridy = y;
            constrains.fill = fill;
            constrains.insets = insets;
            canvas.add(element.asJPanel(), constrains);
            return;
        }
        canvas.add(element.asJPanel());
    }

    @Override
    public void addPanel(Panel element, int x, int y) {
        if (canvas.getLayout() instanceof GridBagLayout)
        {
            var constrains = new GridBagConstraints();
            constrains.gridx = x;
            constrains.gridy = y;
            canvas.add(element.asJPanel(), constrains);
            return;
        }
        canvas.add(element.asJPanel());
    }

    @Override
    public void addPanel(Panel element) { canvas.add(element.asJPanel()); }

    @Override
    public void addButton(Button element, int x, int y, int width) {
        if (canvas.getLayout() instanceof GridBagLayout)
        {
            var constrains = new GridBagConstraints();
            constrains.gridx = x;
            constrains.gridy = y;
            constrains.gridwidth = width;
            canvas.add(element.asJButton(), constrains);
            return;
        }
        canvas.add(element.asJButton());
    }

    @Override
    public void addButton(Button element, int x, int y) {
        if (canvas.getLayout() instanceof GridBagLayout)
        {
            var constrains = new GridBagConstraints();
            constrains.gridx = x;
            constrains.gridy = y;
            canvas.add(element.asJButton(), constrains);
            return;
        }
        canvas.add(element.asJButton());
    }

    @Override
    public void addTextField(TextField element, int x, int y) {
        if (canvas.getLayout() instanceof GridBagLayout)
        {
            var constrains = new GridBagConstraints();
            constrains.gridx = x;
            constrains.gridy = y;
            canvas.add(element.asJTextField(), constrains);
            return;
        }
        canvas.add(element.asJTextField());
    }

    @Override
    public void addCheckBox(CheckBox element, int x, int y, int width) {
        if (canvas.getLayout() instanceof GridBagLayout)
        {
            var constrains = new GridBagConstraints();
            constrains.gridx = x;
            constrains.gridy = y;
            constrains.gridwidth = width;
            canvas.add(element.asJCheckBox(), constrains);
            return;
        }
        canvas.add(element.asJCheckBox());
    }

    @Override
    public void addRadioButton(RadioButton element, int x, int y) {
        if (canvas.getLayout() instanceof GridBagLayout)
        {
            var constrains = new GridBagConstraints();
            constrains.gridx = x;
            constrains.gridy = y;
            canvas.add(element.asJRadioButton(), constrains);
            return;
        }
        canvas.add(element.asJRadioButton());
    }

    @Override
    public void repaintCanvas() { canvas.repaint(); }
}
