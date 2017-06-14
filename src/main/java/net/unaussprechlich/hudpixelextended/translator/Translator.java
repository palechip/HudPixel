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

package net.unaussprechlich.hudpixelextended.translator;

import eladkay.hudpixel.config.CCategory;
import eladkay.hudpixel.config.ConfigPropertyBoolean;
import eladkay.hudpixel.config.ConfigPropertyString;
import eladkay.hudpixel.util.ChatMessageComposer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.unaussprechlich.hudpixelextended.HudPixelExtendedEventHandler;
import net.unaussprechlich.hudpixelextended.util.IEventHandler;
import net.unaussprechlich.hudpixelextended.util.LoggerHelper;

public class Translator implements IEventHandler, ITranslatorRequestCallback {

    @ConfigPropertyBoolean(category = CCategory.TRANSLATOR, id = "disableTranslator", comment = "Disables the Translator", def = false)
    private static boolean disableTranslator = false;
    @ConfigPropertyString(category = CCategory.TRANSLATOR, id = "language", comment = "Sets the language for the Translator. { en = english, de = german, fr = french, ...}.", def = "en")
    private static String language = "en";

    private static Translator INSTANCE;
    private static final String TAG = TextFormatting.DARK_GRAY + "[Hud" + TextFormatting.GOLD + "Translator" + TextFormatting.DARK_GRAY + "]";

    public static Translator getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new Translator();
        return INSTANCE;
    }

    public static String getLanguage() {
        return language;
    }

    private Translator() {
        //TODO: Autogenerated Singletone
    }

    public void init() {
        HudPixelExtendedEventHandler.registerIEvent(this);
    }

    @Override
    public void onChatReceivedMessage(ClientChatReceivedEvent e, final String m) throws Throwable {
        if (disableTranslator) return;

    }

    private String getUsername(String m) {
        String[] singleWords = m.split(" ");
        for (String s : singleWords) {
            if (s.contains(":"))
                return s.replace(":", "");
        }
        return "ERROR";
    }


    @Override
    public void translatorCallback(TranslatorRequest tR) {
        try {
            if (tR.isFailed())
                new ChatMessageComposer(TAG + TextFormatting.DARK_RED + "[FAILED]" + tR.getMessage()).send(false);

            if (tR.getMessage().equalsIgnoreCase(tR.getResponse().get("text").getAsString())) {
                LoggerHelper.logInfo("[HudTranslator] messages were identical!");
                return;
            } else if (tR.getResponse().get("from").getAsJsonObject().get("language").getAsJsonObject().get("iso").getAsString().equalsIgnoreCase(language)) {
                LoggerHelper.logInfo("[HudTranslator] same language!");
                return;
            }

            new ChatMessageComposer(TAG + TextFormatting.YELLOW + tR.getUsername() + TextFormatting.WHITE + ": "
                    + TextFormatting.GRAY + TextFormatting.ITALIC + tR.getResponse().get("text").getAsString()).send(false);
        } catch (NullPointerException e) {
            LoggerHelper.logInfo("[HudTranslator] NullPointer, we love it :)!");
            e.printStackTrace();
            new ChatMessageComposer(TAG + TextFormatting.DARK_RED + "[FAILED]" + tR.getMessage()).send(false);
        }

    }
}
