package dod.Composite;

import lombok.Data;

@Data
public class MovementAchievement extends Achievement{
    public MovementAchievement(String identifier){
        super.setIdentifier(identifier);
        super.setUnlocked(false);
    }

    @Override
    public void unlock(String identifier) {
        if (!isUnlocked() && identifier.equals(getIdentifier())){
            System.out.println("Achieved " + identifier);
            setUnlocked(true);
        }
    }
}
