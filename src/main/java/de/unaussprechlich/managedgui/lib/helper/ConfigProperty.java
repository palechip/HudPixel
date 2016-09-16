package de.unaussprechlich.managedgui.lib.helper;

/**
 * Created by kecka on 06.09.2016.
 */
public class ConfigProperty<T> {

    private T property;
    private final String name;

    public ConfigProperty(String name , T property){
        this.name = name;
        this.property = property;
    }


}
