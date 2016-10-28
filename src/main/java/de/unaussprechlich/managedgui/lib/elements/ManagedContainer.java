package de.unaussprechlich.managedgui.lib.elements;

import de.unaussprechlich.managedgui.lib.config.ConfigManager;

/* *****************************************************************************

 * Copyright (c) 2016 unaussprechlich

 *******************************************************************************/
public abstract class ManagedContainer extends Container {

    private final String ID;

    public enum ManagedContainerDefaultProperties{

        X_OFFSET("xOffset"),
        Y_OFFSET("yOffset"),
        WIDTH("width"),
        HEIGHT("height"),
        COLOR_R("colorRed"),
        COLOR_B("colorBlue"),
        COLOR_G("colorGreen"),
        COLOR_A("colorAlpha"),
        FLOAT_X("floatX"),
        FLOAT_Y("floatY");

        final String name;
        ManagedContainerDefaultProperties(String name){
            this.name = name;
        }
    }

    public ManagedContainer(String name) {
        this.ID = name;
        ConfigManager.registerCategory(name);
    }

    public <T> void registerProperty(String name, T defaultValue){
        ConfigManager.getCategoryByName(ID).registerProperty(name, defaultValue);
    }

    public <T> T getProperty(String name){
        return ConfigManager.getCategoryByName(ID).getPopertyValueByName(name);
    }
}
