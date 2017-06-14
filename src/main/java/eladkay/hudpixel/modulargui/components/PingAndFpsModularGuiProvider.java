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

package eladkay.hudpixel.modulargui.components;


import eladkay.hudpixel.HudPixelMod;
import eladkay.hudpixel.config.CCategory;
import eladkay.hudpixel.config.ConfigPropertyBoolean;
import eladkay.hudpixel.modulargui.HudPixelModularGuiProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.ServerPinger;

import java.net.UnknownHostException;

public class PingAndFpsModularGuiProvider extends HudPixelModularGuiProvider {
    private static final int pingCooldwonMs = 2000;
    @ConfigPropertyBoolean(category = CCategory.HUD, id = "pingFps", comment = "The Ping/FPS Display", def = true)
    public static boolean enabled = false;
    private static long nextTimeStamp;
    private static long lastValidPing;
    private static ServerPinger serverPinger = new ServerPinger();
    private static String pingString;
    PingOrFps pingOrFps;

    public PingAndFpsModularGuiProvider(PingOrFps pingOrFps) {
        this.pingOrFps = pingOrFps;
    }

    /**
     * A methode that returns the last valid ping and updates it if necessary
     *
     * @return "Ping:" + your last ping in ms
     */
    public static String getStaticRenderingString() {

        // updates the ping if the last ping validation has expired
        if (System.currentTimeMillis() >= nextTimeStamp) {
            updatePing();
        }

        // updates the current renderString
        if (Minecraft.getMinecraft().getCurrentServerData() == null || Minecraft.getMinecraft() == null) {
            lastValidPing = 0;
            return pingString = "Irrelevant";
        }

        if (Minecraft.getMinecraft().getCurrentServerData().pingToServer != lastValidPing
                && Minecraft.getMinecraft().getCurrentServerData().pingToServer > 0) {
            lastValidPing = Minecraft.getMinecraft().getCurrentServerData().pingToServer;
            pingString =
                    // EnumChatFormatting.WHITE
                           /* + */""
                    + lastValidPing
                    + "ms";
        }
        return pingString;

    }

    /**
     * the function who updates your ping. Every ping request is done in a external thread,
     * to not block the mainthread while waiting for the response.
     */
    private static void updatePing() {
        nextTimeStamp = System.currentTimeMillis() + pingCooldwonMs;

        //starting external Thread to not block the mainthread
        new Thread("pingThread") {
            @Override
            public void run() {
                try {
                    if (HudPixelMod.Companion.isHypixelNetwork())
                        if (Minecraft.getMinecraft().getCurrentServerData() != null)
                            serverPinger.ping(Minecraft.getMinecraft().getCurrentServerData());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public boolean doesMatchForGame() {
        return true;
    }

    @Override
    public void setupNewGame() {

    }

    @Override
    public void onGameStart() {

    }

    @Override
    public void onGameEnd() {

    }

    @Override
    public void onTickUpdate() {

    }

    @Override
    public void onChatMessage(String textMessage, String formattedMessage) {

    }

    @Override
    public boolean showElement() {
        return Minecraft.getMinecraft().getCurrentServerData() != null && enabled;
    }

    @Override
    public String content() {
        return pingOrFps == PingOrFps.PING ? Minecraft.getDebugFPS() + "" : getStaticRenderingString();
    }

    @Override
    public boolean ignoreEmptyCheck() {
        return false;
    }

    @Override
    public String getAfterstats() {
        return null;
    }

    public enum PingOrFps {PING, FPS}
}
