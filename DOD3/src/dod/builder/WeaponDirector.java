package dod.builder;

public class WeaponDirector {
    WeaponBuilder weaponBuilder;

    public WeaponDirector(WeaponBuilder weaponBuilder){
        this.weaponBuilder = weaponBuilder;
    }

    public Weapon build(){
        System.out.println("Weapon Director: building weapon");
        return weaponBuilder
                .buildDamage()
                .buildRange()
                .buildType()
                .build();
    }
}
