package test.unit;

import dod.facadePattern.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class FacadePatternTests {

    private IUiElementFacade uiElementFacade;

    private Button button;

    private CheckBox checkBox;

    private TextField textField;

    private RadioButton radioButton;

    @BeforeEach
    void init(){
        uiElementFacade = new UiElementFacade();
        button = uiElementFacade.CreateButton("text", "tooltip", action -> {});
        button.setButtonBorderPainted(false);
        button.setButtonContentAreaFilled(false);
        checkBox = uiElementFacade.CreateCheckBox("text");
        checkBox.setCheckBoxSelected(false);
        textField = uiElementFacade.CreateTextField("text", 1);
        radioButton = uiElementFacade.CreateRadioButton("text");
        radioButton.setRadioButtonToolTipText("tooltip");
        radioButton.setRadioButtonSelected(false);
    }

    @Test
    void addRadioButton_success(){
        var button = uiElementFacade.CreateRadioButton(null);
        var buttonGroup = uiElementFacade.CreateButtonGroup();
        assertEquals(buttonGroup.getRadioButtonCount(), 0);
        buttonGroup.addRadioButton(button, true);
        assertEquals(buttonGroup.getRadioButtonCount(), 1);
    }

    @Test
    void setButtonText_success(){
        assertEquals(button.toString(), "text");
        button.setButtonText("test");
        assertEquals(button.toString(), "test");
    }

    @Test
    void setButtonToolTipText_success(){
        assertEquals(button.tooltipToString(), "tooltip");
        button.setButtonToolTipText("test");
        assertEquals(button.tooltipToString(), "test");
    }

    @Test
    void addButtonActionListener_success(){
        assertEquals(Arrays.stream(button.getButtonActionListeners()).count(), 1);
        button.addButtonActionListener(action -> { });
        assertEquals(Arrays.stream(button.getButtonActionListeners()).count(), 2);
    }

    @Test
    void setButtonContentAreaFilled_success(){
        assertFalse(button.isButtonContentAreaFilled());
        button.setButtonContentAreaFilled(true);
        assertTrue(button.isButtonContentAreaFilled());
    }

    @Test
    void setButtonBorderPainted_success(){
        assertFalse(button.isButtonBorderPainted());
        button.setButtonBorderPainted(true);
        assertTrue(button.isButtonBorderPainted());
    }

    @Test
    void setCheckBoxText_success(){
        assertEquals(checkBox.toString(), "text");
        checkBox.setCheckBoxText("test");
        assertEquals(checkBox.toString(), "test");
    }

    @Test
    void setCheckBoxSelected_success(){
        assertFalse(checkBox.isCheckBoxSelected());
        checkBox.setCheckBoxSelected(true);
        assertTrue(checkBox.isCheckBoxSelected());
    }

    @Test
    void setTextFieldText_success(){
        assertEquals(textField.toString(), "text");
        textField.setTextFieldText("test");
        assertEquals(textField.toString(), "test");
    }

    @Test
    void setRadioButtonText_success(){
        assertEquals(radioButton.toString(), "text");
        radioButton.setRadioButtonText("test");
        assertEquals(radioButton.toString(), "test");
    }

    @Test
    void setRadioButtonTooltipText_success(){
        assertEquals(radioButton.getRadioButtonToolTipText(), "tooltip");
        radioButton.setRadioButtonToolTipText("test");
        assertEquals(radioButton.getRadioButtonToolTipText(), "test");
    }

    @Test
    void setRadioButtonSelected_success(){
        assertFalse(radioButton.isRadioButtonSelected());
        radioButton.setRadioButtonSelected(true);
        assertTrue(radioButton.isRadioButtonSelected());
    }

}