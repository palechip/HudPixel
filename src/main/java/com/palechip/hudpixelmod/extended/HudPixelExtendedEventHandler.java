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

package com.palechip.hudpixelmod.extended;

import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.detectors.HypixelNetworkDetector;
import com.palechip.hudpixelmod.extended.configuration.Config;
import com.palechip.hudpixelmod.extended.fancychat.FancyChat;
import com.palechip.hudpixelmod.extended.onlinefriends.OnlineFriendManager;
import com.palechip.hudpixelmod.extended.staff.StaffManager;
import com.palechip.hudpixelmod.extended.statsviewer.StatsViewerManager;
import com.palechip.hudpixelmod.extended.util.IEventHandler;
import com.palechip.hudpixelmod.extended.util.gui.FancyListManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

import static com.palechip.hudpixelmod.util.DisplayUtil.getMcScale;

public class HudPixelExtendedEventHandler{

    private static ArrayList<IEventHandler> ieventArrayList = new ArrayList<IEventHandler>();
    public static void registerIEvent(IEventHandler iEventHandler){
        ieventArrayList.add(iEventHandler);
    }
    public static void unregisterIEvent(IEventHandler iEventHandler){
        ieventArrayList.remove(iEventHandler);
    }

    private static ArrayList<IEventHandler> getIeventBuffer(){
        return new ArrayList<IEventHandler>(ieventArrayList);
    }

    @SubscribeEvent
    public void onPlayerName(PlayerEvent.NameFormat e){
        StaffManager.onPlayerName(e);
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent e){
        try{
            if(!HypixelNetworkDetector.isHypixelNetwork)return;
        }catch (Exception ex){
            HudPixelMod.instance().logWarn("[Extended] An exception occurred in onRenderWorldLast(). Stacktrace below.");
            ex.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Pre e){
        try {
            //Don't do anything unless we are on Hypixel
            if(HypixelNetworkDetector.isHypixelNetwork ) {
                //just triggeres the statsrenderer if the player is waiting for the game to start
                if(!(HudPixelMod.instance().gameDetector.isLobby())
                && !(HudPixelMod.instance().gameDetector.getCurrentGame().hasGameStarted())
                && Config.isStats)
                    StatsViewerManager.onRenderPlayer(e);
            }
        } catch (Exception ex) {
            HudPixelMod.instance().logWarn("[Extended] An exception occurred in onRenderPlayer(). Stacktrace below.");
            ex.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onOpenGui(GuiOpenEvent e) {
        try {
            //Don't do anything unless we are on Hypixel
            if(HypixelNetworkDetector.isHypixelNetwork) {
                if(Minecraft.getMinecraft().thePlayer != null)
                OnlineFriendManager.getInstance();
                FancyChat.getInstance().openGui();
            }
        } catch (Exception ex) {
            HudPixelMod.instance().logWarn("[Extended] An exception occurred in onOpenGui(). Stacktrace below.");
            ex.printStackTrace();
        }
    }

    @SubscribeEvent(receiveCanceled=true)
    public void onChatMessage(ClientChatReceivedEvent e) {
        try {
            //Don't do anything unless we are on Hypixel
            if (HypixelNetworkDetector.isHypixelNetwork) {
                for(IEventHandler i : getIeventBuffer())
                    i.onChatReceived(e);
                FancyChat.getInstance().onChat(e);
            }
        } catch (Exception ex) {
            HudPixelMod.instance().logWarn("[Extended]An exception occurred in onChatMessage(). Stacktrace below.");
            ex.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    long lastSystemTime = System.currentTimeMillis();
    int delay = 20 * 1000; //20s

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent e) {
        try {
            //Don't do anything unless we are on Hypixel
            if (HypixelNetworkDetector.isHypixelNetwork) {
                for(IEventHandler i : getIeventBuffer())
                    i.onClientTick();
                FancyListManager.processLoadingBar();
                handleMouseScroll();
                //Tick for FancyChat
                FancyChat.getInstance().onClientTick();
                //Tick for the statsViewerManager
                if(!(HudPixelMod.instance().gameDetector.isLobby()) && !(HudPixelMod.instance().gameDetector.getCurrentGame().hasGameStarted()))
                    StatsViewerManager.onClientTick();

                if(lastSystemTime+delay < System.currentTimeMillis()){
                    lastSystemTime = System.currentTimeMillis();
                }


            } else if(Config.isDebuging){
                FancyChat.getInstance().onClientTick();
            }
        } catch (Exception ex){
            HudPixelMod.instance().logWarn("[Extended]An exception occurred in onClientTick(). Stacktrace below.");
            ex.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onRenderTick(RenderGameOverlayEvent.Post e) {
        try {
            //Don't do anything unless we are on Hypixel
            if (HypixelNetworkDetector.isHypixelNetwork && e.type == RenderGameOverlayEvent.ElementType.ALL && !e.isCancelable()) {
                for(IEventHandler i : getIeventBuffer())
                    i.onRender();
                if(Config.isFancyChat) FancyChat.getInstance().onRenderTick();
            }
        } catch (Exception ex) {
            HudPixelMod.instance().logWarn("[Extended]An exception occurred in omRenderTick). Stacktrace below.");
            ex.printStackTrace();
        }
    }

    private static final long clickDelay = 1000;
    private static long lastTimeClicked;
    private static boolean doubleClick = false;
    public static void mouseClickEvent(){
        Minecraft mc = Minecraft.getMinecraft();
        if(!(mc.currentScreen instanceof GuiIngameMenu || mc.currentScreen instanceof GuiChat)) return;

        if(System.currentTimeMillis() > (lastTimeClicked + clickDelay) && Mouse.isButtonDown(0)){
            doubleClick = false;
            lastTimeClicked = System.currentTimeMillis();

        } else if(System.currentTimeMillis() < (lastTimeClicked + clickDelay)){
            if(!Mouse.isButtonDown(0) && !doubleClick){
                doubleClick = true;
                return;
            }

            if(Mouse.isButtonDown(0) && doubleClick){
                doubleClick = false;
                int scale = getMcScale();

                int mX = Mouse.getX() / scale;
                int mY = (mc.displayHeight - Mouse.getY()) / scale;
                for(IEventHandler iE : getIeventBuffer()){
                    iE.onMouseClick(mX, mY);
                }
            }
        }
    }

    private static void handleMouseScroll(){
        mouseClickEvent();

        Minecraft mc = Minecraft.getMinecraft();
        if(!(mc.currentScreen instanceof GuiIngameMenu || mc.currentScreen instanceof GuiChat)) return;

        int scale = getMcScale();

        int mX = Mouse.getX() / scale;
        int mY = (mc.displayHeight - Mouse.getY()) / scale;
        int i = Mouse.getDWheel();
        for(IEventHandler iE : getIeventBuffer()){
            iE.handleMouseInput(i, mX, mY);
        }
        FancyChat.getInstance().handleMouseInput(i);

    }

    /**
     * prints the message to the clientchat
     * @param message the message
     **/
    private static void printMessage(String message) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(
                new ChatComponentText(message));
    }
}
