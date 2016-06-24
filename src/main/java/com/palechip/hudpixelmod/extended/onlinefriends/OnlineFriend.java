package com.palechip.hudpixelmod.extended.onlinefriends;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.EnumChatFormatting;

import static com.palechip.hudpixelmod.extended.util.RenderUtils.renderBox;

/******************************************************************************
 * HudPixelExtended by unaussprechlich(github.com/unaussprechlich/HudPixelExtended),
 * an unofficial Minecraft Mod for the Hypixel Network.
 * <p>
 * Original version by palechip (github.com/palechip/HudPixel)
 * "Reloaded" version by PixelModders -> Eladkay (github.com/PixelModders/HudPixel)
 * <p>
 * Copyright (c) 2016 unaussprechlich and contributors
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
public class OnlineFriend {


    private String username;
    private String gamemode;

    public OnlineFriend(String username, String gamemode){
        this.gamemode = gamemode;
        this.username = username;
    }


    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    public String getGamemode() {return gamemode;}
    public void setGamemode(String gamemode) {this.gamemode = gamemode;}

    public void renderOnlineFriend(float xStart, float yStart, FontRenderer fontRenderer){
        renderBox(xStart, yStart, 20, 20, 0);
        fontRenderer.drawStringWithShadow(EnumChatFormatting.GOLD + username,xStart + 22, yStart, 0xffffff);
        fontRenderer.drawStringWithShadow(EnumChatFormatting.GREEN + gamemode,xStart + 22, yStart + 10, 0xffffff);
    }
}
