package dod.facadePattern;

import java.awt.*;
import java.awt.event.ActionListener;

public interface IUiElementFacade {

    Button CreateButton(String text, String tooltip, ActionListener actionListener);

    Label CreateLabel(String text);

    TextField CreateTextField(String text, Integer columns);

    CheckBox CreateCheckBox(String text);

    RadioButton CreateRadioButton(String text);

    TextArea CreateTextArea();

    Panel CreatePanel();

    Canvas CreateCanvas(Container container);

    ButtonGroup CreateButtonGroup();
}
