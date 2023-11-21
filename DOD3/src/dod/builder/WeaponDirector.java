package dod.builder;

public class WeaponDirector {
    WeaponBuilder weaponBuilder;

    public WeaponDirector(WeaponBuilder weaponBuilder){
        this.weaponBuilder = weaponBuilder;
    }

    public Weapon build(){
        return weaponBuilder
                .buildDamage()
                .buildRange()
                .buildType()
                .build();
    }
}
