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

package com.palechip.hudpixelmod.extended.onlinefriends;

import com.palechip.hudpixelmod.extended.util.ILoadPlayerHeadCallback;
import com.palechip.hudpixelmod.extended.util.LoadPlayerHead;
import com.palechip.hudpixelmod.extended.util.McColorHelper;
import com.palechip.hudpixelmod.extended.util.gui.FancyListObject;
import net.minecraft.util.ResourceLocation;


public class OnlineFriend extends FancyListObject implements ILoadPlayerHeadCallback, McColorHelper{

    private String username;
    private String gamemode;
    private boolean isOnline;

    String getUsername() {return username;}
    String getGamemode() {return gamemode;}
    void setGamemode(String gamemode) {this.gamemode = gamemode;}
    void setOnline(Boolean isOnline) {this.isOnline = isOnline;}
    public boolean isOnline() {return isOnline;}

    /**
     * Constructor ... also loads the playerhead
     * @param username Username
     * @param gamemode current string to render
     */
    OnlineFriend(String username, String gamemode){
        this.gamemode = gamemode;
        this.username = username;

        this.resourceLocation = null;

        this.renderLine1 = GRAY + "● " + GOLD + username;
        this.renderLine2 = gamemode;
        this.renderLineSmall = GRAY + "● " + YELLOW + username;
        this.renderPicture = WHITE + "";

        new LoadPlayerHead(username, this);
    }

    @Override
    public void onClientTick(){
        if(isOnline) this.renderLine1 = GREEN + "● " + GOLD + username;
        else         this.renderLine1 = D_RED + "● " + GOLD + username;
        this.renderLine2 = gamemode;
        if(isOnline) this.renderLineSmall = GREEN + "● " + YELLOW + username;
        else         this.renderLineSmall = D_RED + "● " + YELLOW + username;
    }

    @Override
    public void onLoadPlayerHeadResponse(ResourceLocation resourceLocation) {
        if(resourceLocation != null){
            this.resourceLocation = resourceLocation;
        }
    }
}



