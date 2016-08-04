package com.palechip.hudpixelmod.extended.onlinefriends;

import com.palechip.hudpixelmod.api.interaction.Queue;
import com.palechip.hudpixelmod.api.interaction.callbacks.FriendResponseCallback;
import com.palechip.hudpixelmod.api.interaction.representations.Friend;
import com.palechip.hudpixelmod.config.HudPixelConfig;
import com.palechip.hudpixelmod.extended.HudPixelExtendedEventHandler;
import com.palechip.hudpixelmod.extended.configuration.Config;
import com.palechip.hudpixelmod.extended.util.IEventHandler;
import com.palechip.hudpixelmod.extended.util.LoggerHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.util.ArrayList;

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
public class OnlineFriendsLoader implements FriendResponseCallback, IEventHandler{

    private static final int REQUEST_COOLDOWN = 20 * 60 * 1000; // = 30min

    private static long lastRequest;
    private static ArrayList<String> allreadyStored = new ArrayList<String>();
    private static boolean isApiLoaded = false;

    public static ArrayList<String> getAllreadyStored() {
        return allreadyStored;
    }

    public static boolean isApiLoaded() {
        return isApiLoaded;
    }


    public void setupLoader(){
        HudPixelExtendedEventHandler.registerIEvent(this);
        requestFriends(true);
    }

    public OnlineFriendsLoader(){
        setupLoader();
    }

    private void requestFriends(Boolean forceRequest){
        if(HudPixelConfig.useAPI && Config.isFriendsDisplay) {
            // check if enough time has past
            if((System.currentTimeMillis() > lastRequest + REQUEST_COOLDOWN)  || forceRequest) {
                // save the time of the request
                lastRequest = System.currentTimeMillis();
                // tell the queue that we need boosters
                Queue.getInstance().getFriends(this, Minecraft.getMinecraft().thePlayer.getUniqueID());
            }
        }
    }

    @Override
    public void onFriendResponse(ArrayList<Friend> friends) {
        if(friends == null){
            LoggerHelper.logWarn("[OnlineFriends][APIloader]: The api answered the request with NULL!");
            return;
        }
        for(Friend f : friends){
            if(!allreadyStored.contains(f.getFriendName())){
                OnlineFriendManager.getInstance().addFriend(new OnlineFriend(f.getFriendName(), EnumChatFormatting.DARK_GRAY + "not loaded yet!", f.getFriendUUID()));
                allreadyStored.add(f.getFriendName());
            }
        }
        isApiLoaded = true;
        LoggerHelper.logInfo("[OnlineFriends][APIloader]: Loaded a total of " + friends.size() + " friends!");
    }


    @Override
    public void onClientTick() {
        requestFriends(false);
    }

    @Override
    public void onChatReceived(ClientChatReceivedEvent e) throws Throwable {}

    @Override
    public void onRender() {}

    @Override
    public void handleMouseInput(int i, int mX, int mY) {}

    @Override
    public void onMouseClick(int mX, int mY) {}


}
