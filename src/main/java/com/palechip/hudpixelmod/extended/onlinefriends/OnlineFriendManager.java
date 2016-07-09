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

package com.palechip.hudpixelmod.extended.onlinefriends;

import com.palechip.hudpixelmod.extended.configuration.Config;
import com.palechip.hudpixelmod.extended.util.LoggerHelper;
import com.palechip.hudpixelmod.extended.util.gui.FancyListManager;
import com.palechip.hudpixelmod.extended.util.gui.FancyListObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class OnlineFriendManager extends FancyListManager implements IUpdater{

    private static final String JOINED_MESSAGE = " joined.";
    private static final String LEFT_MESSAGE = " left.";
    private static final int UPDATE_COOLDOWN_RENDERING = 10 * 1000; // = 10sec
    private static long lastUpdateRendering = 0;
    private static final int UPDATE_COOLDOWN_ONLINE= 2* 60 * 1000; // = 10sec
    private static long lastUpdateOnline = 0;
    private static OnlineFriendManager instance;

    public static OnlineFriendManager getInstance() {
        if(instance == null) instance = new OnlineFriendManager();
        return instance;
    }

    private OnlineFriendManager(){
        super(8);
        this.isButtons = true;
        new OnlineFriendsLoader();
    }

    public void addFriend(FancyListObject fco){
        this.fancyListObjects.add(fco);
    }

    private void updateRendering(){
        if((System.currentTimeMillis() > lastUpdateRendering + UPDATE_COOLDOWN_RENDERING)) {
            lastUpdateRendering = System.currentTimeMillis();
            if(!fancyListObjects.isEmpty())
                 for(FancyListObject fco : fancyListObjects)
                         fco.onClientTick();
            //sort the list to display only friends first
            Collections.sort(fancyListObjects, new Comparator<FancyListObject>() {
                @Override
                public int compare(FancyListObject f1, FancyListObject f2) {
                    OnlineFriend o1 = (OnlineFriend) f1;
                    OnlineFriend o2 = (OnlineFriend) f2;
                    return Boolean.valueOf(o2.isOnline()).compareTo(o1.isOnline());
                }
            });
        }
    }

    @Override
    public void onClientTick() {
        this.shownObjects = Config.friendsShownAtOnce;
        if((System.currentTimeMillis() > lastUpdateOnline + UPDATE_COOLDOWN_ONLINE) && !fancyListObjects.isEmpty()) {
            lastUpdateOnline = System.currentTimeMillis();
            new OnlineFriendsUpdater(this);
        }
        updateRendering();
    }

    @Override
    public void onChatReceived(ClientChatReceivedEvent e) throws Throwable {
        for(String s : OnlineFriendsLoader.getAllreadyStored()){
            if(e.message.getUnformattedText().equalsIgnoreCase(s + JOINED_MESSAGE))
                for (FancyListObject fco : fancyListObjects){
                    OnlineFriend of = (OnlineFriend) fco;
                    if(of.getUsername().equals(s)){
                        of.setOnline(true);
                        of.setGamemode(EnumChatFormatting.WHITE + "not loaded yet!");
                    }

                }

            else if(e.message.getUnformattedText().equalsIgnoreCase(s + LEFT_MESSAGE))
                for (FancyListObject fco : fancyListObjects){
                    OnlineFriend of = (OnlineFriend) fco;
                    if(of.getUsername().equals(s)){
                        of.setOnline(false);
                        of.setGamemode(EnumChatFormatting.DARK_GRAY + "currently offline");
                    }
                }
        }
    }

    @Override
    public void onRender() {
        if(Minecraft.getMinecraft().currentScreen instanceof GuiIngameMenu && lastUpdateRendering != 0 && OnlineFriendsLoader.isApiLoaded()){
            this.renderDisplay();
            this.isMouseHander = true;
        } else {
            this.isMouseHander = false;
        }

    }

    @Override
    public void onUpdaterResponse(HashMap<String, String> onlineFriends) {
        if(onlineFriends == null){
            LoggerHelper.logWarn("[OnlineFriends][Updater]: Something went wrong while calling a update!");
        } else if(!fancyListObjects.isEmpty()){
            for(FancyListObject fco : fancyListObjects){
                OnlineFriend of = (OnlineFriend) fco;
                if(onlineFriends.containsKey(of.getUsername())){
                    of.setGamemode(onlineFriends.get(of.getUsername()));
                    of.setOnline(true);
                } else {
                    of.setGamemode(EnumChatFormatting.DARK_GRAY + "currently offline");
                    of.setOnline(false);
                }
            }
        }
    }
}
