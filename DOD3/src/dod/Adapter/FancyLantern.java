package dod.Adapter;

public class FancyLantern {

    public boolean isRetainableLantern() {
        return true;
    }

    public int lookDistanceIncreaseLantern() {
        return 5;  // Assume FancyLantern provides more light
    }

    public String toStringLantern() {
        return "Fancy lantern";
    }

    public boolean isDurableLantern() {
        return true;
    }
}
