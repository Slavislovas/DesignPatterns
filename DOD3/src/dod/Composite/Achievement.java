package dod.Composite;

import lombok.Data;

@Data
public abstract class Achievement {
    private String identifier;
    private boolean unlocked;

    abstract void unlock(String identifier);
}
