package dod.bridgePattern;

public class SmallItem implements IItemType {
    @Override
    public int getValue() { return 1; }

    @Override
    public String getName() { return "Small"; }
}
