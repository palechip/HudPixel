package com.palechip.hudpixelmod.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.games.Warlords;

import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class WarlordsDamageChatFilter {
    public static final String take = "\u00AB";
    public static final String give = "\u00BB";
    public static final String healing = " healed ";

    public WarlordsDamageChatFilter() {
    }

    public void onChat(ClientChatReceivedEvent e) {
        // only if we are in a Warlords game
        if(HudPixelMod.instance().gameDetector.getCurrentGame() instanceof Warlords) {
            // check if the filter is enabled
            if(HudPixelConfig.warlordsFilterDamageDone > 0 || HudPixelConfig.warlordsFilterDamageTaken > 0 || HudPixelConfig.warlordsFilterHealingDone > 0 || HudPixelConfig.warlordsFilterHealingReceived > 0) {
                String message = e.message.getUnformattedText();
                // incoming
                if(message.startsWith(take)) {
                    // healing
                    if(message.contains(healing)) {
                        if(HudPixelConfig.warlordsFilterHealingReceived > getDamageOrHealthValue(message)) {
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
                    // damage
                    else  {
                        if(HudPixelConfig.warlordsFilterDamageDone > getDamageOrHealthValue(message)) {
                            e.setCanceled(true);
                        }
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
            String result = m.group();
            // and cast it into an integer (without whitespace)
            return Integer.valueOf(result.replace(" ", ""));
        } catch(Exception e) {
            HudPixelMod.instance().logDebug("Failed to extract damage from this message: " + message);
        }
        // We failed :(
        return Integer.MAX_VALUE;
    }

}
