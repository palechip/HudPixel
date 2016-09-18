package de.unaussprechlich.managedgui.lib.config;

import java.util.HashMap;

/******************************************************************************

 * Copyright (c) 2016 unaussprechlich

 *******************************************************************************/
public class ConfigCategory {

    private HashMap<String, ConfigProperty> properties = new HashMap<>();

    public ConfigProperty getPopertyByName(String name){
        return properties.get(name);
    }

    public void registerProperty(String name, ConfigProperty property, String description){
    }

}
