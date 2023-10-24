package dod.facadePattern;

class ButtonGroupImplementation extends ButtonGroup {

    private final javax.swing.ButtonGroup buttonGroup;

    ButtonGroupImplementation() { buttonGroup = new javax.swing.ButtonGroup(); }

    @Override
    public void addRadioButton(RadioButton element, boolean isSelected) {
        buttonGroup.add(element.asJRadioButton());
        element.asJRadioButton().setSelected(isSelected);
    }
}
