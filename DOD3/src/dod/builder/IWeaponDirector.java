package dod.builder;

import dod.proxy.WeaponBuilderType;

public interface IWeaponDirector {

    IWeaponDirector setBuilder(String type);
    WeaponBuilder getBuilder();
    Weapon build(String name);
}
