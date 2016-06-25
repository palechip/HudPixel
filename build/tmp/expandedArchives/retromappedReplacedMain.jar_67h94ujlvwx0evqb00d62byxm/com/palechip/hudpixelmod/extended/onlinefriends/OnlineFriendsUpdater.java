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

import com.palechip.hudpixelmod.extended.HudPixelExtended;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class OnlineFriendsUpdater {

//######################################################################################################################

    // [SETTING] the friendlist separation message
    private static final String SEPARATION_MESSAGE = "-----------------------------------------------------";
    // [SETTING] the friendlist headline message
    private static final String FRIENDS_LIST_START = "                           Friends (Page ";
    // [SETTING] the friendlist offline message
    private static final String IS_CURRENTLY_OFFLINE = " is currently offline";
    // [SETTING] time between every friendlist update (in ms)
    private static final int UPDATE_DELAY = 30000;

//######################################################################################################################

    static boolean friendListExpected = false;
    public static boolean requireUpdate = false;
    private static boolean isFriendList = false;
    private static long lastUpdate = 0;

    /**
     * listens to the chatevents if there is a requested friendlist
     * @param e ClientChatReceivedEven
     * @firedBY HudPixelExtendedEventHandler -> onChat()
     */
    public static void onChat(ClientChatReceivedEvent e) {

        // checks first if there is a request
        if (friendListExpected) {

            String m = e.message.func_150260_c();

            //starts and stops the friendlist-chat-parser by the separation message
            if (m.startsWith(SEPARATION_MESSAGE)) {
                if (isFriendList) {
                    HudPixelExtended.onlineFriendsManager.update();
                    isFriendList = false;
                    friendListExpected = false;
                } else {
                    isFriendList = true;
                }

            //checks if the message is not the friendlist-header and if the player is not offline
            } else if(isFriendList) {
                if (!(m.contains(IS_CURRENTLY_OFFLINE)) && !(m.startsWith(FRIENDS_LIST_START))) {
                    chatParser(m);

                }
            }
            // deletes the message
            e.setCanceled(true);
        }
    }

    /**
     * function which checks if the friendlist is outdated and if there is another
     * request for a second,... /f list X command
     * @firedBY HudPixelExtendedEventHandler -> onClientTick()
     */
    public static void onClientTick() {
        if (requireUpdate) {
            if (System.currentTimeMillis() > (lastUpdate + UPDATE_DELAY)) {
                updateOnlineFriends();
                requireUpdate = false;
            }
        }
    }

    /**
     * a internal function that triggers the update process and resets the request/update time
     */
    private static void updateOnlineFriends() {
        Minecraft.func_71410_x().field_71439_g.func_71165_d("/f list");
        friendListExpected = true;
        lastUpdate = System.currentTimeMillis();
    }

    /**
     * a internal function which separates the message and extracts player, game and servertype
     * @param m message to parse
     */
    private static void chatParser(String m) {

        //splits the string
        String[] singleWords = m.split(" ");

        //the first element has to be the playername
        String playerName = singleWords[0];
        String gameType = "NULL";

        //if the player is in any gamemode game/lobby
        if (singleWords[3].equalsIgnoreCase("a")) {

            if(singleWords[singleWords.length-1].equalsIgnoreCase("game"))
                gameType = EnumChatFormatting.RED + singleWords[4];
            else
                gameType = singleWords[4];

            //needed to add support for games like Crazy Walls who are written in two words
            for (int i = 5; i < singleWords.length; i++) {
                gameType += " ";
                gameType += singleWords[i];
            }

        // if the player is in housing
        } else if (singleWords[3].equalsIgnoreCase("housing")) {
            gameType = singleWords[3];

        // if the player is idling in the limbo
        } else if (singleWords[2].equalsIgnoreCase("idle")) {
            gameType = EnumChatFormatting.GRAY + "idle in Limbo";
        }

        //updates the stored OnlinePlayers
        HudPixelExtended.onlineFriendsManager.addPlayer(playerName, gameType);

    }
}
