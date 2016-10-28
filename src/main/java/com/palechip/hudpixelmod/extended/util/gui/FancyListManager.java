/* **********************************************************************************************************************
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
 **********************************************************************************************************************/
package com.palechip.hudpixelmod.extended.util.gui;

import com.palechip.hudpixelmod.extended.HudPixelExtendedEventHandler;
import com.palechip.hudpixelmod.extended.util.IEventHandler;
import com.palechip.hudpixelmod.util.DisplayUtil;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;

import static com.palechip.hudpixelmod.extended.util.gui.FancyListObject.loadingBar;

public abstract class FancyListManager implements IEventHandler {

    /**
     * process the current loadingbar value
     */
    private static int tickCounter = 0;
    protected int shownObjects;
    protected boolean isButtons = false;
    protected boolean isMouseHander = false;
    protected float xStart = 2;
    protected float yStart = 2;
    protected boolean renderRightSide = false;

    protected ArrayList<FancyListObject> fancyListObjects = new ArrayList<FancyListObject>();
    private int indexScroll = 0;

    public FancyListManager(int shownObjects, float xStart, float yStart, boolean renderRightSide) {
        this.shownObjects = shownObjects;
        this.xStart = xStart;
        this.yStart = yStart;
        this.renderRightSide = renderRightSide;
        HudPixelExtendedEventHandler.registerIEvent(this);
    }

    public static void processLoadingBar() {
        if (tickCounter >= 2) {
            if (loadingBar >= 15) loadingBar = 0;
            else loadingBar++;
            tickCounter = 0;
        } else tickCounter++;
    }

    public int size() {
        return fancyListObjects.size();
    }

    @Override
    public void openGUI(GuiScreen guiScreen) {
        indexScroll = 0;
    }

    @Override
    public void onConfigChanged(){
        this.renderRightSide = getConfigRenderRight();
        this.xStart = getConfigxStart();
        this.yStart = getConfigyStart();
    }

    public abstract int getConfigxStart();
    public abstract int getConfigyStart();
    public abstract boolean getConfigRenderRight();

    /**
     * this really renders nothing it just sets rge right offset for each element
     */
    protected void renderDisplay() {

        if (fancyListObjects.isEmpty()) return;

        float xStart = this.xStart;
        float yStart = this.yStart;

        if (renderRightSide) xStart = DisplayUtil.getScaledMcWidth() - xStart - 140;

        if (fancyListObjects.size() <= shownObjects) {
            yStart += 13;
            for (FancyListObject fco : fancyListObjects) {
                fco.onRenderTick(false, xStart, yStart, renderRightSide);
                yStart += 25;
            }
            return;
        }

        if (indexScroll > 0) fancyListObjects.get(indexScroll - 1).onRenderTick(true, xStart, yStart, renderRightSide);
        yStart += 13;

        for (int i = indexScroll; i <= indexScroll + shownObjects - 1; i++) {
            fancyListObjects.get(i).onRenderTick(false, xStart, yStart, renderRightSide);
            yStart += 25;
        }

        if (indexScroll + shownObjects <= size() - 1)
            fancyListObjects.get(indexScroll + shownObjects).onRenderTick(true, xStart, yStart, renderRightSide);
    }


    @Override
    public void onMouseClick(int mX, int mY) {
        if (!isMouseHander) return;
        if (fancyListObjects.isEmpty()) return;
        if (fancyListObjects.size() <= shownObjects)
            for (FancyListObject fco : fancyListObjects)
                fco.onMouseClick(mX, mY);
        else for (int i = indexScroll; i <= indexScroll + shownObjects - 1; i++) {
            if (isButtons)
                fancyListObjects.get(i).onMouseClick(mX, mY);
        }
    }

    /**
     * handels the scrollinput and processes the right index for the list
     *
     * @param i scroll input
     */

    @Override
    public void handleMouseInput(int i, int mX, int mY) {
        if (!isMouseHander) return;
        if (fancyListObjects.isEmpty()) return;
        if (fancyListObjects.size() <= shownObjects)
            for (FancyListObject fco : fancyListObjects)
                fco.onMouseInput(mX, mY);
        else for (int fco = indexScroll; fco <= indexScroll + shownObjects - 1; fco++) {
            if (isButtons)
                fancyListObjects.get(fco).onMouseInput(mX, mY);
        }

        if (mY > (26 * shownObjects + 28)) return;
        if (renderRightSide) {
            float xStart = DisplayUtil.getScaledMcWidth() - this.xStart - 140;
            if (mX < xStart || mX > xStart + 140) return;
        } else if (mX > 140 + xStart) return;

        if (i != 0) {
            if (i < 0) {
                if (indexScroll >= (size() - shownObjects)) {
                    indexScroll = size() - shownObjects;
                } else {
                    indexScroll++;
                }
            } else if (i > 0) {
                if (indexScroll - 1 <= 0) {
                    indexScroll = 0;
                } else {
                    indexScroll--;
                }
            }
        }
    }

}
