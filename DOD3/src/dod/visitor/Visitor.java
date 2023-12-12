package dod.visitor;

import dod.Composite.AttackAchievement;
import dod.Composite.GiftAchievement;
import dod.Composite.MovementAchievement;
import lombok.Getter;

public class Visitor implements AchievementVisitor {
    @Getter
    private int totalPoints = 0;
    @Override
    public void visit(AttackAchievement attackAchievement) {
        if (attackAchievement.isUnlocked()) {
            System.out.println("Processing Attack Achievement");
            int score = calculateAttackScore(attackAchievement);
            System.out.println("Attack Achievement Score: " + score);
            totalPoints += score;
        }
    }

    @Override
    public void visit(GiftAchievement giftAchievement) {
        if (giftAchievement.isUnlocked()) {
            System.out.println("Processing Gift Achievement");
            int score = calculateGiftScore(giftAchievement);
            System.out.println("Gift Achievement Score: " + score);
            totalPoints += score;
        }
    }

    @Override
    public void visit(MovementAchievement movementAchievement) {
        if (movementAchievement.isUnlocked()) {
            System.out.println("Processing Movement Achievement");
            int score = calculateMovementScore(movementAchievement);
            System.out.println("Movement Achievement Score: " + score);
            totalPoints += score;
        }
    }

    private int calculateAttackScore(AttackAchievement attackAchievement) {
        int baseScore = 10;
        int multiplier = attackAchievement.isUnlocked() ? 2 : 1;
        return baseScore * multiplier;
    }

    private int calculateGiftScore(GiftAchievement giftAchievement) {
        int baseScore = 5;
        int additionalScore = giftAchievement.isUnlocked() ? 3 : 0;
        return baseScore + additionalScore;
    }

    private int calculateMovementScore(MovementAchievement movementAchievement) {
        int baseScore = 8;
        int deduction = movementAchievement.isUnlocked() ? 0 : 8;
        return baseScore - deduction;
    }
}