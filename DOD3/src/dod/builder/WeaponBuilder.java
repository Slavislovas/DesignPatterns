package dod.builder;

public interface WeaponBuilder {
    WeaponBuilder buildDamage();
    WeaponBuilder buildRange();
    WeaponBuilder buildType();
    Weapon build();
}
