package dod.builder;

import java.util.Objects;

public class SwordBuilder implements WeaponBuilder{
    private Weapon weapon;

    public SwordBuilder(){
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
