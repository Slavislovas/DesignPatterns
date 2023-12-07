package dod.iterator;

import dod.Composite.Achievement;
import dod.Composite.AchievementComposite;

import java.util.Stack;

public class UnlockedAchievementIterator implements Iterator<Achievement>{
    private final AchievementComposite root;
    private final Stack<java.util.Iterator<Achievement>> stack;

    public UnlockedAchievementIterator(AchievementComposite root) {
        this.root = root;
        this.stack = new Stack<>();
        stack.push(root.getChildren().iterator());
    }

    @Override
    public boolean hasNext() {
        while (!stack.isEmpty()) {
            java.util.Iterator<Achievement> currentIterator = stack.peek();
            if (currentIterator.hasNext()) {
                return true;
            } else {
                stack.pop();
            }
        }
        return false;
    }

    @Override
    public Achievement next() {
        if (!hasNext()) {
            throw new IllegalStateException("No more unlocked achievements.");
        }

        java.util.Iterator<Achievement> currentIterator = stack.peek();
        Achievement nextAchievement = currentIterator.next();

        while (nextAchievement instanceof AchievementComposite) {
            stack.push(((AchievementComposite) nextAchievement).getChildren().iterator());
            nextAchievement = stack.peek().next();
        }

        return nextAchievement;
    }

    @Override
    public Achievement first() {
        java.util.Iterator<Achievement> iterator = root.getChildren().iterator();
        stack.push(iterator);

        if (stack.isEmpty()) {
            throw new IllegalStateException("No unlocked achievements.");
        }

        return stack.peek().next();
    }
}