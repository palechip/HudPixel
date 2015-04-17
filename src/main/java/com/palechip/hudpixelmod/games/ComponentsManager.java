package com.palechip.hudpixelmod.games;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.components.IComponent;

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

    public ArrayList<IComponent> getComponentInstances(GameConfiguration configuration) {
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
                        //TODO: check if the config option for the component is turned on
                        
                        // check if we have a instatiation configuration for this component
                        if(this.instances.containsKey(component)) {
                            try {
                            // get all instances
                            for(ComponentInstanceConfiguration config : this.instances.get(component)) {
                                instanceList.add(config.getInstance());
                            }
                            logger.logDebug("Successfully instatiated the component " + component + " using its configuration.");
                            } catch (Exception e) {
                                logger.logError("Failed to instanciate component \"" + component + "\" using its configuration.");
                            }
                        } else {
                            try {
                                // instantiate the component with the empty default constructor
                                instanceList.add((IComponent) this.components.get(component).newInstance());
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
