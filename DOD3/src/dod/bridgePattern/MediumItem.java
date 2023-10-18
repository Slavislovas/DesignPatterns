package dod.bridgePattern;

public class MediumItem  implements IItemType {
    @Override
    public int getValue() { return 2; }

    @Override
    public String getName() { return "Medium"; }
}
