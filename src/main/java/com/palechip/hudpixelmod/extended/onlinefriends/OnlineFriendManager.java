package com.palechip.hudpixelmod.extended.onlinefriends;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.util.ArrayList;

import static com.palechip.hudpixelmod.extended.onlinefriends.OnlineFriendsUpdater.friendListExpected;

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
public class OnlineFriendManager {

    private static final int FRIENDDYSPLAY_OFFSET = 24;

    private static ArrayList<OnlineFriend> onlineFriendsListBUFFER = new ArrayList<OnlineFriend>();
    private static ArrayList<OnlineFriend> onlineFriendsList = new ArrayList<OnlineFriend>();

    public void renderOnlineFriends(){
        FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRendererObj;

        int xStart = 2;
        int yStart = 2;

        ArrayList<OnlineFriend> ofList =  onlineFriendsList;

        if(ofList.isEmpty()){
            fontRenderer.drawStringWithShadow(EnumChatFormatting.GRAY + "Loading ... (try reopening the menu)", xStart, yStart, 0xffffff);
        } else if(friendListExpected){
            fontRenderer.drawStringWithShadow(EnumChatFormatting.GRAY + "Loading ... ", xStart, yStart, 0xffffff);
        } else {
            for (OnlineFriend of : ofList){
                of.renderOnlineFriend(xStart,yStart);
                yStart += FRIENDDYSPLAY_OFFSET;
            }
        }
    }

    public void update(){
        onlineFriendsList = new ArrayList<OnlineFriend>(onlineFriendsListBUFFER);
        onlineFriendsListBUFFER.clear();

    }

    void addPlayer(String playerName, String gameType) {
        if(!onlineFriendsList.isEmpty()) {
            for (OnlineFriend of : onlineFriendsList) {
                if (of.getUsername().equalsIgnoreCase(playerName)) {
                    if (of.getGamemode().equalsIgnoreCase(gameType)) {
                        onlineFriendsListBUFFER.add(of);
                        return;
                    } else {
                        OnlineFriend ofBUFFER;
                        ofBUFFER = of;
                        ofBUFFER.setGamemode(gameType);
                        onlineFriendsListBUFFER.add(ofBUFFER);
                        return;
                    }
                }
            }
        }

        onlineFriendsListBUFFER.add(new OnlineFriend(playerName, gameType));
    }

}
