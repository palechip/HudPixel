package com.palechip.hudpixelmod.extended.onlinefriends;

import com.palechip.hudpixelmod.api.interaction.Queue;
import com.palechip.hudpixelmod.api.interaction.callbacks.FriendResponseCallback;
import com.palechip.hudpixelmod.api.interaction.representations.Friend;
import com.palechip.hudpixelmod.config.HudPixelConfig;
import com.palechip.hudpixelmod.extended.HudPixelExtendedEventHandler;
import com.palechip.hudpixelmod.extended.configuration.Config;
import com.palechip.hudpixelmod.extended.util.IEventHandler;
import com.palechip.hudpixelmod.extended.util.LoggerHelper;
import com.palechip.hudpixelmod.extended.util.gui.FancyListObject;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.util.ArrayList;
import java.util.HashMap;

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

    private static final int REQUEST_COOLDOWN = 20 * 1000; // = 5min

    private static long lastRequest;
    private static ArrayList<String> friendsFromApi = new ArrayList<String>();
    private static HashMap<String, OnlineFriend> onlineFriends = new HashMap<String, OnlineFriend>();
    private static boolean isApiLoaded = false;

    public static HashMap<String, OnlineFriend> getOnlineFriends() {
        return onlineFriends;
    }

    public static boolean isApiLoaded() {
        return isApiLoaded;
    }

    public static ArrayList<String> getFriendsFromApi() {
        return friendsFromApi;
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
                Queue.getInstance().getFriends(this, "");
            }
        }
    }

    @Override
    public void onFriendResponse(ArrayList<Friend> friends) {
        friendsFromApi.clear();
        for(Friend f : friends){
            if(f.getFriendName() != Minecraft.getMinecraft().thePlayer.getName())
                friendsFromApi.add(f.getFriendName());
                System.out.println(f.getFriendName());
        }
        LoggerHelper.logInfo("[OnlineFriends][APIloader]: Loaded a total of " + friends.size() + " friends!");
        generateFriends();
        isApiLoaded = true;
    }


    private void generateFriends(){
        for(String s : friendsFromApi){
            if(!onlineFriends.containsKey(s)){
                onlineFriends.put(s, new OnlineFriend(s, "not loaded yet!"));
            }
        }
        OnlineFriendManager.getInstance().setFLClist(new ArrayList<FancyListObject>(onlineFriends.values()));
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
    public void handleScrollInput(int i) {}


}
