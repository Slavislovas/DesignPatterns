package dod.builder;

import dod.proxy.WeaponBuilderType;

public class WeaponDirector implements IWeaponDirector {
    WeaponBuilder weaponBuilder;

    public WeaponDirector() { }

    @Override
    public WeaponDirector setBuilder(String type) {
        switch(type) {
            case WeaponBuilderType.FIST:
                this.weaponBuilder = new FistWeaponBuilder();
                break;
            case WeaponBuilderType.SWORD:
                this.weaponBuilder = new SwordWeaponBuilder();
                break;
            default:
                System.out.println("Incorrect builder type");
        }
        return this;
    }

    public WeaponDirector setBuilder(WeaponBuilder builder) {
        this.weaponBuilder = builder;
        return this;
    }

    @Override
    public WeaponBuilder getBuilder() {
        return this.weaponBuilder;
    }

    @Override
    public Weapon build(String name){
        if (weaponBuilder != null) {
            return weaponBuilder
                    .buildDamage()
                    .buildRange()
                    .buildType()
                    .build();
        }
        System.out.println("Weapon builder is not initialized.");
        return null;
    }
}
