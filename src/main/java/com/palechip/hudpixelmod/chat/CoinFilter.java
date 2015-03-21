package com.palechip.hudpixelmod.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.HudPixelMod;

import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class CoinFilter {
    public CoinFilter() {   
    }
    public void onChat(ClientChatReceivedEvent e) {
        if (HudPixelConfig.filterCoins>0) {
            String messege = e.message.getUnformattedText();
            if (messege.startsWith("+")&&messege.contains("oins")) {
                if (HudPixelConfig.filterCoins > getCoinsFromMessage(messege)) {
                    e.setCanceled(true);
                }
            }
        }
    }
    public static int getCoinsFromMessage(String message) {
        try {
            Pattern p = Pattern.compile("[0-9]\\w+");
            Matcher m = p.matcher(message);
            if(!m.find()) {
                return Integer.MAX_VALUE;
            }
            String coins = m.group();
            return Integer.valueOf(coins);
        } catch (Exception e) {
            HudPixelMod.instance().logDebug("Failed to extract coins from this message: " + message);
        }
        return Integer.MAX_VALUE;
    }
}
