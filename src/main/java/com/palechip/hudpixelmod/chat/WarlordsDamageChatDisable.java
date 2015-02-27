package com.palechip.hudpixelmod.chat;

import com.palechip.hudpixelmod.HudPixelConfig;

import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class WarlordsDamageChatDisable {
    public static final String takeDamage = "«";
    public static final String giveDamage = "»";

    public WarlordsDamageChatDisable() {
    }

    public void onChat(ClientChatReceivedEvent e) {
        if (HudPixelConfig.disableWarlordsDamagePos) {
            if (e.message.getUnformattedText().startsWith(giveDamage)) {
                e.setCanceled(true);
            }
        }
        if (HudPixelConfig.disableWarlordsDamageNeg) {
            if (e.message.getUnformattedText().startsWith(takeDamage)) {
                e.setCanceled(true);
            }
        }

    }

}
