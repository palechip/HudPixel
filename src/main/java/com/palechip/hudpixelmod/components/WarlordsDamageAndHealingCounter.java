package com.palechip.hudpixelmod.components;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.util.EnumChatFormatting;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.chat.WarlordsDamageChatFilter;

public class WarlordsDamageAndHealingCounter implements IComponent {
    public static enum Type {Damage, Healing};
    
    private Type type;
    private int count;

    public WarlordsDamageAndHealingCounter(Type type) {
        this.type = type;
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
        if(textMessage.startsWith(WarlordsDamageChatFilter.give)) {
            // healing
            if(this.type == Type.Healing && textMessage.contains(WarlordsDamageChatFilter.healing)) {
                this.count += getDamageOrHealthValue(textMessage);
            }
            // damage
            if(this.type == Type.Damage && !textMessage.contains(WarlordsDamageChatFilter.absorption) && !textMessage.contains(WarlordsDamageChatFilter.healing)) {
                this.count += getDamageOrHealthValue(textMessage);
            }
        }
    }

    @Override
    public String getRenderingString() {
        // if the played class doens't have access to healing, they won't be bothered.
        if(this.count == 0 && this.type == Type.Healing) {
            return "";
        }
        
        // format into xxx.x
        double formatted = Math.round(this.count / 100.0) / 10.0;
        
        switch(this.type) {
        case Healing:
            return  EnumChatFormatting.GREEN + "Healing: " + formatted + "k";
        case Damage:
            return EnumChatFormatting.RED + "Damage: " + formatted + "k";
        }
        return "";
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
            if(!m.find()) {
                // We failed :(
                return 0;
            }
            // save the result
            String result = m.group();
            // if there is a second match, we'll use that because the first was an all number username in this case
            if(m.find()) {
                result = m.group();
            }
            
            // and cast it into an integer (without whitespace)
            return Integer.valueOf(result.replace(" ", ""));
        } catch(Exception e) {
            HudPixelMod.instance().logDebug("Failed to extract damage from this message: " + message);
        }
        // We failed :(
        return 0;
    }

}
