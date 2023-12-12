package dod.builder;

public interface WeaponBuilder {
    String getBuilderType();
    WeaponBuilder buildDamage();
    WeaponBuilder buildRange();
    WeaponBuilder buildType();
    Weapon build();
}
