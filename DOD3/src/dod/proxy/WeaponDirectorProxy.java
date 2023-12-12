package dod.proxy;

import dod.builder.IWeaponDirector;
import dod.builder.Weapon;
import dod.builder.WeaponBuilder;
import dod.builder.WeaponDirector;

import java.util.ArrayList;
import java.util.Objects;

public class WeaponDirectorProxy implements IWeaponDirector {

    WeaponDirector weaponDirector;
    ArrayList<WeaponBuilder> builders = new ArrayList<>();

    @Override
    public WeaponDirectorProxy setBuilder(String type) {
        InstantiateDirector();
        for (WeaponBuilder builder : builders) {
            if (builder.getBuilderType().equals(type)) {
                weaponDirector.setBuilder(builder);
                return this;
            }
        }
        WeaponBuilder builder = weaponDirector.setBuilder(type).getBuilder();
        builders.add(builder);
        return this;
    }

    @Override
    public WeaponBuilder getBuilder() {
        InstantiateDirector();
        return weaponDirector.getBuilder();
    }

    @Override
    public Weapon build(String name) {
        InstantiateDirector();
        Weapon weapon = weaponDirector.build(null);
        if (Objects.equals(name, "STRONGMAN")) {
            weapon.setDamage(weapon.getDamage() + 5);
        }
        return weapon;
    }

    void InstantiateDirector() {
        if (this.weaponDirector == null) {
            System.out.println("WeaponDirector created");
            this.weaponDirector = new WeaponDirector();
        }
    }
}
