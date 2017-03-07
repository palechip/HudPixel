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

package com.palechip.hudpixelmod.extended;

import com.palechip.hudpixelmod.GameDetector;
import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.config.EasyConfigHandler;
import com.palechip.hudpixelmod.extended.fancychat.FancyChat;
import com.palechip.hudpixelmod.extended.onlinefriends.OnlineFriendManager;
import com.palechip.hudpixelmod.extended.statsviewer.StatsViewerManager;
import com.palechip.hudpixelmod.extended.util.IEventHandler;
import com.palechip.hudpixelmod.extended.util.gui.FancyListManager;
import com.palechip.hudpixelmod.modulargui.ModularGuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

import static com.palechip.hudpixelmod.util.DisplayUtil.getMcScale;


public class HudPixelExtendedEventHandler {

    private static final long clickDelay = 1000;

    private static long lastTimeClicked;
    private static boolean doubleClick = false;
    private long lastSystemTime = System.currentTimeMillis();
    private int delay = 20 * 1000; //20s

    private static final ArrayList<IEventHandler> iEventArrayList = new ArrayList<>();

    public static void registerIEvent(IEventHandler iEventHandler) {
        iEventArrayList.add(iEventHandler);
    }

    public static void unregisterIEvent(IEventHandler iEventHandler) {
        iEventArrayList.remove(iEventHandler);
    }

    public static ArrayList<IEventHandler> getIeventBuffer() {
        return new ArrayList<>(iEventArrayList);
    }


    private static void mouseClickEvent() {
        Minecraft mc = Minecraft.getMinecraft();
        if (!(mc.currentScreen instanceof GuiIngameMenu || mc.currentScreen instanceof GuiChat)) return;

        if (System.currentTimeMillis() > (lastTimeClicked + clickDelay) && Mouse.isButtonDown(0)) {
            doubleClick = false;
            lastTimeClicked = System.currentTimeMillis();

        } else if (System.currentTimeMillis() < (lastTimeClicked + clickDelay)) {
            if (!Mouse.isButtonDown(0) && !doubleClick) {
                doubleClick = true;
                return;
            }

            if (Mouse.isButtonDown(0) && doubleClick) {
                doubleClick = false;
                int scale = getMcScale();

                int mX = Mouse.getX() / scale;
                int mY = (mc.displayHeight - Mouse.getY()) / scale;
                for (IEventHandler iE : getIeventBuffer()) {
                    iE.onMouseClick(mX, mY);
                }
            }
        }
    }

    public static void handleMouseScroll(int i) {
        mouseClickEvent();

        Minecraft mc = Minecraft.getMinecraft();
        if (!(mc.currentScreen instanceof GuiIngameMenu || mc.currentScreen instanceof GuiChat)) return;

        int scale = getMcScale();

        int mX = Mouse.getX() / scale;
        int mY = (mc.displayHeight - Mouse.getY()) / scale;
        //int i = Mouse.getDWheel();
        for (IEventHandler iE : getIeventBuffer()) {
            iE.handleMouseInput(i, mX, mY);
        }
        FancyChat.getInstance().handleMouseInput(i);
    }

    public static void onGameStart() {

    }

    public static void onGameEnd() {
        ModularGuiHelper.onGameEnd();
    }

