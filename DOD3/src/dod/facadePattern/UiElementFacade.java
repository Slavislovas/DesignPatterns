package dod.facadePattern;

import java.awt.*;
import java.awt.event.ActionListener;

public class UiElementFacade implements IUiElementFacade {

    public Button CreateButton(String text, String tooltip, ActionListener actionListener) {
        var button = new ButtonImplementation();
        if (text != null) {
            button.setButtonText(text);
        }
        if (tooltip != null) {
            button.setButtonToolTipText(tooltip);
        }
        if (actionListener != null) {
            button.addButtonActionListener(actionListener);
        }
        return button;
    }

    public Label CreateLabel(String text) {
        var label = new LabelImplementation();
        if (text != null) {
            label.setLabelText(text);
        }
        return label;
    }

    public TextField CreateTextField(String text, Integer columns) {
        var textField = new TextFieldImplementation();
        if (text != null) {
            textField.setTextFieldText(text);
        }
        if (columns != null) {
            textField.setTextFieldColumns(columns);
        }
        return textField;
    }

    public CheckBox CreateCheckBox(String text) {
        var checkBox = new CheckBoxImplementation();
        if (text != null) {
            checkBox.setCheckBoxText(text);
        }
        return checkBox;
    }

    public RadioButton CreateRadioButton(String text) {
        var radioButton = new RadioButtonImplementation();
        if (text != null) {
            radioButton.setRadioButtonText(text);
        }
        return radioButton;
    }

    public TextArea CreateTextArea() { return new TextAreaImplementation(); }

    public Panel CreatePanel() { return new PanelImplementation(); }


    public Canvas CreateCanvas(Container container) { return new CanvasImplementation(container); }

    public ButtonGroup CreateButtonGroup() { return new ButtonGroupImplementation(); }
}
