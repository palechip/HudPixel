package com.palechip.hudpixelmod.extended.util.gui;

import com.palechip.hudpixelmod.extended.util.RenderUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;

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
public abstract class FancyListObject {

    protected String renderLine1;
    protected String renderLine2;
    protected String renderLineSmall;
    protected String renderPicture;
    protected ResourceLocation resourceLocation;

    public void onRenderTick(boolean small, float xStart, float yStart){
        if(small) {
            renderBoosterSMALL(xStart, yStart);
        } else {
            renderBoosterSHOWN(xStart, yStart);
        }
    }

    public abstract void onClientTick();

    private void renderBoosterSMALL(float xStart, float yStart){
        FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRendererObj;

        //makes the background larger, if the string is to long
        RenderUtils.renderBoxWithColor(xStart, yStart, 130, 12, 0, 0f, 0f, 0f, 0.3f);

        //drwaws the player head after t is loaded

        RenderUtils.drawModalRectWithCustomSizedTexture(
                Math.round(xStart), Math.round(yStart), 0, 0,
                12, 12, 12, 12, resourceLocation, 1f);

        //draws the strings with the minecraft fontRenderer
        fontRenderer.drawStringWithShadow(renderLineSmall ,xStart + 14, yStart + 2, 0xffffff);
    }

    private void renderBoosterSHOWN(float xStart, float yStart){
        FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRendererObj;

        //makes the background larger, if the string is to long
        RenderUtils.renderBoxWithColor(xStart, yStart, 140, 24, 0, 0f, 0f, 0f, 0.3f);

        //draws the player head after t is loaded
        RenderUtils.drawModalRectWithCustomSizedTexture(
                Math.round(xStart), Math.round(yStart), 0, 0,
                24, 24, 24 , 24 , resourceLocation, 1f);

        if(!renderPicture.equals(EnumChatFormatting.WHITE + "")){
            RenderUtils.renderBoxWithColor(xStart, yStart + 12, 24, 9, 0, 0f, 0f, 0f, 0.5f);
        }

        //draws the strings with the minecraft fontRenderer
        fontRenderer.drawStringWithShadow( renderLine1, xStart + 28, yStart + 4, 0xffffff);
        fontRenderer.drawStringWithShadow( renderLine2, xStart + 28, yStart + 13, 0xffffff);
        fontRenderer.drawString(renderPicture, Math.round(xStart), Math.round(yStart + 13),0xffffff);

    }
}
