package com.palechip.hudpixelmod.extended.footballdisplay;

import com.palechip.hudpixelmod.extended.util.ILoadPlayerHeadCallback;
import com.palechip.hudpixelmod.extended.util.ImageLoader;
import com.palechip.hudpixelmod.extended.util.LoadPlayerHead;
import com.palechip.hudpixelmod.extended.util.RenderUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import static com.palechip.hudpixelmod.extended.footballdisplay.FootballDisplay.oX;
import static com.palechip.hudpixelmod.extended.footballdisplay.FootballDisplay.oY;
import static com.palechip.hudpixelmod.extended.footballdisplay.FootballDisplay.size;

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
public class FootballPlayer implements ILoadPlayerHeadCallback{

    private float x;
    private float y;
    private boolean isRed;
    private boolean isBlue;
    private  boolean isOwner;
    private EntityPlayer p;
    private ResourceLocation resourceLocation = null;

    FootballPlayer(String username, EntityPlayer p){
        this.p = p;
        this.resourceLocation = ImageLoader.steveHeadLocation();
        new LoadPlayerHead(username, this);
    }

    public void setOwner(){isRed = false;isBlue = false;isOwner = true;}
    public void setBlue() {isRed = false;isBlue = true; isOwner = false;}
    public void setRed()  {isRed = true; isBlue = false;isOwner = false;}

    void onRender(int s, boolean flip){

        this.y = p.getPosition().getX();
        this.x = p.getPosition().getZ();

        final int hs = 10;
        final int bs = 14;

        if(flip){
            if(isRed)           RenderUtils.renderBoxWithColor(oX + size/2 + x*s - bs/2 , oY + size/2 -  y*s + 2 , bs, bs -8 , 0, 1f, 0f, 0f, 1f);
            else if(isBlue)     RenderUtils.renderBoxWithColor(oX + size/2 + x*s - bs/2 , oY + size/2 - y*s + 2 , bs, bs -8, 0, 0f, 0f, 1f, 1f);
            else if(isOwner)    RenderUtils.renderBoxWithColor(oX + size/2 + x*s - bs/2 , oY + size/2 - y*s + 2 , bs, bs -8 , 0, 1f, 1f, 1f, 1f);

            RenderUtils.drawModalRectWithCustomSizedTexture(oX + size/2 + x*s - hs/2 , oY + size/2 - y*s - hs/2, hs, hs, hs, hs, hs, hs,resourceLocation, 1f);
        } else {
            if(isRed)           RenderUtils.renderBoxWithColor(oX + size/2 - x*s - bs/2 , oY + size/2 -  y*s + 2 , bs, bs -8 , 0, 1f, 0f, 0f, 1f);
            else if(isBlue)     RenderUtils.renderBoxWithColor(oX + size/2 - x*s - bs/2 , oY + size/2 -  y*s + 2 , bs, bs -8, 0, 0f, 0f, 1f, 1f);
            else if(isOwner)    RenderUtils.renderBoxWithColor(oX + size/2 - x*s - bs/2 , oY + size/2 -  y*s + 2 , bs, bs -8 , 0, 1f, 1f, 1f, 1f);

            RenderUtils.drawModalRectWithCustomSizedTexture(oX + size/2 - x*s - hs/2 , oY + size/2 - y*s - hs/2, hs, hs, hs, hs, hs, hs,resourceLocation, 1f);
        }

    }

    @Override
    public void onLoadPlayerHeadResponse(ResourceLocation resourceLocation) {
        if(resourceLocation != null) this.resourceLocation = resourceLocation;
    }
}
