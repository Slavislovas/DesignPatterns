package dod.BotLogic;

import lombok.Getter;

@Getter
public class CurrentGold {

    private int value;

    public CurrentGold(int value) {
        this.setValue(value);
    }

    public void setValue(int value) {
        this.value = value;
    }
}
