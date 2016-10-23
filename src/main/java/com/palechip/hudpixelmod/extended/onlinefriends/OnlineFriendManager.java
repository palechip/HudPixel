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

import com.palechip.hudpixelmod.extended.util.LoggerHelper;
import com.palechip.hudpixelmod.extended.util.gui.FancyListManager;
import com.palechip.hudpixelmod.extended.util.gui.FancyListObject;
import com.palechip.hudpixelmod.util.ConfigPropertyBoolean;
import com.palechip.hudpixelmod.util.ConfigPropertyInt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class OnlineFriendManager extends FancyListManager implements IUpdater {

    private static final String JOINED_MESSAGE = " joined.";
    private static final String LEFT_MESSAGE = " left.";
    private static final int UPDATE_COOLDOWN_RENDERING = 10 * 1000; // = 10sec
    private static long lastUpdateRendering = 0;
    private static final int UPDATE_COOLDOWN_ONLINE = 2 * 60 * 1000; // = 2min
    private static long lastUpdateOnline = 0;
    private static OnlineFriendManager instance;

    private static ArrayList<FancyListObject> localStorageFCO = new ArrayList<FancyListObject>();

    public static OnlineFriendManager getInstance() {
        return instance == null ? instance = new OnlineFriendManager() : instance;
    }

    @ConfigPropertyInt(catagory = "hudpixel", id = "xOffsetFriendsDisplay", comment = "X offset for friends display", def = 2)
    public static int xOffsetFriendsDisplay = 2;

    @ConfigPropertyInt(catagory = "hudpixel", id = "yOffsetFriendsDisplay", comment = "Y offset for friends display", def = 2)
    public static int yOffsetFriendsDisplay = 2;

    @ConfigPropertyInt(catagory = "hudpixel", id = "friendsShownAtOnce", comment = "Friends shown at once", def = 10)
    public static int friendsShownAtOnce = 2;

    @ConfigPropertyBoolean(catagory = "hudpixel", id = "shownFriendsDisplayRight", comment = "Show friends display on the right", def = false)
    public static boolean shownFriendsDisplayRight = false;

    @ConfigPropertyBoolean(catagory = "hudpixel", id = "hideOfflineFriends", comment = "Hide offline friends?", def = true)
    public static boolean hideOfflineFriends = true;

    private OnlineFriendManager() {
        super(5, xOffsetFriendsDisplay, yOffsetFriendsDisplay, shownFriendsDisplayRight);
        this.isButtons = true;
        new OnlineFriendsLoader();
    }

    void addFriend(FancyListObject fco) {
        localStorageFCO.add(fco);
    }

    private void updateRendering() {
        if ((System.currentTimeMillis() > lastUpdateRendering + UPDATE_COOLDOWN_RENDERING)) {
            lastUpdateRendering = System.currentTimeMillis();

            if (!localStorageFCO.isEmpty())

                localStorageFCO.forEach(FancyListObject::onClientTick);

            //sort the list to display only friends first
            Collections.sort(localStorageFCO, (f1, f2) -> {
                OnlineFriend o1 = (OnlineFriend) f1;
                OnlineFriend o2 = (OnlineFriend) f2;
                return Boolean.valueOf(o2.isOnline()).compareTo(o1.isOnline());
            });

            if (hideOfflineFriends) {
                ArrayList<FancyListObject> buff = new ArrayList<FancyListObject>();
                for (FancyListObject fco : localStorageFCO) {
                    OnlineFriend of = (OnlineFriend) fco;
                    if (of.isOnline()) buff.add(fco);
                }
                fancyListObjects = buff;
            } else {
                fancyListObjects = localStorageFCO;
            }
        }
    }

    @Override
    public void onClientTick() {
        this.renderRightSide = shownFriendsDisplayRight;
        this.shownObjects = friendsShownAtOnce;
        if ((System.currentTimeMillis() > lastUpdateOnline + UPDATE_COOLDOWN_ONLINE) && !localStorageFCO.isEmpty()) {
            lastUpdateOnline = System.currentTimeMillis();
            new OnlineFriendsUpdater(this);
        }
        updateRendering();
    }

    @Override
    public void onChatReceived(ClientChatReceivedEvent e) throws Throwable {
        for (String s : OnlineFriendsLoader.getAllreadyStored()) {
            if (e.message.getUnformattedText().equalsIgnoreCase(s + JOINED_MESSAGE))
                for (FancyListObject fco : localStorageFCO) {
                    OnlineFriend of = (OnlineFriend) fco;
                    if (of.getUsername().equals(s)) {
                        of.setOnline(true);
                        of.setGamemode(EnumChatFormatting.WHITE + "not loaded yet!");
                    }
                }
            else if (e.message.getUnformattedText().equalsIgnoreCase(s + LEFT_MESSAGE))
                for (FancyListObject fco : localStorageFCO) {
                    OnlineFriend of = (OnlineFriend) fco;
                    if (of.getUsername().equals(s)) {
                        of.setOnline(false);
                        of.setGamemode(EnumChatFormatting.DARK_GRAY + "currently offline");
                    }
                }
        }
    }

    @Override
    public void onRender() {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiChat && lastUpdateRendering != 0 && OnlineFriendsLoader.isApiLoaded() && OnlineFriendsLoader.enabled) {
            this.renderDisplay();
            this.isMouseHander = true;
        } else {
            this.isMouseHander = false;
        }
    }

    @Override
    public void onUpdaterResponse(HashMap<String, String> onlineFriends) {
        if (onlineFriends == null) {
            LoggerHelper.logWarn("[OnlineFriends][Updater]: Something went wrong while calling a update!");
        } else if (!localStorageFCO.isEmpty()) {
            for (FancyListObject fco : localStorageFCO) {
                OnlineFriend of = (OnlineFriend) fco;
                if (onlineFriends.containsKey(of.getUsername())) {
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
