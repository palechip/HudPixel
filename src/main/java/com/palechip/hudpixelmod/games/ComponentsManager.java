/*******************************************************************************
 * HudPixel Reloaded (github.com/palechip/HudPixel), an unofficial Minecraft Mod for the Hypixel Network
 *
 * Copyright (c) 2014-2015 palechip (twitter.com/palechip) and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package com.palechip.hudpixelmod.games;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.components.IComponent;
import com.palechip.hudpixelmod.config.HudPixelConfig;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Can instantiate the components for a GameConfiguration.
 * @author palechip
 *
 */
public class ComponentsManager {

    private static final String COMPONENTS_PACKAGE = "com.palechip.hudpixelmod.components"; 

    private HashMap<String, Class> components;
    private HashMap<String, ArrayList<ComponentInstanceConfiguration>> instances;

    public ComponentsManager(ArrayList<GameConfiguration> configurations, JsonArray componentConfigurations) {
        HudPixelMod logger = HudPixelMod.instance();
        components = new HashMap<String, Class>();
        instances = new HashMap<String, ArrayList<ComponentInstanceConfiguration>>();
        // cycle through all configurations
        for(GameConfiguration config : configurations) {
            // and their components
            for(String component : config.getComponents()) {
                // check if the component wasn't looked up
                if(!components.containsKey(component)) {
                    // search for the component in the components package
                    try {
                        Class cls = Class.forName(COMPONENTS_PACKAGE + "." + component);
                        // and add it if it was found.
                        components.put(component, cls);
                        // log for debugging
                        logger.logDebug("Component found: " + component);
                    } catch(ClassNotFoundException e) {
                        logger.logError("MISSING COMPONENT! Failed to load the component called \"" + component + "\". This component will not be accessible.");
                    }
                }
            }
        }

        // read all instance configurations
        for (int i = 0; i < componentConfigurations.size(); i++) {
            try {
                JsonObject component = componentConfigurations.get(i).getAsJsonObject();

                // get the name
                String name = component.get("name").getAsString();

                // collect the list of instances
                ArrayList<ComponentInstanceConfiguration> componentInstances = new ArrayList<ComponentInstanceConfiguration>();
                // fill the list using all objects in the instances array
                for(JsonElement e : component.get("instances").getAsJsonArray()) {
                    // collect the array
                     componentInstances.add(new ComponentInstanceConfiguration(this.components.get(name), e.getAsJsonObject()));
                }
                // add it to the map
                this.instances.put(name,componentInstances);
            } catch (Exception e) {
                logger.logError("Failed to process a component configuration. The component part will not be available.");
            }
        }
    }

    /**
     * Creates instances for all components of a game. Only components enabled by the config will be returned.
     * @param configuration The game for which the instances shall be created.
     */
    public ArrayList<IComponent> getComponentInstances(GameConfiguration configuration) {
        return this.getComponentInstances(configuration, false);
    }
    /**
     * Creates instances for all components of a game.
     * @param configuration The game for which the instances shall be created.
     * @param ignoreConfig If this is true it will return all components regardless if they are enabled or not. (Used by the Config itself)
     */
    public ArrayList<IComponent> getComponentInstances(GameConfiguration configuration, boolean ignoreConfig) {
        ArrayList<IComponent> instanceList = new ArrayList<IComponent>();
        HudPixelMod logger = HudPixelMod.instance();
        // check if the configuration is valid
        if(configuration == GameConfiguration.NULL_GAME) {
            logger.logWarn("Tried to instantiate NULL_GAME's components! This shouldn't happen.");
            // not valid, return the empty list
            return instanceList;
        } else {
            // cycle through the components
            for(String component : configuration.getComponents()) {
                // and check if the component was found
                if(components.containsKey(component)) {
                        // check if we have a instatiation configuration for this component
                        if(this.instances.containsKey(component)) {
                            try {
                            // get all instances
                            for(ComponentInstanceConfiguration config : this.instances.get(component)) {
                                IComponent instance = config.getInstance();
                                if(HudPixelConfig.getConfigValue(configuration.getConfigPrefix() + instance.getConfigName()) || ignoreConfig) {
                                    instanceList.add(instance);
                                }
                            }
                            logger.logDebug("Successfully instatiated the component " + component + " using its configuration.");
                            } catch (Exception e) {
                                logger.logError("Failed to instanciate component \"" + component + "\" using its configuration.");
                            }
                        } else {
                            try {
                                // instantiate the component with the empty default constructor
                                IComponent instance = (IComponent) this.components.get(component).newInstance();
                                // check if the config says it is enabled (can't be done earlier because we need the instance)
                                if(HudPixelConfig.getConfigValue(configuration.getConfigPrefix() + instance.getConfigName()) || ignoreConfig) {
                                    instanceList.add(instance);
                                }
                                logger.logDebug("Successfully instatiated the component " + component + " using the empty constructor.");
                            } catch (Exception e) {
                                logger.logError("Failed to instanciate component \"" + component + "\" using the empty constructor.");
                            }
                        }
                } else {
                    // log the problem and ignore the component
                    logger.logError("Ignoring the component called \"" + component + "\". It will not be available in the game.");
                }
            }
            return instanceList;
        }
    }
}
