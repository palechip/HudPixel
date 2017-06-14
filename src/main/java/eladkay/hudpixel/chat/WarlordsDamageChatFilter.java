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

package eladkay.hudpixel.chat;


import eladkay.hudpixel.GameDetector;
import eladkay.hudpixel.HudPixelMod;
import eladkay.hudpixel.config.CCategory;
import eladkay.hudpixel.config.ConfigPropertyBoolean;
import eladkay.hudpixel.config.ConfigPropertyInt;
import eladkay.hudpixel.util.GameType;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WarlordsDamageChatFilter {
    public static final String take = "\u00AB";
    public static final String give = "\u00BB";
    public static final String healing = " healed ";
    public static final String absorption = " absorbed ";
    public static final String wounded1 = "You are now wounded.";
    public static final String wounded2 = "You are wounded.";
    public static final String noLongerWounded = "You are no longer wounded.";

    @ConfigPropertyBoolean(category = CCategory.WARLORDS, id = "warlordsFilterWounded", comment = "Warlords Filter Wounded", def = true)
    public static boolean warlordsFilterWounded = false;
    @ConfigPropertyBoolean(category = CCategory.WARLORDS, id = "warlordsFilterAbsorbtion", comment = "Warlords Filter Absorbtion", def = true)
    public static boolean warlordsFilterAbsorbtion = false;
    @ConfigPropertyInt(category = CCategory.WARLORDS, id = "warlordsFilterDamageDone", comment = "Warlords Filter Damage Done", def = 0)
    public static int warlordsFilterDamageDone = 0;
    @ConfigPropertyInt(category = CCategory.WARLORDS, id = "warlordsFilterHealingDone", comment = "Warlords Filter Healing Done", def = 0)
    public static int warlordsFilterHealingDone = 0;
    @ConfigPropertyInt(category = CCategory.WARLORDS, id = "warlordsFilterDamageTaken", comment = "Warlords Filter Damage Taken", def = 0)
    public static int warlordsFilterDamageTaken = 0;
    @ConfigPropertyInt(category = CCategory.WARLORDS, id = "warlordsFilterHealingReceived", comment = "Warlords Filter Healing Received", def = 0)
    public static int warlordsFilterHealingReceived = 0;

    public WarlordsDamageChatFilter() {
    }

    /**
     * @return The damage or health. Int.MAX_VALUE in case of failure.
     */
    public static int getDamageOrHealthValue(String message) {
        try {
            // filter !, which highlights critical damage/health
            message = message.replace("!", "");

            // do some regex magic
            Pattern p = Pattern.compile("\\s[0-9]+\\s");
            Matcher m = p.matcher(message);
            if (!m.find()) // We failed :(
                return Integer.MAX_VALUE;
            // Get the last occurrence in order to sort out party poopers with all number names
            String result = m.group(m.groupCount());
            // and cast it into an integer (without whitespace)
            return Integer.valueOf(result.replace(" ", ""));
        } catch (Exception e) {
            HudPixelMod.Companion.instance().logDebug("Failed to extract damage from this message: " + message);
        }
        // We failed :(
        return Integer.MAX_VALUE;
    }

    public void onChat(ClientChatReceivedEvent e) {
        // only if we are in a Warlords game
        if (GameDetector.getCurrentGameType().equals(GameType.WARLORDS)) {
            // isHypixelNetwork if the filter is enabled
            if (warlordsFilterDamageDone > 0 || warlordsFilterDamageTaken > 0 || warlordsFilterHealingDone > 0 || warlordsFilterHealingReceived > 0 || warlordsFilterAbsorbtion || warlordsFilterWounded) {
                String message = e.getMessage().getUnformattedText();
                // incoming
                if (message.startsWith(take))
                    messageFilter(e, message);

                // outgoing
                else if (message.startsWith(give))
                    messageFilter(e, message);

                //Filter wounded messages
                if (warlordsFilterWounded)
                    if (message.equals(wounded1) || message.equals(wounded2) || message.equals(noLongerWounded))
                        e.setCanceled(true);
            }
        }
    }

    private void messageFilter(ClientChatReceivedEvent e, String message) {
        if (message.contains(healing))
            if (warlordsFilterHealingReceived > getDamageOrHealthValue(message))
                e.setCanceled(true);

         else if (message.contains(absorption))
            if (warlordsFilterAbsorbtion)
                e.setCanceled(true);

        else
            if (warlordsFilterDamageTaken > getDamageOrHealthValue(message))
                e.setCanceled(true);
    }

}
