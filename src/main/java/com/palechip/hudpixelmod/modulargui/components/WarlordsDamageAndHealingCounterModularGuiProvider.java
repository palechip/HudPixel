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

package com.palechip.hudpixelmod.modulargui.components;

import com.palechip.hudpixelmod.GameDetector;
import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.chat.WarlordsDamageChatFilter;
import com.palechip.hudpixelmod.config.CCategory;
import com.palechip.hudpixelmod.config.ConfigPropertyBoolean;
import com.palechip.hudpixelmod.modulargui.HudPixelModularGuiProvider;
import com.palechip.hudpixelmod.util.GameType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WarlordsDamageAndHealingCounterModularGuiProvider extends HudPixelModularGuiProvider {
    @ConfigPropertyBoolean(category = CCategory.WARLORDS, id = "warlordsDamageAndHealthCounter", comment = "The Warlords Damage And Health Tracker", def = true)
    public static boolean enabled = false;
    private Type type;
    private int count;

    public WarlordsDamageAndHealingCounterModularGuiProvider(Type type) {
        this.type = type;
    }

    /**
     * @return The damage or health. 0 in case of failure.
     */
    public static int getDamageOrHealthValue(String message) {
        try {
            // filter !, which highlights critical damage/health
            message = message.replace("!", "");

            // do some regex magic
            Pattern p = Pattern.compile("\\s[0-9]+\\s");
            Matcher m = p.matcher(message);
            if (!m.find()) {
                // We failed :(
                return 0;
            }
            // save the result
            String result = m.group();
            // if there is a second match, we'll use that because the first was an all number username in this case
            if (m.find()) {
                result = m.group();
            }

            // and cast it into an integer (without whitespace)
            return Integer.valueOf(result.replace(" ", ""));
        } catch (Exception e) {
            HudPixelMod.Companion.instance().logDebug("Failed to extract damage from this message: " + message);
        }
        // We failed :(
        return 0;
    }

    @Override
    public boolean doesMatchForGame() {
        return GameDetector.doesGameTypeMatchWithCurrent(GameType.WARLORDS) && enabled;
    }

    @Override
    public void setupNewGame() {
        this.count = 0;
    }

    @Override
    public void onGameStart() {
    }

    @Override
    public void onGameEnd() {
    }

    @Override
    public void onTickUpdate() {
    }

    @Override
    public void onChatMessage(String textMessage, String formattedMessage) {
        // incoming
        if (textMessage.startsWith(WarlordsDamageChatFilter.give)) {
            // healing
            if (this.type == Type.Healing && textMessage.contains(WarlordsDamageChatFilter.healing)) {
                this.count += getDamageOrHealthValue(textMessage);
            }
            // damage
            if (this.type == Type.Damage && !textMessage.contains(WarlordsDamageChatFilter.absorption) && !textMessage.contains(WarlordsDamageChatFilter.healing)) {
                this.count += getDamageOrHealthValue(textMessage);
            }
        }
    }

    public String getRenderingString() {
        // if the played class doens't have access to healing, they won't be bothered.
        if (this.count == 0 && this.type == Type.Healing) {
            return "";
        }

        // format into xxx.x
        double formatted = Math.round(this.count / 100.0) / 10.0;

        //eladkay: yes, I know.
        switch (this.type) {
            case Healing:
                return formatted + "k";
            case Damage:
                return formatted + "k";
        }
        return "";
    }

    @Override
    public boolean showElement() {
        return doesMatchForGame();
    }

    @Override
    public String content() {
        return getRenderingString();
    }

    @Override
    public boolean ignoreEmptyCheck() {
        return false;
    }

    @Override
    public String getAfterstats() {
        if (type == Type.Damage) {
            return YELLOW + "You dealt a total of " + RED + count + " damage.\n";
        } else {
            return YELLOW + "You dealt a total of " + GREEN + count + " healing.";
        }
    }

    public enum Type {Damage, Healing}
}
