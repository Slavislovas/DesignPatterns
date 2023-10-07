package dod.Adapter;

import dod.game.items.lantern.Lantern;

public class FancyLanternAdapter extends Lantern {
    private final FancyLantern fancyLantern = new FancyLantern();

    @Override
    public boolean isRetainable() {
        return fancyLantern.isRetainableLantern();
    }

    @Override
    public int lookDistanceIncrease() {
        return fancyLantern.lookDistanceIncreaseLantern();
    }

    @Override
    public String toString() {
        return fancyLantern.toStringLantern();
    }
}
