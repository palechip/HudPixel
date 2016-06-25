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
package com.palechip.hudpixelmod.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.palechip.hudpixelmod.config.HudPixelConfig;
import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.util.GameType;

import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class WarlordsDamageChatFilter {
    public static final String take = "\u00AB";
    public static final String give = "\u00BB";
    public static final String healing = " healed ";
    public static final String absorption = " absorbed ";
    public static final String wounded1 = "You are now wounded.";
    public static final String wounded2 = "You are wounded.";
    public static final String noLongerWounded = "You are no longer wounded.";

    public WarlordsDamageChatFilter() {
    }

    public void onChat(ClientChatReceivedEvent e) {
        // only if we are in a Warlords game
        if(HudPixelMod.instance().gameDetector.getCurrentGame().equals(GameType.WARLORDS)) {
            // check if the filter is enabled
            if(HudPixelConfig.warlordsFilterDamageDone > 0 || HudPixelConfig.warlordsFilterDamageTaken > 0 || HudPixelConfig.warlordsFilterHealingDone > 0 || HudPixelConfig.warlordsFilterHealingReceived > 0 || HudPixelConfig.warlordsFilterAbsorbtion || HudPixelConfig.warlordsFilterWounded) {
                String message = e.message.func_150260_c();
                // incoming
                if(message.startsWith(take)) {
                    // healing
                    if(message.contains(healing)) {
                        if(HudPixelConfig.warlordsFilterHealingReceived > getDamageOrHealthValue(message)) {
                            e.setCanceled(true);
                        }
                    }
                    // absorption
                    else if(message.contains(absorption)) {
                        if(HudPixelConfig.warlordsFilterAbsorbtion) {
                            e.setCanceled(true);
                        }
                    }
                    // damage
                    else  {
                        if(HudPixelConfig.warlordsFilterDamageTaken > getDamageOrHealthValue(message)) {
                            e.setCanceled(true);
                        }
                    }
                }
                // outgoing
                else if(message.startsWith(give)) {
                    // healing
                    if(message.contains(healing)) {
                        if(HudPixelConfig.warlordsFilterHealingDone > getDamageOrHealthValue(message)) {
                            e.setCanceled(true);
                        }
                    }
                    // absorption
                    else if(message.contains(absorption)) {
                        if(HudPixelConfig.warlordsFilterAbsorbtion) {
                            e.setCanceled(true);
                        }
                    }
                    // damage
                    else  {
                        if(HudPixelConfig.warlordsFilterDamageDone > getDamageOrHealthValue(message)) {
                            e.setCanceled(true);
                        }
                    }
                }
                //Filter wounded messages
                if (HudPixelConfig.warlordsFilterWounded) {
                    if (message.equals(wounded1) || message.equals(wounded2) || message.equals(noLongerWounded)) {
                        e.setCanceled(true);
                    }
                }
            }
        }
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
            if(!m.find()) {
                // We failed :(
                return Integer.MAX_VALUE;
            }
            // Get the last occurrence in order to sort out party poopers with all number names
            String result = m.group(m.groupCount());
            // and cast it into an integer (without whitespace)
            return Integer.valueOf(result.replace(" ", ""));
        } catch(Exception e) {
            HudPixelMod.instance().logDebug("Failed to extract damage from this message: " + message);
        }
        // We failed :(
        return Integer.MAX_VALUE;
    }

}
