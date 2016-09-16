/******************************************************************************
 * HudPixelExtended by unaussprechlich(github.com/unaussprechlich/HudPixelExtended),
 * an unofficial Minecraft Mod for the Hypixel Network.
 * <p>
 * Original version by palechip (github.com/palechip/HudPixel)
 * "Reloaded" version by PixelModders > Eladkay (github.com/PixelModders/HudPixel)
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

import com.palechip.hudpixelmod.extended.HudPixelExtendedEventHandler;
import com.palechip.hudpixelmod.extended.util.IEventHandler;
import com.palechip.hudpixelmod.extended.util.LoggerHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.util.HashMap;

public class OnlineFriendsUpdater implements IEventHandler {

    //######################################################################################################################
    // [SETTING] the friendlist separation message
    private static final String SEPARATION_MESSAGE = "-----------------------------------------------------";
    // [SETTING] the friendlist headline message
    private static final String FRIENDS_LIST_START = "Friends (Page ";
    // [SETTING] the friendlist offline message
    private static final String IS_CURRENTLY_OFFLINE = " is currently offline";
    // [SETTING] time between every request(in ms)
    private static final int REQUEST_DELAY = 5000;
    //######################################################################################################################

    private HashMap<String, String> onlineFriends = new HashMap<String, String>();
    private boolean friendListExpected = false;
    private boolean friendsListStarted = false;
    private boolean maybeFriendsList = false;
    private long lastRequest = 0;
    private boolean hasFinished = false;

    private int count = 0;
    private int page = 1;
    private boolean requestNextPage = false;

    private IUpdater callback;

    OnlineFriendsUpdater(IUpdater iUpdater) {
        LoggerHelper.logInfo("[OnlineFriends][Updater]: going to update your online friends!");
        HudPixelExtendedEventHandler.registerIEvent(this);
        onlineFriends.clear();
        callback = iUpdater;
        requestPage(1);
    }

    private void requestPage(int page) {
        requestNextPage = false;
        lastRequest = System.currentTimeMillis();
        friendListExpected = true;
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/f list " + page);
    }

    /**
     * function which checks if the friendlist is outdated and if there is another
     * request for a second,... /f list X command
     *
     * @firedBY HudPixelExtendedEventHandler > onClientTick()
     */
    @Override
    public void onClientTick() {
        if (requestNextPage && System.currentTimeMillis() > (lastRequest + REQUEST_DELAY)) {
            LoggerHelper.logInfo("[OnlineFriends][Updater]: Why do you have so many friends ... i will request page " + page + " now!");
            requestPage(page);
        }
    }

    /**
     * listens to the chatevents if there is a requested friendlist
     *
     * @param e ClientChatReceivedEven
     * @firedBY HudPixelExtendedEventHandler > onChat()
     */
    @Override
    public void onChatReceived(ClientChatReceivedEvent e) throws Throwable {
        //TODO: THIS IS PRETTY AF ... I NEED MORE VARS TO MAKE IT WORK ... YOU CAN STILL UNDERSTAND HOW IT SEPARATES o.O
        // checks first if there is a request
        if (friendListExpected) {
            String m = e.message.getUnformattedText();
            //starts and stops the friendlistchatparser by the separation message

            if (m.startsWith(SEPARATION_MESSAGE)) {
                if (friendsListStarted) {
                    resetFriendList(false);
                } else {
                    maybeFriendsList = true;
                }

            } else if (maybeFriendsList) {
                if (m.contains(FRIENDS_LIST_START)) {
                    maybeFriendsList = false;
                    friendsListStarted = true;
                } else
                    resetFriendList(true);
            } else if (m.contains(IS_CURRENTLY_OFFLINE)) {
                hasFinished = true;
            } else if (!m.contains(IS_CURRENTLY_OFFLINE) && friendsListStarted) {
                count++;
                if (count >= 8) {
                    page++;
                    requestNextPage = true;
                }
                chatParser(m);
            }


            // deletes the message
            e.setCanceled(true);
        }
    }

    /**
     * a internal function which separates the message and extracts player, game and servertype
     *
     * @param m message to parse
     */
    private void chatParser(String m) {


        //splits the string
        String[] singleWords = m.split(" ");

        //the first element has to be the playername
        String playerName = singleWords[0];
        String gameType = EnumChatFormatting.GRAY + "in an unknown realm.";

        //if the player is in any gamemode game/lobby
        if (singleWords[3].equalsIgnoreCase("a")) {

            if (singleWords[singleWords.length - 1].equalsIgnoreCase("game"))
                gameType = EnumChatFormatting.RED + singleWords[4];
            else if (singleWords[singleWords.length - 1].equalsIgnoreCase("lobby"))
                gameType = EnumChatFormatting.GREEN + singleWords[4];

            //needed to add support for games like Crazy Walls who are written in two words
            for (int i = 5; i < singleWords.length; i++) {
                gameType += " ";
                gameType += singleWords[i];
            }

            // if the player is in housing
        } else if (singleWords[3].equalsIgnoreCase("housing"))
            gameType = EnumChatFormatting.GREEN + singleWords[3];

            // if the player is idling in the limbo
        else if (singleWords[2].equalsIgnoreCase("idle"))
            gameType = EnumChatFormatting.GRAY + "idle in Limbo";
        LoggerHelper.logInfo("[OnlineFriends][Updater]: [" + playerName + "] -> " + gameType);
        onlineFriends.put(playerName, gameType);
    }

    private void resetFriendList(boolean error) {
        if (error) {
            callback.onUpdaterResponse(null);
            LoggerHelper.logInfo("[OnlineFriends][Updater]: Something went wrong on Page: " + page + " Line: " + count + "!");
            HudPixelExtendedEventHandler.unregisterIEvent(this);
            callback.onUpdaterResponse(null);
        } else {
            if (hasFinished) {
                HudPixelExtendedEventHandler.unregisterIEvent(this);
                callback.onUpdaterResponse(onlineFriends);
            }
            friendsListStarted = false;
            maybeFriendsList = false;
            friendListExpected = false;
            hasFinished = false;
        }
    }

    @Override
    public void onRender() {
    }

    @Override
    public void handleMouseInput(int i, int mX, int mY) {
    }

    @Override
    public void onMouseClick(int mX, int mY) {
    }


}