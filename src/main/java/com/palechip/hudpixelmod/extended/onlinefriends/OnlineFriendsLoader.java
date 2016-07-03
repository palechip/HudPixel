package com.palechip.hudpixelmod.extended.onlinefriends;

import com.palechip.hudpixelmod.api.interaction.Queue;
import com.palechip.hudpixelmod.api.interaction.callbacks.FriendResponseCallback;
import com.palechip.hudpixelmod.api.interaction.callbacks.SessionResponseCallback;
import com.palechip.hudpixelmod.api.interaction.representations.Friend;
import com.palechip.hudpixelmod.api.interaction.representations.Session;
import com.palechip.hudpixelmod.config.HudPixelConfig;
import com.palechip.hudpixelmod.extended.util.IEventHandler;
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
public class OnlineFriendsLoader implements FriendResponseCallback, IEventHandler, SessionResponseCallback{

    private static final int REQUEST_COOLDOWN = 5 * 60 * 1000; // = 5min
    private static final String JOINED_MESSAGE = " joined.";
    private static final String LEFT_MESSAGE = " left.";

    private static long lastRequest;
    private static String playerName;
    private static ArrayList<String> friendsFromApi = new ArrayList<String>();
    private static HashMap<String, OnlineFriend> onlineFriends = new HashMap<String, OnlineFriend>();

    private static boolean isApiLoaded = false;

    public static boolean isApiLoaded() {
        return isApiLoaded;
    }

    public void setupLoader(){
        playerName = Minecraft.getMinecraft().thePlayer.getName();
        requestFriends(true);
    }

    private OnlineFriendsLoader(){
        setupLoader();
    }

    private void requestFriends(Boolean forceRequest){
        if(HudPixelConfig.useAPI && HudPixelConfig.displayNetworkBoosters) {
            // check if enough time has past
            if((System.currentTimeMillis() > lastRequest + REQUEST_COOLDOWN)  || forceRequest) {
                // save the time of the request
                lastRequest = System.currentTimeMillis();
                // tell the queue that we need boosters
                Queue.getInstance().getFriends(this, playerName);
            }
        }
    }

    @Override
    public void onFriendResponse(ArrayList<Friend> friends) {
        friendsFromApi.clear();
        for(Friend f : friends){
            friendsFromApi.add(f.getFriendName());
        }
        generateFriends();
        isApiLoaded = true;
    }

    @Override
    public void onSessionRespone(Session session) {
        if(onlineFriends.containsKey(session.getSessionOwner())){
            onlineFriends.get(session.getSessionOwner()).setGamemode(session.getGameType().getName());
        }
    }

    private void generateFriends(){
        for(String s : friendsFromApi){
            if(!onlineFriends.containsKey(s)){
                onlineFriends.put(s, new OnlineFriend(s, "not loaded yet!"));
            }
        }
    }

    @Override
    public void onClientTick() {requestFriends(false);}

    @Override
    public void onChatReceived(ClientChatReceivedEvent e) throws Throwable {
        for(String s : friendsFromApi){
            if(e.message.getUnformattedText().equalsIgnoreCase(s + JOINED_MESSAGE))
                onlineFriends.get(s).setOnline(true);
            else if(e.message.getUnformattedText().equalsIgnoreCase(s + LEFT_MESSAGE))
                onlineFriends.get(s).setOnline(false);
        }
    }

    @Override
    public void onRender() {}

    @Override
    public void handleScrollInput(int i) {}


}
