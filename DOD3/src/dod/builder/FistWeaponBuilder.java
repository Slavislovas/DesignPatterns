package dod.builder;

public class FistWeaponBuilder implements WeaponBuilder {
    private final Weapon weapon;

    public FistWeaponBuilder(){
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
