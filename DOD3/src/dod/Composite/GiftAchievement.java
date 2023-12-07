package dod.Composite;

public class GiftAchievement extends Achievement{
    public GiftAchievement(String identifier){
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
