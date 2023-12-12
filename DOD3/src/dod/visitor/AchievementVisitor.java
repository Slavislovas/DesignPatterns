package dod.visitor;

import dod.Composite.AttackAchievement;
import dod.Composite.GiftAchievement;
import dod.Composite.MovementAchievement;

public interface AchievementVisitor {
    void visit(AttackAchievement attackAchievement);
    void visit(GiftAchievement giftAchievement);
    void visit(MovementAchievement movementAchievement);
}