    /**
     * prints the message to the clientchat
     *
     * @param message the message
     **/
    private static void printMessage(String message) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(
                new ChatComponentText(message));
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        try {
            // This event isn't bound to the Hypixel Network
            if (eventArgs.modID.equals(HudPixelMod.MODID)) {
                EasyConfigHandler.INSTANCE.synchronize();
                getIeventBuffer().forEach(IEventHandler::onConfigChanged);
            }
        } catch (Exception e) {
            HudPixelMod.Companion.instance().logWarn("[Extended] An exception occurred in onConfigChanged(). Stacktrace below.");
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onPlayerName(PlayerEvent.NameFormat e) {
        //StaffManager.onPlayerName(e);
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent e) {
        try {
            if (!HudPixelMod.Companion.isHypixelNetwork()) return;

            for (IEventHandler i : getIeventBuffer())
                i.onRenderWorld(e);

        } catch (Exception ex) {
            HudPixelMod.Companion.instance().logWarn("[Extended] An exception occurred in onRenderWorldLast(). Stacktrace below.");
            ex.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Pre e) {
        try {
            //Don't do anything unless we are on Hypixel
            if (HudPixelMod.Companion.isHypixelNetwork()) {
                for (IEventHandler i : getIeventBuffer())
                    i.onRenderPlayer(e);

                //just triggeres the statsrenderer if the player is waiting for the game to start
                if (GameDetector.getIsLobby() && StatsViewerManager.enabled)
                    StatsViewerManager.onRenderPlayer(e);
            }
        } catch (Exception ex) {
            HudPixelMod.Companion.instance().logWarn("[Extended] An exception occurred in onRenderPlayer(). Stacktrace below.");
            ex.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onOpenGui(GuiOpenEvent e) {
        try {
            //Don't do anything unless we are on Hypixel
            if (HudPixelMod.Companion.isHypixelNetwork()) {
                for (IEventHandler i : getIeventBuffer())
                    i.openGUI(Minecraft.getMinecraft().currentScreen);
                if (Minecraft.getMinecraft().thePlayer != null)
                    OnlineFriendManager.getInstance();
                FancyChat.getInstance().openGui();

            }
        } catch (Exception ex) {
            HudPixelMod.Companion.instance().logWarn("[Extended] An exception occurred in onOpenGui(). Stacktrace below.");
            ex.printStackTrace();
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onChatMessage(ClientChatReceivedEvent e) {
        try {
            //Don't do anything unless we are on Hypixel
            if (HudPixelMod.Companion.isHypixelNetwork()) {
                final String message = e.message.getUnformattedText();

                for (IEventHandler i : getIeventBuffer())
                    i.onChatReceivedMessage(e, message);

                for (IEventHandler i : getIeventBuffer())
                    i.onChatReceived(e);
                FancyChat.getInstance().onChat(e);
            }
        } catch (Exception ex) {
            HudPixelMod.Companion.instance().logWarn("[Extended]An exception occurred in onChatMessage(). Stacktrace below.");
            ex.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private static short tick;
    private static short sec;
    private static short min;

    private static void processTickTimes() {
        tick++;
        if (tick >= 20) {
            tick = 0;
            sec++;
            getIeventBuffer().forEach(IEventHandler::everySEC);
            if (sec >= 60) {
                sec = 0;
                min++;
                getIeventBuffer().forEach(IEventHandler::everyMIN);
                if (min >= 60)
                    min = 0;
            } else if (sec == 5) {
                getIeventBuffer().forEach(IEventHandler::everyFiveSEC);
            }
        } else if (tick == 10) {
            getIeventBuffer().forEach(IEventHandler::everyTenTICKS);
        }

    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent e) {
        try {
            //Don't do anything unless we are on Hypixel
            if (HudPixelMod.Companion.isHypixelNetwork()) {

                processTickTimes();

                getIeventBuffer().forEach(IEventHandler::onClientTick);

                FancyListManager.processLoadingBar();
                //handleMouseScroll();
                //Tick for FancyChat
                FancyChat.getInstance().onClientTick();
                //Tick for the statsViewerManager
                if (GameDetector.getIsLobby()) {
                    //System.out.print(GameDetector.getCurrentGameType().getNm());
                    StatsViewerManager.onClientTick();
                }

                if (lastSystemTime + delay < System.currentTimeMillis()) {
                    lastSystemTime = System.currentTimeMillis();
                }

            } else if (HudPixelMod.Companion.getIS_DEBUGGING()) {
                FancyChat.getInstance().onClientTick();
            }
        } catch (Exception ex) {
            HudPixelMod.Companion.instance().logWarn("[Extended]An exception occurred in onClientTick(). Stacktrace below.");
            ex.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onRenderTick(RenderGameOverlayEvent.Post e) {
        try {
            //Don't do anything unless we are on Hypixel
            if (HudPixelMod.Companion.isHypixelNetwork() && e.type == RenderGameOverlayEvent.ElementType.ALL && !e.isCancelable()) {
                getIeventBuffer().forEach(IEventHandler::onRender);
                if (FancyChat.enabled) FancyChat.getInstance().onRenderTick();
            }
        } catch (Exception ex) {
            HudPixelMod.Companion.instance().logWarn("[Extended]An exception occurred in omRenderTick). Stacktrace below.");
            ex.printStackTrace();
        }
    }
}
