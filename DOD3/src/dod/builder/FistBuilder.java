package dod.builder;

public class FistBuilder implements WeaponBuilder {
    private final Weapon weapon;

    public FistBuilder(){
        weapon = new Weapon();
    }

    @Override
    public WeaponBuilder buildDamage() {
        weapon.setDamage(1);
        return this;
    }

    @Override
    public WeaponBuilder buildRange() {
        weapon.setRange(1);
        return this;
    }

    @Override
    public WeaponBuilder buildType() {
        weapon.setType(WeaponType.FIST);
        return this;
    }

    @Override
    public Weapon build() {
        return weapon;
    }
}
