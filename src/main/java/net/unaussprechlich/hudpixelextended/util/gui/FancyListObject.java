/*
 * HudPixelReloaded - License
 * <p>
 * The repository contains parts of Minecraft Forge and its dependencies. These parts have their licenses
 * under forge-docs/. These parts can be downloaded at files.minecraftforge.net.This project contains a
 * unofficial copy of pictures from the official Hypixel website. All copyright is held by the creator!
 * Parts of the code are based upon the Hypixel Public API. These parts are all in src/main/java/net/hypixel/api and
 * subdirectories and have a special copyright header. Unfortunately they are missing a license but they are obviously
 * intended for usage in this kind of application. By default, all rights are reserved.
 * The original version of the HudPixel Mod is made by palechip and published under the MIT license.
 * The majority of code left from palechip's creations is the component implementation.The ported version to
 * Minecraft 1.8.9 and up HudPixel Reloaded is made by PixelModders/Eladkay and also published under the MIT license
 * (to be changed to the new license as detailed below in the next minor update).
 * <p>
 * For the rest of the code and for the build the following license applies:
 * <p>
 * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
 * #  HudPixel by PixelModders, Eladkay & unaussprechlich is licensed under a Creative Commons         #
 * #  Attribution-NonCommercial-ShareAlike 4.0 International License with the following restrictions.  #
 * #  Based on a work at HudPixelExtended & HudPixel.                                                  #
 * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
 * <p>
 * Restrictions:
 * <p>
 * The authors are allowed to change the license at their desire. This license is void for members of PixelModders and
 * to unaussprechlich, except for clause 3. The licensor cannot revoke these freedoms in most cases, as long as you follow
 * the following license terms and the license terms given by the listed above Creative Commons License, however in extreme
 * cases the authors reserve the right to revoke all rights for usage of the codebase.
 * <p>
 * 1. PixelModders, Eladkay & unaussprechlich are the authors of this licensed material. GitHub contributors are NOT
 * considered authors, neither are members of the HudHelper program. GitHub contributers still hold the rights for their
 * code, but only when it is used separately from HudPixel and any license header must indicate that.
 * 2. You shall not claim ownership over this project and repost it in any case, without written permission from at least
 * two of the authors.
 * 3. You shall not make money with the provided material. This project is 100% non commercial and will always stay that
 * way. This clause is the only one remaining, should the rest of the license be revoked. The only exception to this
 * clause is completely cosmetic features. Only the authors may sell cosmetic features for the mod.
 * 4. Every single contibutor owns copyright over his contributed code when separated from HudPixel. When it's part of
 * HudPixel, it is only governed by this license, and any copyright header must indicate that. After the contributed
 * code is merged to the release branch you cannot revoke the given freedoms by this license.
 * 5. If your own project contains a part of the licensed material you have to give the authors full access to all project
 * related files.
 * 6. You shall not act against the will of the authors regarding anything related to the mod or its codebase. The authors
 * reserve the right to take down any infringing project.
 */

package net.unaussprechlich.hudpixelextended.util.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.unaussprechlich.managedgui.lib.util.RenderUtils;

import java.util.ArrayList;

public abstract class FancyListObject {

    public static int loadingBar;
    protected String renderLine1;
    protected String renderLine2;
    protected String renderLineSmall;
    protected String renderPicture;
    protected ResourceLocation resourceLocation;
    protected ArrayList<FancyListButton> fancyListObjectButtons = new ArrayList<FancyListButton>();
    private int xStart;
    private int yStart;
    private boolean renderRight;
    private boolean isHover;

    protected void addButton(FancyListButton fcob) {
        fancyListObjectButtons.add(fcob);
    }


