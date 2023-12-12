package dod.builder;

import dod.proxy.WeaponBuilderType;

public class SwordWeaponBuilder implements WeaponBuilder {
    private final Weapon weapon;

    public SwordWeaponBuilder() {
        System.out.println("Sword builder created");
        this.weapon = new Weapon();
    }

    @Override
    public String getBuilderType() {
        return WeaponBuilderType.SWORD;
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
