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
package com.palechip.hudpixelmod.components;

import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.chat.WarlordsDamageChatFilter;
import net.minecraft.util.EnumChatFormatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
    Use @link com.palechip.hudpixelmod.modulargui.components.WarlordsDamageAndHealingCounterModularGuiProvider
 */
@Deprecated
public class WarlordsDamageAndHealingCounter implements IComponent {
    public enum Type {Damage, Healing};
    
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

    @Override
    public String getConfigName() {
        return "DamageAndHealthCounter";
    }

    @Override
    public String getConfigComment() {
        return "Counts the damage and healing you do in a %game game.";
    }

    @Override
    public boolean getConfigDefaultValue() {
        return true;
    }

}
