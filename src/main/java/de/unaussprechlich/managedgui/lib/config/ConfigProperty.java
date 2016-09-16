package de.unaussprechlich.managedgui.lib.config;

/******************************************************************************

 * Copyright (c) 2016 unaussprechlich

 *******************************************************************************/
public class ConfigProperty<T> {

    private T property;


    public ConfigProperty( T property){
        this.property = property;
    }

    public T getProperty(){
        return property;
    }


}
