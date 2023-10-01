package dod.game.items.lantern;

public class MediumLantern extends Lantern {
    @Override
    public boolean isRetainable() {
        return true;
    }

    @Override
    public int lookDistanceIncrease() {
        return 2;
    }

    @Override
    public String toString() {
        return "Medium lantern";
    }
}
