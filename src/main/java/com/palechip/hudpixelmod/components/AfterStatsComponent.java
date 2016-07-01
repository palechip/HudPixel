/*******************************************************************************
 * HudPixel Reloaded (github.com/palechip/HudPixel), an unofficial Minecraft Mod for the Hypixel Network
 * <p>
 * Copyright (c) 2014-2015 palechip (twitter.com/palechip) and contributors
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package com.palechip.hudpixelmod.components;

import com.palechip.hudpixelmod.util.GameType;
import net.minecraftforge.fml.client.FMLClientHandler;

/*
    No real need for it anymore
 */
@Deprecated
public class AfterStatsComponent {

    private GameType type;

    public AfterStatsComponent(int modid) {
        this.type = GameType.getTypeByID(modid);
    }

    public void onChatMessage(String textMessage, String formattedMessage) {
        String username = FMLClientHandler.instance().getClient().getSession().getUsername();
        switch (this.type) {
            case BLITZ:
                if (textMessage.contains(username + " was killed")) {
                    if (textMessage.contains(" by ")) {
                        String killer;
                        String messageStartingWithKiller = textMessage.substring(textMessage.indexOf("by ") + 3);
                        if (messageStartingWithKiller.contains(" ")) {
                            killer = messageStartingWithKiller.substring(0, messageStartingWithKiller.indexOf(' ')).replace("!", "");
                        } else {
                            killer = messageStartingWithKiller.replace("!", "");


                        }
                    }
                }
        }
    }
}