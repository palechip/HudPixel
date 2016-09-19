package de.unaussprechlich.managedgui.lib.config;

import de.unaussprechlich.managedgui.lib.exceptions.NameInUseException;
import net.minecraftforge.common.config.Property;

import java.util.HashMap;

/******************************************************************************

 * Copyright (c) 2016 unaussprechlich

 *******************************************************************************/
public class ConfigCategory {

    public final String category;

    public ConfigCategory(String name){
        this.category = name;
        loadCategoryProperties();
    }

    private HashMap<String, Property> properties = new HashMap<>();

    public <T> T getPopertyValueByName(String name){
        Property prop = properties.get(name);
        switch(prop.getType()){
            case BOOLEAN:return (T) Boolean.valueOf(prop.getBoolean());
            case INTEGER:return (T) Integer.valueOf(prop.getInt());
            case DOUBLE: return (T) Double.valueOf(prop.getDouble());
            default: return null;
        }
    }

    private void loadCategoryProperties(){
        properties.clear();
        net.minecraftforge.common.config.ConfigCategory category = ConfigManager.getConfig().getCategory(this.category);
        for (String key : category.getValues().keySet()){
            properties.put(key, category.get(key));
        }
    }

    public <T> void registerProperty(String name, T defaultValue){
        if(properties.containsKey(name)){
            throw new NameInUseException(name, this.category);
        } else {
            ConfigManager.registerProperty(name, defaultValue, category);
        }

    }

}
