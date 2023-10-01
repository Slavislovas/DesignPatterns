package dod.game.items.lantern;

public class StrongLantern extends Lantern {
    @Override
    public boolean isRetainable() {
        return true;
    }

    @Override
    public int lookDistanceIncrease() {
        return 3;
    }

    @Override
    public String toString() {
        return "Strong lantern";
    }
}
