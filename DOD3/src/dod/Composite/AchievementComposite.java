package dod.Composite;

import dod.iterator.IterableCollection;
import dod.iterator.Iterator;
import dod.iterator.AchievementsIterator;
import dod.visitor.AchievementVisitor;

import java.util.List;

public class AchievementComposite extends Achievement implements IterableCollection<Achievement> {
    private List<Achievement> items;

    public AchievementComposite(Achievement... items){
        this.items = List.of(items);
    }

    public void add(Achievement item){
        this.items.add(item);
    }

    public void remove(Achievement item){
        this.items.remove(item);
    }

    @Override
    public void unlock(String identifier) {
        for (Achievement item : items) {
            item.unlock(identifier);
        }
    }

    @Override
    public String getIdentifier() {
        return "Composite";
    }

    public List<Achievement> getChildren(){
        return this.items;
    }

    @Override
    public Iterator<Achievement> getIterator() {
        return new AchievementsIterator(this);
    }

    @Override
    public void accept(AchievementVisitor visitor) {
        for (Achievement item : items) {
            item.accept(visitor);
        }
    }
}
