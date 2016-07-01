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
import com.palechip.hudpixelmod.extended.footballdisplay.FootBallDisplay;
import com.palechip.hudpixelmod.extended.onlinefriends.OnlineFriend;
import com.palechip.hudpixelmod.extended.onlinefriends.OnlineFriendsUpdater;
import com.palechip.hudpixelmod.extended.statsviewer.StatsViewerManager;
import com.palechip.hudpixelmod.extended.util.IEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

public class HudPixelExtendedEventHandler{

    private static ArrayList<IEvent> iEventArrayList = new ArrayList<IEvent>();

    public static void registerIEvent(IEvent iEvent){
        iEventArrayList.add(iEvent);
    }

    public static void unregisterIEvent(IEvent iEvent){
        iEventArrayList.remove(iEvent);
    }

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Pre e){
        try {
            //Don't do anything unless we are on Hypixel
            if(HypixelNetworkDetector.isHypixelNetwork ) {

                //just triggeres the statsrenderer if the player is waiting for the game to start
                if(!(HudPixelMod.instance().gameDetector.isInLobby())
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

                FancyChat.getInstance().openGui();
                OnlineFriendsUpdater.requireUpdate=true;

            } else if(Config.isDebuging){
                FancyChat.getInstance().openGui();
            }
        } catch (Exception ex) {
            HudPixelMod.instance().logWarn("[Extended] An exception occurred in onOpenGui(). Stacktrace below.");
            ex.printStackTrace();
        }
    }

    private ArrayList<IEvent> getiEventBUFFER(){
        return new ArrayList<IEvent>(iEventArrayList);
    }

    @SubscribeEvent(receiveCanceled=true)
    public void onChatMessage(ClientChatReceivedEvent e) {
        try {
            //Don't do anything unless we are on Hypixel
            if (HypixelNetworkDetector.isHypixelNetwork) {

                for(IEvent i : getiEventBUFFER()){
                    i.onChatReceived(e);
                }

                if(Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().thePlayer.getName() != null)
                if(e.message.getUnformattedText().equals("We are about to send you the Football Resource Pack")){
                    HudPixelExtended.footBallDisplay = new FootBallDisplay();
                }

                FancyChat.getInstance().onChat(e);
                OnlineFriendsUpdater.onChat(e);
            } else if(Config.isDebuging){
                FancyChat.getInstance().onChat(e);
            }
        } catch (Exception ex) {
            HudPixelMod.instance().logWarn("[Extended]An exception occurred in onChatMessage(). Stacktrace below.");
            ex.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @SubscribeEvent(receiveCanceled=true)
    public void onClientTick(TickEvent.ClientTickEvent e) {
        try {
            //Don't do anything unless we are on Hypixel
            if (HypixelNetworkDetector.isHypixelNetwork) {

                for(IEvent i : getiEventBUFFER()){
                    i.onClientTick();
                }

                handleMouseScroll();

                //Tick for FancyChat
                FancyChat.getInstance().onClientTick();

                //Tick for the statsViewerManager
                if(!(HudPixelMod.instance().gameDetector.isInLobby())
                        && !(HudPixelMod.instance().gameDetector.getCurrentGame().hasGameStarted()))
                    StatsViewerManager.onClientTick();

                //Tick for the friends list
                if(Minecraft.getMinecraft().currentScreen instanceof GuiIngameMenu){
                    OnlineFriendsUpdater.onClientTick();
                    OnlineFriend.onClientTick();
                }


            } else if(Config.isDebuging){
                FancyChat.getInstance().onClientTick();
            }
        } catch (Exception ex) {
            HudPixelMod.instance().logWarn("[Extended]An exception occurred in onClientTick(). Stacktrace below.");
            ex.printStackTrace();
        }
    }

    @SubscribeEvent(receiveCanceled=true)
    public void onRenderTick(TickEvent.RenderTickEvent e) {
        try {
            //Don't do anything unless we are on Hypixel
            if (HypixelNetworkDetector.isHypixelNetwork) {

                for(IEvent i : getiEventBUFFER()){
                    i.onRender();
                }

                if(Config.isFancyChat) FancyChat.getInstance().onRenderTick();

                if(Minecraft.getMinecraft().currentScreen instanceof GuiIngameMenu && Config.isFriendsDisplay){
                    HudPixelExtended.onlineFriendsManager.renderOnlineFriends();
                }
            } else if(Config.isDebuging){
                FancyChat.getInstance().onRenderTick();
            }
        } catch (Exception ex) {
            HudPixelMod.instance().logWarn("[Extended]An exception occurred in omRenderTick). Stacktrace below.");
            ex.printStackTrace();
        }
    }

    private void handleMouseScroll(){

        int i = Mouse.getDWheel();

        if (Minecraft.getMinecraft().currentScreen instanceof GuiChat) {

            for(IEvent iE : getiEventBUFFER()){
                iE.handleScrollInput(i);
            }

        }

    }

}
