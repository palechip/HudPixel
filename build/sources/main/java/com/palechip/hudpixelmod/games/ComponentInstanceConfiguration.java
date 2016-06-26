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

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.components.IComponent;

public class ComponentInstanceConfiguration {
    Class compenent;
    Set<Integer> ids;
    Object[] paramValues;
    Constructor<?> constructor;

    public ComponentInstanceConfiguration(Class component,JsonObject instance) {
        try {
            this.compenent = component;
            ids = new HashSet<Integer>();

            // get all ids
            for(JsonElement id : instance.get("ids").getAsJsonArray()) {
                ids.add(id.getAsInt());
            }

            // get all parameters
            ArrayList<Class> types = new ArrayList<Class>();
            ArrayList<Object> values = new ArrayList<Object>();

            for(JsonElement paramElement : instance.get("params").getAsJsonArray()) {
                // it should be an Object
                JsonObject param = paramElement.getAsJsonObject();
                // get the strings saved in the object
                String typeStr = param.get("type").getAsString();
                String valueStr = param.get("value").getAsString();

                Class type;
                Object value;

                // go through the possible types
                if(typeStr.equalsIgnoreCase("boolean") || typeStr.equalsIgnoreCase("bool")) {
                    // boolean
                    type = boolean.class;
                    value = Boolean.parseBoolean(valueStr);
                } else if(typeStr.equalsIgnoreCase("integer") || typeStr.equalsIgnoreCase("int")) {
                    // integer
                    type = int.class;
                    value = Integer.parseInt(valueStr);
                } else if(typeStr.equalsIgnoreCase("string")) {
                    // string
                    type = String.class;
                    value = valueStr;
                } else {
                    // inner static classes of the component. (e.g. enums) MUST provide a function called valueOf(String)

                    // get the class
                    type = Class.forName(component.getName() + "$" + typeStr);

                    // now use the valueOf method to get the value
                    value = type.getMethod("valueOf", String.class).invoke(null, valueStr);

                }

                // add the parameter to the temporary lists
                types.add(type);
                values.add(value);
            }

            // get the constructor based of the types
            this.constructor = component.getConstructor(types.toArray(new Class[types.size()]));

            // save the values as an Object array to pass it on instatiation
            this.paramValues = values.toArray();

        } catch (Exception e) {
            HudPixelMod.instance().logError("Failed to create a component instance configuration. This instance will be left out.");
            e.printStackTrace();
            // make sure it isn't used by clearing the ids set
            this.ids.clear();
        }
    }

    /**
     * Checks if the instance should be created for the given game.
     * @return true if the modid is in the ids list or if the modid 0 is in the ids list.
     */
    public boolean isForGame(int modID) {
        return ids.contains(modID) || ids.contains(0);
    }
    /**
     * Creates an instance of the component using the pre-defined configuration.
     * @return A working instance of the component.
     * @throws Exception If something doesn't go right, there will be an Exception.
     */
    public IComponent getInstance() throws Exception{
        return (IComponent)this.constructor.newInstance(this.paramValues);
    }
}
