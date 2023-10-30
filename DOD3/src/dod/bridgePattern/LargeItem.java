package dod.bridgePattern;

public class LargeItem implements IItemType {
    @Override
    public int getValue() { return 3; }

    @Override
    public String getName() { return "Large"; }
}
