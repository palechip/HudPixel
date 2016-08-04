package com.palechip.hudpixelmod.extended.util.gui;

import com.palechip.hudpixelmod.extended.util.RenderUtils;
import net.minecraft.util.ResourceLocation;

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
public abstract class FancyListButton {

    private float xStart = -1;
    private float yStart = -1;
    public boolean isHover;

    private ResourceLocation resourceLocation;

    private float r;
    private float g;
    private float b;

    protected FancyListButton(float r, float g, float b, ResourceLocation resourceLocation){
        this.r = r; this.g = g; this.b = b;
        this.resourceLocation = resourceLocation;
    }

    protected abstract void onClick();

    void onMouseClick(int mX, int mY){
        if(isHover && mX > xStart && mX < xStart + 140 && mY > yStart && mY < yStart+24)
            onClick();
    }

    void onMouseInput(int mX, int mY) {
        if(xStart < 0 || yStart < 0) return;
        if(mX > xStart && mX < xStart + 24 && mY > yStart && mY < yStart+24)
            isHover = true;
        else isHover = false;
    }

    void onRender(float xStart, float yStart){
        this.xStart = xStart;
        this.yStart = yStart;
        if(!isHover) RenderUtils.renderBoxWithColor(xStart, yStart, 24, 24, 0, r, g, b, 0.5f); //draws the background
        else         RenderUtils.renderBoxWithColor(xStart, yStart, 24, 24, 0, r, g, b, 0.8f);
        RenderUtils.drawModalRectWithCustomSizedTexture( //draws the image shown
                Math.round(xStart), Math.round(yStart), 0, 0,
               24, 24, 24 , 24 , resourceLocation, 1f);
    }
}

