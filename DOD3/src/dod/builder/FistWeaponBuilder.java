package dod.builder;

import dod.proxy.WeaponBuilderType;

public class FistWeaponBuilder implements WeaponBuilder {
    private final Weapon weapon;

    public FistWeaponBuilder(){
        System.out.println("Fist builder created");
        weapon = new Weapon();
    }

    @Override
    public String getBuilderType() {
        return WeaponBuilderType.FIST;
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
