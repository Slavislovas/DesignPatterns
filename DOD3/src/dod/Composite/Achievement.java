package dod.Composite;

import dod.visitor.AchievementVisitor;
import lombok.Data;

@Data
public abstract class Achievement {
    private String identifier;
    private boolean unlocked;

    abstract void unlock(String identifier);
    abstract void accept(AchievementVisitor visitor);
}