    /**
     * renders the loading animation
     *
     * @param xStart startposition of the friendsdisplay
     * @param yStart startposition of the friendsdisplay
     */
    private void renderLoadingBar(float xStart, float yStart) {

        final int a = 2;
        final int b = 1;
        final float alpha = 0.8f;

        switch (loadingBar) {
            case 0:
                RenderUtils.renderBoxWithColor((short) xStart + 7, (short) yStart + 9, 2, (short) 6 + a, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(xStart + 11, yStart + 9, 2, 6, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(xStart + 15, yStart + 9, 2, 6, 1f, 1f, 1f, alpha);
                break;
            case 1:
                RenderUtils.renderBoxWithColor(xStart + 7, yStart + 9, 2, 6 + b, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(xStart + 11, yStart + 9, 2, 6 + a, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(xStart + 15, yStart + 9, 2, 6, 1f, 1f, 1f, alpha);
                break;
            case 2:
                RenderUtils.renderBoxWithColor(xStart + 7, yStart + 9, 2, 6, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(xStart + 11, yStart + 9, 2, 6 + b, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(xStart + 15, yStart + 9, 2, 6 + a, 1f, 1f, 1f, alpha);
                break;
            case 3:
                RenderUtils.renderBoxWithColor(xStart + 7, yStart + 9, 2, 6, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(xStart + 11, yStart + 9, 2, 6, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(xStart + 15, yStart + 9, 2, 6 + b, 1f, 1f, 1f, alpha);
                break;
            default:
                RenderUtils.renderBoxWithColor(xStart + 7, yStart + 9, 2, 6, 1f, 1f, 1f, 0.8f);
                RenderUtils.renderBoxWithColor(xStart + 11, yStart + 9, 2, 6, 1f, 1f, 1f, alpha);
                RenderUtils.renderBoxWithColor(xStart + 15, yStart + 9, 2, 6, 1f, 1f, 1f, alpha);
                break;
        }
    }

    /**
     * You have to call it each render tick from the FancyListManager
     *
     * @param small  set it to true if you want the display small
     * @param xStart xStart
     * @param yStart yStart
     */
    void onRenderTick(boolean small, int xStart, int yStart, boolean renderRightSide) {
        this.xStart = xStart;
        this.yStart = yStart;
        this.renderRight = renderRightSide;
        if (small) renderBoosterSMALL();
        else renderBoosterSHOWN();
    }

    void onMouseInput(int mX, int mY) {
        float buttonOffsetX1;
        float buttonOffsetX2;
        if (!renderRight) {
            buttonOffsetX1 = xStart;
            buttonOffsetX2 = xStart + 140 + fancyListObjectButtons.size() * 24;
        } else {
            buttonOffsetX1 = xStart - fancyListObjectButtons.size() * 24;
            buttonOffsetX2 = xStart + 140;
        }

        if (mX > xStart && mX < (xStart + 140) && mY > yStart && mY < yStart + 24) {
            isHover = true;
            for (FancyListButton fcob : fancyListObjectButtons)
                fcob.isHover = false;
        } else if (isHover && mX > buttonOffsetX1 && mX < buttonOffsetX2 && mY > yStart && mY < yStart + 24) {
            isHover = true;
            for (FancyListButton fcob : fancyListObjectButtons)
                fcob.onMouseInput(mX, mY);
        } else isHover = false;
    }

    public void onClientTick() {
        onTick();
    }

    public abstract void onTick();

    /**
     * This method draws the display in the smaller version and just with the first line of
     * the string!
     */
    private void renderBoosterSMALL() {
        FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRendererObj;

        float xStart = this.xStart;
        //System.out.print(renderRight);
        if (renderRight) {
            xStart = xStart + 10;
        }

        RenderUtils.renderBoxWithColor(xStart, yStart, 130, 12, 0f, 0f, 0f, 0.3f);//draws the background

        if (resourceLocation == null) renderLoadingBar(xStart, yStart);
        else
            RenderUtils.texture_modularRect( //draws the texture
                    Math.round(xStart), Math.round(yStart), 0, 0,
                    12, 12, 12, 12, resourceLocation, 1f);

        fontRenderer.drawStringWithShadow(renderLineSmall, xStart + 14, yStart + 2, 0xffffff); //draws the first line string
    }

    /**
     * This method draws the display with all it's components
     */
    private void renderBoosterSHOWN() {
        FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRendererObj;

        if (!isHover)
            RenderUtils.renderBoxWithColor(xStart, yStart, 140, 24, 0f, 0f, 0f, 0.3f); //draws the background
        else {
            RenderUtils.renderBoxWithColor(xStart, yStart, 140, 24, 1f, 1f, 1f, 0.2f);
            int xStartB;
            if (!renderRight) xStartB = xStart + 140;
            else xStartB = xStart - 24;


            int yStartB = yStart;
            for (FancyListButton fcob : fancyListObjectButtons) {
                fcob.onRender(xStartB, yStartB);
                if (!renderRight) xStartB += 24;
                else xStartB -= 24;
            }

        }

        if (resourceLocation == null) renderLoadingBar(xStart, yStart);
        else
            RenderUtils.texture_modularRect( //draws the image shown
                    Math.round(xStart), Math.round(yStart), 0, 0,
                    24, 24, 24, 24, resourceLocation, 1f);

        if (!renderPicture.equals(TextFormatting.WHITE + "")) { //draws a background over the image if there is a string to render
            RenderUtils.renderBoxWithColor(xStart, yStart + 12, 24, 9, 0f, 0f, 0f, 0.5f);
        }

        fontRenderer.drawStringWithShadow(renderLine1, xStart + 28, yStart + 4, 0xffffff); //draws the first line
        fontRenderer.drawStringWithShadow(renderLine2, xStart + 28, yStart + 13, 0xffffff); //draws the second line
        fontRenderer.drawString(renderPicture, Math.round(xStart), Math.round(yStart + 13), 0xffffff); //draws the string over the image

    }

    public void onMouseClick(int mX, int mY) {
        for (FancyListButton fcob : fancyListObjectButtons)
            fcob.onMouseClick(mX, mY);
    }
}
