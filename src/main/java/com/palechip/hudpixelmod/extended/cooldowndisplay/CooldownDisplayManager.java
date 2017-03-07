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

package com.palechip.hudpixelmod.extended.cooldowndisplay;

import com.palechip.hudpixelmod.GameDetector;
import com.palechip.hudpixelmod.config.CCategory;
import com.palechip.hudpixelmod.config.ConfigPropertyBoolean;
import com.palechip.hudpixelmod.config.ConfigPropertyInt;
import com.palechip.hudpixelmod.extended.HudPixelExtendedEventHandler;
import com.palechip.hudpixelmod.extended.util.IEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.util.ArrayList;

import static com.palechip.hudpixelmod.extended.cooldowndisplay.CooldownManagerFactory.setCooldownDisplay;

public class CooldownDisplayManager implements IEventHandler {

    @ConfigPropertyBoolean(category = CCategory.COOLDOWN_DISPLAY, id = "cooldownDisplay", comment = "The Cooldown Tracker", def = true)
    public static boolean enabled = true;
    @ConfigPropertyInt(category = CCategory.COOLDOWN_DISPLAY, id = "yOffsetCooldownDisplay", comment = "Y offset of cooldown display", def = 25)
    public static int yOffsetCooldownDisplay = 25;
    @ConfigPropertyInt(category = CCategory.COOLDOWN_DISPLAY, id = "xOffsetCooldownDisplay", comment = "X offset of cooldown display", def = 0)
    public static int xOffsetCooldownDisplay = 0;

    static ArrayList<CooldownDisplayModule> cdModules = new ArrayList<CooldownDisplayModule>();
    private static CooldownDisplayManager instance;
    int count = 0;

    private CooldownDisplayManager() {
        HudPixelExtendedEventHandler.registerIEvent(this);
    }

    public static CooldownDisplayManager getInstance() {
        if (instance == null) instance = new CooldownDisplayManager();
        return instance;
    }

    @Override
    public void onClientTick() {
        count++;
        if (count > 40) {
            count = 0;
            cdModules = setCooldownDisplay(GameDetector.getCurrentGameType());
        }
        if (cdModules.isEmpty()) return;
        for (CooldownDisplayModule cdM : cdModules)
            cdM.onClientTick();
    }

    @Override
    public void onRender() {

        if (cdModules.isEmpty() || !enabled || GameDetector.getIsLobby()) return;

        Minecraft mc = Minecraft.getMinecraft();
        int scale;
        if (mc.gameSettings.guiScale == 0) {
            ScaledResolution res = new ScaledResolution(mc);
            scale = res.getScaleFactor();
        } else {
            scale = mc.gameSettings.guiScale;
        }

        int xCenter = (Minecraft.getMinecraft().displayWidth / 2 / scale) - ((cdModules.size() / 2) * 25) + 4;
        int yCenter = Minecraft.getMinecraft().displayHeight / 2 / scale;
        for (int i = 0; i < cdModules.size(); i++)
            cdModules.get(i).renderModule(Math.round(xCenter + xOffsetCooldownDisplay + (i * 25)), yCenter + yOffsetCooldownDisplay);
    }

    @Override
    public void handleMouseInput(int i, int mX, int mY) {
    }

    @Override
    public void onMouseClick(int mX, int mY) {
    }

    @Override
    public void onChatReceived(ClientChatReceivedEvent e) throws Throwable {
    }
}
