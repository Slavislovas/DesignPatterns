package dod.builder;

public class SwordWeaponBuilder implements WeaponBuilder {
    private final Weapon weapon;

    public SwordWeaponBuilder() {
        this.weapon = new Weapon();
    }

    @Override
    public WeaponBuilder buildDamage() {
        weapon.setDamage(2);
        return this;
    }

    @Override
    public WeaponBuilder buildRange() {
        weapon.setRange(2);
        return this;
    }

    @Override
    public WeaponBuilder buildType() {
        weapon.setType(WeaponType.SWORD);
        return this;
    }

    @Override
    public Weapon build() {
        return weapon;
    }
}
