package dod.game.items.lantern;

public class WeakLantern extends Lantern {
    @Override
    public boolean isRetainable() {
        return true;
    }

    @Override
    public int lookDistanceIncrease() {
        return 1;
    }

    @Override
    public String toString() {
        return "Weak lantern";
    }
}
