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
package com.palechip.hudpixelmod.extended.util.gui;

import com.palechip.hudpixelmod.extended.util.RenderUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.util.ArrayList;

public abstract class FancyListObject {

    protected String renderLine1;
    protected String renderLine2;
    protected String renderLineSmall;
    protected String renderPicture;
    protected ResourceLocation resourceLocation;
    public static int loadingBar;
    private float xStart;
    private float yStart;
    private boolean renderRight;
    private boolean isHover;


    protected ArrayList<FancyListButton> fancyListObjectButtons = new ArrayList<FancyListButton>();

    protected void addButton(FancyListButton fcob){
        fancyListObjectButtons.add(fcob);
    }



    /**
     * renders the loading animation
     * @param xStart startposition of the friendsdisplay
     * @param yStart startposition of the friendsdisplay
     */
    private void renderLoadingBar(float xStart, float yStart){

        final int a = 2;
        final int b = 1;
        final float alpha = 0.8f;

        switch (loadingBar){
            case 0:
                RenderUtils.renderBoxWithColor(xStart + 7,  yStart + 9, 2, 6 + a, 0, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(xStart + 11, yStart + 9, 2, 6    , 0, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(xStart + 15, yStart + 9, 2, 6    , 0, 1f, 1f, 1f, alpha);
                break;
            case 1:
                RenderUtils.renderBoxWithColor(xStart + 7,  yStart + 9, 2, 6 + b, 0, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(xStart + 11, yStart + 9, 2, 6 + a, 0, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(xStart + 15, yStart + 9, 2, 6    , 0, 1f, 1f, 1f, alpha);
                break;
            case 2:
                RenderUtils.renderBoxWithColor(xStart + 7,  yStart + 9, 2, 6    , 0, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(xStart + 11, yStart + 9, 2, 6 + b, 0, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(xStart + 15, yStart + 9, 2, 6 + a, 0, 1f, 1f, 1f, alpha);
                break;
            case 3:
                RenderUtils.renderBoxWithColor(xStart + 7,  yStart + 9, 2, 6    , 0, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(xStart + 11, yStart + 9, 2, 6    , 0, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(xStart + 15, yStart + 9, 2, 6 + b, 0, 1f, 1f, 1f, alpha);
                break;
            default:
                RenderUtils.renderBoxWithColor(xStart + 7,  yStart + 9, 2, 6    , 0, 1f, 1f, 1f, 0.8f);
                RenderUtils.renderBoxWithColor(xStart + 11, yStart + 9, 2, 6    , 0, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(xStart + 15, yStart + 9, 2, 6    , 0, 1f, 1f, 1f, alpha);
                break;
        }
    }

    /**
     * You have to call it each render tick from the FancyListManager
     * @param small set it to true if you want the display small
     * @param xStart xStart
     * @param yStart yStart
     */
    void onRenderTick(boolean small, float xStart, float yStart, boolean renderRightSide){
        this.xStart = xStart;
        this.yStart = yStart;
        this.renderRight = renderRightSide;
        if(small) renderBoosterSMALL();
        else      renderBoosterSHOWN();
    }

    void onMouseInput(int mX, int mY){

        if(mX > xStart && mX < (xStart + 140) && mY > yStart && mY < yStart + 24){
            isHover = true;
            for(FancyListButton fcob : fancyListObjectButtons)
                fcob.isHover = false;
        } else if(isHover && mX > xStart + 140 && mX < (xStart + 140 + fancyListObjectButtons.size()*24) && mY > yStart && mY < yStart+24) {
            isHover = true;
            for(FancyListButton fcob : fancyListObjectButtons)
                fcob.onMouseInput(mX, mY);
        } else isHover = false;
    }

    public void onClientTick(){
        onTick();
    }

    public abstract void onTick();

    /**
     * This method draws the display in the smaller version and just with the first line of
     * the string!
     */
    private void renderBoosterSMALL(){
        FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRendererObj;

        float xStart = this.xStart;
        if(renderRight){
            xStart = xStart + 10;
        }

        RenderUtils.renderBoxWithColor(xStart, yStart, 130, 12, 0, 0f, 0f, 0f, 0.3f);//draws the background

        if(resourceLocation == null) renderLoadingBar(xStart, yStart);
        else
        RenderUtils.drawModalRectWithCustomSizedTexture( //draws the texture
                Math.round(xStart), Math.round(yStart), 0, 0,
                12, 12, 12, 12, resourceLocation, 1f);

        fontRenderer.drawStringWithShadow(renderLineSmall ,xStart + 14, yStart + 2, 0xffffff); //draws the first line string
    }

    /**
     * This method draws the display with all it's components
     */
    private void renderBoosterSHOWN(){
        FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRendererObj;

        if(!isHover) RenderUtils.renderBoxWithColor(xStart, yStart, 140, 24, 0, 0f, 0f, 0f, 0.3f); //draws the background
        else{
            RenderUtils.renderBoxWithColor(xStart, yStart, 140, 24, 0, 1f, 1f, 1f, 0.2f);
            float xStartB = xStart + 140;
            float yStartB = yStart;
            for(FancyListButton fcob : fancyListObjectButtons){
                fcob.onRender(xStartB, yStartB);
                xStartB += 24;
            }

        }

        if(resourceLocation == null) renderLoadingBar(xStart, yStart);
        else
        RenderUtils.drawModalRectWithCustomSizedTexture( //draws the image shown
                Math.round(xStart), Math.round(yStart), 0, 0,
                24, 24, 24 , 24 , resourceLocation, 1f);

        if(!renderPicture.equals(EnumChatFormatting.WHITE + "")){ //draws a background over the image if there is a string to render
            RenderUtils.renderBoxWithColor(xStart, yStart + 12, 24, 9, 0, 0f, 0f, 0f, 0.5f);
        }

        fontRenderer.drawStringWithShadow( renderLine1, xStart + 28, yStart + 4, 0xffffff); //draws the first line
        fontRenderer.drawStringWithShadow( renderLine2, xStart + 28, yStart + 13, 0xffffff); //draws the second line
        fontRenderer.drawString(renderPicture, Math.round(xStart), Math.round(yStart + 13),0xffffff); //draws the string over the image

    }

    public void onMouseClick(int mX, int mY) {
        for(FancyListButton fcob : fancyListObjectButtons)
            fcob.onMouseClick(mX, mY);
    }
}
