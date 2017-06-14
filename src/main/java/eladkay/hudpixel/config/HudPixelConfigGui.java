/*
 * HudPixelReloaded - License
 * <p>
 * The repository contains parts of Minecraft Forge and its dependencies. These parts have their licenses
 * under forge-docs/. These parts can be downloaded at files.minecraftforge.net.This project contains a
 * unofficial copy of pictures from the official Hypixel website. All copyright is held by the creator!
 * Parts of the code are based upon the Hypixel Public API. These parts are all in src/main/java/net/hypixel/api and
 * subdirectories and have a special copyright header. Unfortunately they are missing a license but they are obviously
 * intended for usage in this kind of application. By default, all rights are reserved.
 * The original version of the HudPixel Mod is made by palechip and published under the MIT license.
 * The majority of code left from palechip's creations is the component implementation.The ported version to
 * Minecraft 1.8.9 and up HudPixel Reloaded is made by PixelModders/Eladkay and also published under the MIT license
 * (to be changed to the new license as detailed below in the next minor update).
 * <p>
 * For the rest of the code and for the build the following license applies:
 * <p>
 * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
 * #  HudPixel by PixelModders, Eladkay & unaussprechlich is licensed under a Creative Commons         #
 * #  Attribution-NonCommercial-ShareAlike 4.0 International License with the following restrictions.  #
 * #  Based on a work at HudPixelExtended & HudPixel.                                                  #
 * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
 * <p>
 * Restrictions:
 * <p>
 * The authors are allowed to change the license at their desire. This license is void for members of PixelModders and
 * to unaussprechlich, except for clause 3. The licensor cannot revoke these freedoms in most cases, as long as you follow
 * the following license terms and the license terms given by the listed above Creative Commons License, however in extreme
 * cases the authors reserve the right to revoke all rights for usage of the codebase.
 * <p>
 * 1. PixelModders, Eladkay & unaussprechlich are the authors of this licensed material. GitHub contributors are NOT
 * considered authors, neither are members of the HudHelper program. GitHub contributers still hold the rights for their
 * code, but only when it is used separately from HudPixel and any license header must indicate that.
 * 2. You shall not claim ownership over this project and repost it in any case, without written permission from at least
 * two of the authors.
 * 3. You shall not make money with the provided material. This project is 100% non commercial and will always stay that
 * way. This clause is the only one remaining, should the rest of the license be revoked. The only exception to this
 * clause is completely cosmetic features. Only the authors may sell cosmetic features for the mod.
 * 4. Every single contibutor owns copyright over his contributed code when separated from HudPixel. When it's part of
 * HudPixel, it is only governed by this license, and any copyright header must indicate that. After the contributed
 * code is merged to the release branch you cannot revoke the given freedoms by this license.
 * 5. If your own project contains a part of the licensed material you have to give the authors full access to all project
 * related files.
 * 6. You shall not act against the will of the authors regarding anything related to the mod or its codebase. The authors
 * reserve the right to take down any infringing project.
 */

package eladkay.hudpixel.config;


import eladkay.hudpixel.HudPixelMod;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.unaussprechlich.hudpixelextended.exceptions.IllegalParameterTypeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * HudPixel ConfigGUI
 */
public class HudPixelConfigGui extends GuiConfig {

    /**
     * static HasMap to store the element lists with key categiry
     */
    private static HashMap<CCategory, ArrayList<IConfigElement>> configCategoryMap = new HashMap<>();

    /**
     * Constructor calls super() for the parent GUI
     *
     * @param parent ParentGUI
     */
    public HudPixelConfigGui(GuiScreen parent) {
        super(parent, getConfigElements(), HudPixelMod.MODID, false, false, "HudPixel Config");
    }

    /**
     * a single list for the ConfigGUI
     *
     * @return all ConfigElements in a list
     */
    private static List<IConfigElement> getConfigElements() {
        return configCategoryMap.keySet()
                .stream()
                .map(cCategory -> new DummyConfigElement.DummyCategoryElement(
                        cCategory.getTextFormatting() + cCategory.getName(),
                        "",
                        configCategoryMap.get(cCategory)))
                .collect(Collectors.toList());
    }

    /**
     * Call the method before reloading the  config, so the GUI gets cleared.
     */
    public static void deleteBeforReload() {
        configCategoryMap.clear();
    }


    /**
     * Adds a Element to the ConfigGUI element HasMap. Made it use generics, because generics are cool :)
     * Will throw a IllegalParameterTypeException if the Type is not supported.
     *
     * @param category The category the element will be pushed
     * @param id       The ID the element has in the config
     * @param defEntry The default value of the element
     * @param comment  We all like comments, so we know what option we are really changing.
     * @param <T>      used generics for defEntry, will throw a exception if type is not supported
     */
    public static <T> void addElement(CCategory category, String id, T defEntry, String comment) {

        //adds the category if it doesn't exists
        if (!configCategoryMap.containsKey(category)) {
            configCategoryMap.put(category, new ArrayList<>());
        }

        //generates & puts the element in the right list
        if (defEntry.getClass() == String.class) {
            configCategoryMap.get(category).add((
                    new ConfigElement(HudPixelMod.Companion.getCONFIG().get(category.getName(), id, (String) defEntry, comment))));
        } else if (defEntry.getClass() == Boolean.class) {
            configCategoryMap.get(category).add((
                    new ConfigElement(HudPixelMod.Companion.getCONFIG().get(category.getName(), id, (Boolean) defEntry, comment))));
        } else if (defEntry.getClass() == Integer.class) {
            configCategoryMap.get(category).add((
                    new ConfigElement(HudPixelMod.Companion.getCONFIG().get(category.getName(), id, (Integer) defEntry, comment))));
        } else if (defEntry.getClass() == Double.class) {
            configCategoryMap.get(category).add((
                    new ConfigElement(HudPixelMod.Companion.getCONFIG().get(category.getName(), id, (Double) defEntry, comment))));
        } else if (defEntry.getClass() == Float.class) {
            configCategoryMap.get(category).add((
                    new ConfigElement(HudPixelMod.Companion.getCONFIG().get(category.getName(), id, (Float) defEntry, comment))));
        } else {
            throw new IllegalParameterTypeException(defEntry);
        }
    }


}

