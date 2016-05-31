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

package de.unaussprechlich.hudpixelextended.newcomponents;

import com.palechip.hudpixelmod.config.HudPixelConfig;
import de.unaussprechlich.hudpixelextended.fancychat.FancyChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.util.ArrayList;

public class OnlineFriendsComponent {

//######################################################################################################################

    // [SETTING] the friendlist separation message
    private static final String SEPARATION_MESSAGE = "-----------------------------------------------------";
    // [SETTING] the friendlist headline message
    private static final String FRIENDS_LIST_START = "                           Friends (Page ";
    // [SETTING] the friendlist offline message
    private static final String IS_CURRENTLY_OFFLINE = " is currently offline";
    // [SETTING] time between every /f list X request (in ms)
    private static final int REQUEST_DELAY = 10000;
    // [SETTING] time between every friendlist update (in ms)
    private static final int UPDATE_DELAY = 30000;
    // [SETTING] maximal shown online players
    private static final int MAX_SHOWN_ONLINE_FRIENDS = 20;

//######################################################################################################################

    private ArrayList<String> onlineFriendsStrings = new ArrayList<String>();

    private boolean friendListExpected = false;
    public  boolean requireUpdate = false;
    private boolean nextRequest = false;
    private boolean isFriendList = false;
    private int nextPageCounter = 0;
    private int page = 1;
    private long lastUpdate = 0;
    private long lastRequest = 0;

    /**
     * listens to the chatevents if there is a requested friendlist
     * @param e ClientChatReceivedEven
     * @firedBY HudPixelExtendedEventHandler -> onChat()
     */
    public void onChat(ClientChatReceivedEvent e) {

        // checks first if there is a request
        if (friendListExpected) {

            String m = e.message.getUnformattedText();

            //starts and stops the friendlist-chat-parser by the seperation message
            if (m.startsWith(SEPARATION_MESSAGE)) {
                if (isFriendList) {
                    nextPageCounter = 0;
                    page = 1;
                    isFriendList = false;
                    friendListExpected = false;
                } else {
                    isFriendList = true;
                }

            //checks if the messae is not the friendlist-header and if the player is not offline
            } else if (!m.contains(IS_CURRENTLY_OFFLINE) && !m.startsWith(FRIENDS_LIST_START)) {
                chatParser(m);
                nextPageCounter++;

                //if there is are more players online, than the first page shows the process runs again
                if (nextPageCounter == 8) {
                    nextRequest = true;
                    nextPageCounter = 0;
                }
            }
        }
        e.setCanceled(true);
    }


    /**
     * renders the friendliste when the GuiIngameMenu is shwon
     * @firedBY HudPixelExtendedEventHandler -> onRenderTick
     */
    public void renderOnlineFriends() {

        int xStart = HudPixelConfig.displayXOffset;
        int yStart = HudPixelConfig.displayXOffset;

        FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRendererObj;

        if (friendListExpected || onlineFriendsStrings.isEmpty()) {
            fontRenderer.drawStringWithShadow(EnumChatFormatting.GRAY + "Loading ... (try reopening the menu)", xStart, yStart, 0xffffff);
        } else {
            for (Object s : onlineFriendsStrings) {
                fontRenderer.drawStringWithShadow((String) s, xStart, yStart, 0xffffff);
                yStart += FancyChat.RENDERING_HEIGHT_OFFSET;
            }
        }
    }

    /**
     * function which checks if the friendlist is outdated and if there is another
     * request for a second,... /f list X command
     * @firedBY HudPixelExtendedEventHandler -> onClientTick()
     */
    public void onClientTick() {
        if (requireUpdate) {
            if (System.currentTimeMillis() > (lastUpdate + UPDATE_DELAY)) {
                System.out.println("FRIENDLIST -> updaten: machen wir :D");
                updateOnlineFriends();
                requireUpdate = false;
            }
        } else if (nextRequest) {
            if (System.currentTimeMillis() > (lastRequest + REQUEST_DELAY)) {
                page++;
                nextRequest = false;
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/f list " + page);
            }
        }
    }

    /**
     * a internal function that triggers the update process and resets the request/update time
     */
    private void updateOnlineFriends() {
        onlineFriendsStrings.clear();
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/f list");
        friendListExpected = true;
        lastRequest = System.currentTimeMillis();
        lastUpdate = System.currentTimeMillis();
    }

    /**
     * a internal function which separates the message and extracts player, game and servertype
     * @param m message to parse
     */
    private void chatParser(String m) {

        //displays something if there is nothing to display or if the display is reloading
        if (onlineFriendsStrings.size() == MAX_SHOWN_ONLINE_FRIENDS) {
            onlineFriendsStrings.add(EnumChatFormatting.GRAY + "You have too many online friends ...");
        } else if (onlineFriendsStrings.size() > MAX_SHOWN_ONLINE_FRIENDS) {
            return;
        }

        //splits the string
        String[] singleWords = m.split(" ");

        //the first element has to be the playername
        String playerName = singleWords[0];
        String gameType = "NULL";

        //if the player is in any gamemode game/lobby
        if (singleWords[3].equalsIgnoreCase("a")) {
            gameType = singleWords[4];
            //needed to add support for games like Crazy Walls who are written in two words
            for (int i = 5; i <= singleWords.length; i++) {
                gameType += singleWords[i];
            }

        // if the player is in housing
        } else if (singleWords[3].equalsIgnoreCase("housing")) {
            gameType = singleWords[3];

        // if the player is idling in the limbo
        } else if (singleWords[2].equalsIgnoreCase("idle")) {
            gameType = EnumChatFormatting.GRAY + "idle in Limbo";
        }

        // creates the string to display
        onlineFriendsStrings.add(""
                + EnumChatFormatting.GOLD
                + playerName
                + EnumChatFormatting.WHITE
                + " -> "
                + EnumChatFormatting.GREEN
                + gameType);
    }
}
