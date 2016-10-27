package de.unaussprechlich.managedgui.lib.config;

import de.unaussprechlich.managedgui.lib.exceptions.NameInUseException;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.HashMap;

/******************************************************************************
 * HudPixelExtended by unaussprechlich(github.com/unaussprechlich/HudPixelExtended), 
 * an unofficial Minecraft Mod for the Hypixel Network.
 *
 * Original version by palechip (github.com/palechip/HudPixel)
 * "Reloaded" version by PixelModders -> Eladkay (github.com/PixelModders/HudPixel)
 *
 * Copyright (c) 2016 unaussprechlich and contributors
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
public class ConfigManager {

    private static Configuration config;

    public static HashMap<String, ConfigCategory> categories = new HashMap<>();

    public ConfigManager(String modid){
        config = new Configuration(new File(Minecraft.getMinecraft().mcDataDir.toString() + "/" + modid + "/ManagedGuiLibConfig.txt"));
        config.load();
        loadCategories();
        config.save();
    }

    public static Configuration getConfig() {
        return config;
    }

    static <T> void registerProperty(String name, T defaultValue, String category){
        config.load();
        if(defaultValue.getClass() == boolean.class){
            ConfigManager.getConfig().get(category, name, (Boolean)defaultValue);
        } else if(defaultValue.getClass() == int.class){
            ConfigManager.getConfig().get(category, name, (Integer)defaultValue);
        } else if(defaultValue.getClass() == double.class){
            ConfigManager.getConfig().get(category, name, (Double)defaultValue);
        }
        config.save();
    }

    private static void loadCategories(){
        categories.clear();
        for (String key : config.getCategoryNames()){
            categories.put(key, new ConfigCategory(key));
        }
    }

    public static ConfigCategory getCategoryByName(String name){
        return categories.get(name);
    }

    public static void registerCategory(String name) throws NameInUseException{
        if(categories.containsKey(name)){
            throw new NameInUseException(name, "ConfigCategories");
        } else {
            config.load();
            config.getCategory(name);
            config.save();
        }
    }






}
