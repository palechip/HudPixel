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

package com.palechip.hudpixelmod.extended.fancychat;

import com.palechip.hudpixelmod.extended.configuration.Config;
import com.palechip.hudpixelmod.extended.util.MessageBuffer;
import com.palechip.hudpixelmod.extended.util.RenderUtils;
import com.palechip.hudpixelmod.extended.util.SoundManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.util.ArrayList;
import java.util.List;

import static com.palechip.hudpixelmod.extended.fancychat.FancyChatFilter.*;

public class FancyChat {


//######################################################################################################################

    // [SETTING] the time a FancyChatMessage will be displayed (in ms)
    private static long displayTimeMs = Config.displayMessages * 1000;
    // [SETTING] the offset between each chatline (in ms)
    public static final int RENDERING_HEIGHT_OFFSET = 9;
    // [SETTING] the with of the fancy chat overlay
    public static final int FIELD_WIDTH = 325;
    // [SETTING] the with of the fancy chat overlay
    private static final int BOTTOM_Y_OFFSET = 28;
    // [SETTING] the with of the fancy chat overlay
    private static int MAX_STORED_FANCYCHATMESSAGES = Config.storedMessages;
    // [SETTING] the with of the fancy chat overlay
    private static final int MAX_SCROLLIST_LINES = 20;
    // [SETTING] the with of the fancy chat overlay
    private static final int SCROLLED_PER_NOTCH = 5;

//######################################################################################################################


    private static final String partyInvite1 = " has invited you to join their party!";
    private static final String partyInvite2 = "Click here to join! You have 60 seconds to accept.";
    private static final String spacer = "-----------------------------------------------------";
    private static final String nothing = " ";
    private static Boolean spacerPartyRequired = false;
    private int scroll = MAX_SCROLLIST_LINES;
    private static FancyChat fancyChatInstance;

    private ArrayList<FancyChatObject> fancyChatObjects = new ArrayList<FancyChatObject>();
    private MessageBuffer fancyChatMessages = new MessageBuffer(MAX_STORED_FANCYCHATMESSAGES);

    private FancyChat() {
    }

    public static FancyChat getInstance(){
        if(fancyChatInstance != null){
            return fancyChatInstance;
        } else {
            fancyChatInstance = new FancyChat();
            return fancyChatInstance;
        }
    }


    //------------------------------------------------------------------------------------------------------------------

    /**
     * Fired by the onRenderTick event @ HudPixelMod.class
     */
    public void onRenderTick() {

        //returns if there is nothing to render
        if (fancyChatObjects.isEmpty() && fancyChatMessages.messageBuffer.isEmpty()) return;
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        if (Minecraft.getMinecraft().displayWidth < 1300) {
            return;
        }

        if (Minecraft.getMinecraft().inGameHasFocus) {
            renderOverlay(fancyChatObjects, res);
        } else if (Minecraft.getMinecraft().currentScreen instanceof GuiChat) {
            if (fancyChatMessages != null && !fancyChatMessages.messageBuffer.isEmpty()) {
                int bufferSize = fancyChatMessages.messageBuffer.size();
                if (bufferSize <= MAX_SCROLLIST_LINES) {
                    renderOverlayGui(fancyChatMessages.getLast(bufferSize), res);
                } else if (MAX_SCROLLIST_LINES < bufferSize) {
                    renderOverlayGui(fancyChatMessages.getSubArray(scroll - MAX_SCROLLIST_LINES, scroll), res);
                } else if (scroll > bufferSize) {
                    renderOverlayGui(fancyChatMessages.getSubArray(bufferSize - MAX_SCROLLIST_LINES, bufferSize), res);
                } else {
                    renderOverlayGui(fancyChatMessages.getLast(MAX_SCROLLIST_LINES), res);
                }
            }
        }
    }

    /**
     * Method that resets the scroll-height by reopening the
     * GuiChat.
     *
     * @firedBY -> HudPixelMod.onGuiShown() -> EVENT
     */
    public void openGui() {
        scroll = MAX_SCROLLIST_LINES;
    }

    /**
     * Fired by the onClientTick event @ HudPixelMod.class
     * updates all the FancyChatObjects and delete them if they are expired
     */
    public void onClientTick() {

        // add a the secound spacer for the party-invite message
        if (spacerPartyRequired) {
            this.printMessage(nothing);
            this.printMessage(EnumChatFormatting.GOLD + spacer);
            spacerPartyRequired = false;
        }

        // updates the fancyChatObjects stored in the ArrayList and deletes them if there are outdated
        if (fancyChatObjects != null && !fancyChatObjects.isEmpty()) {
            ArrayList<FancyChatObject> fcoBufferRemove = new ArrayList<FancyChatObject>();
            for (FancyChatObject fco : fancyChatObjects) {
                if ((fco.getTimestamp() + displayTimeMs) <= System.currentTimeMillis()) {
                    fcoBufferRemove.add(fco);
                }
            }
            for (FancyChatObject fco : fcoBufferRemove) {
                fancyChatObjects.remove(fco);
            }
        }
    }


    /**
     * Fired by the onChat event @ HudPixelMod.class.
     *
     * @param e The chat event
     */
    public void onChat(ClientChatReceivedEvent e) {
        String message = e.message.getUnformattedText();

        // [IF] it is a partyinventation
        if (message.contains(partyInvite1) || message.contains(partyInvite2)) {
            partyChecker(message, e);

            // [IF] the message is blacklisted
        } else if (blacklistChecker(message)) {
            e.setCanceled(true);

        } else if (fancyChatChecker(message)) {
            addFancyChatEntry(e.message.getFormattedText());

            //playes a sound when the player recieves a message
            if (!message.contains(" " + Minecraft.getMinecraft().thePlayer.getName() + ": ")) {
                SoundManager.playSound(SoundManager.Sounds.NOTIFICATION_SHORT_WOOD);
            }

        } else if (message.startsWith("From ")) {
            addFancyChatEntry(e.message.getFormattedText());

            SoundManager.playSound(SoundManager.Sounds.NOTIFICATION_SHORT2);

            // [IF] the message is a FancyChatMessage
        } else if (message.startsWith("To ")) {
            addFancyChatEntry(e.message.getFormattedText());
        } else if (Config.isDebuging) {
            if (message.startsWith("<aussprechlich2> messagebuffer.clear")) {
                fancyChatMessages.messageBuffer.clear();
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    /**
     * small methode that adds a FancyChat message to the right buffers and adjust the scrollheight
     * @param s Formatted chat string
     */
    private void addFancyChatEntry(String s){
        fancyChatObjects.add(new FancyChatObject(s));
        fancyChatMessages.addMinecraftEntry(s);
        if(scroll < fancyChatMessages.messageBuffer.size()
                && scroll > MAX_SCROLLIST_LINES){
            scroll++;
        }
    }

    /**
     * renders the overlay, when you are in the GuiChat
     *
     * @param renderList renderList with the strings
     * @param res        the resolution of the screen
     * @firedBY -> this.onRenderTick()
     */
    private void renderOverlayGui(List<String> renderList, ScaledResolution res) {

        FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRendererObj;
        int yStart = res.getScaledHeight() - BOTTOM_Y_OFFSET - RENDERING_HEIGHT_OFFSET;
        int xStart = res.getScaledWidth() - FIELD_WIDTH - 2;
        int size = renderList.size();

        //draws the transparent box behind the text
        RenderUtils.renderBox(xStart, yStart - (size - 1) * RENDERING_HEIGHT_OFFSET,
                FIELD_WIDTH, RENDERING_HEIGHT_OFFSET * size, 0);

        //corrects the offset between background and text
        yStart += 1;

        //draws the strings & splits them with a method @ FancyChatObject.class
        for (String s : renderList) {
            fontRenderer.drawStringWithShadow(s, xStart, yStart, 0xffffff);
            yStart -= RENDERING_HEIGHT_OFFSET;
        }

        //renders the scrollbar
        if (!(size < MAX_SCROLLIST_LINES)) renderScrollBar(res);
    }

    /**
     * Don't try to understand it ... I don't understand it either, but somehow it works!
     * Maybe i should try paper&pencil to improve this wonderful code. Let's mark it - perhaps one day!
     * TODO: make it pretty :D
     *
     * @param res screen-resolution
     */
    private void renderScrollBar(ScaledResolution res) {

        //gets the height of the scroller in ratio to the currently stored messages
        double boxHeight = MAX_SCROLLIST_LINES * RENDERING_HEIGHT_OFFSET;
        double heightRatio = ((double) MAX_SCROLLIST_LINES / (double) (fancyChatMessages.messageBuffer.size()));
        double height = 0;
        if (heightRatio > 0) height = boxHeight * heightRatio;

        //gets the position of the scroller in ratio to the currently stored messages
        double posRatio = ((double) (scroll) / (double) (fancyChatMessages.messageBuffer.size()));
        if (posRatio > 1) posRatio = 1;
        double yPos = posRatio * (boxHeight);

        //start (x/y) of the scrollbar
        double yStart = res.getScaledHeight() - BOTTOM_Y_OFFSET - RENDERING_HEIGHT_OFFSET * MAX_SCROLLIST_LINES;
        double xStart = res.getScaledWidth() - FIELD_WIDTH - 2 - 5;

        //renders the scrollbar background
        RenderUtils.renderBoxWithColor(xStart, yStart, 5, MAX_SCROLLIST_LINES * RENDERING_HEIGHT_OFFSET, 0,
                0, 0, 0, 0.5F);

        //renders the scroller on the scrollbar
        RenderUtils.renderBoxWithColor(xStart, (yStart + boxHeight) - yPos, 3, height, 0,
                1, 1, 1, 1F);

    }

    /**
     * renders the overlay when you are ingame
     *
     * @param renderFco renderList with the fco elements
     * @param res       resolution of the screen
     * @firedBY -> this.onRenderTick()
     */
    private void renderOverlay(ArrayList<FancyChatObject> renderFco, ScaledResolution res) {

        FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRendererObj;
        int lines = 0;

        //gets the number of lines
        for (FancyChatObject fco : renderFco) {
            lines += fco.getSize();
        }

        // sets the correct start cords
        int yStart = res.getScaledHeight() - BOTTOM_Y_OFFSET - RENDERING_HEIGHT_OFFSET * lines;
        int xStart = res.getScaledWidth() - FIELD_WIDTH - 2;

        //draws the transparent box behind the text
        RenderUtils.renderBox(xStart, yStart, FIELD_WIDTH, RENDERING_HEIGHT_OFFSET * lines, 0);

        //draws the strings & splits them with a method @ FancyChatObject.class
        for (FancyChatObject fco : renderFco) {
            yStart += RENDERING_HEIGHT_OFFSET * fco.drawTextField(xStart, yStart, fontRenderer);
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    /**
     * @return -> [true] -> if the blacklist contains the message
     **/
    private Boolean fancyChatChecker(String m) {
        for (String a : fancyChatTriggers) {
            if (m.startsWith(a)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Performs a better party invite message and plays a sound
     *
     * @param message unformatted message received by the client
     * @param e       ClientChatReceivedEvent
     */
    private void partyChecker(String message, ClientChatReceivedEvent e) {

        if (message.contains(partyInvite1)) {
            this.printMessage(EnumChatFormatting.GOLD + spacer);
            this.printMessage(nothing);
            this.printMessage(e.message.getFormattedText());
            e.setCanceled(true);
            SoundManager.playSound(SoundManager.Sounds.NOTIFICATION_OLDSCHOOLMESSAGE);

        } else if (message.contains(partyInvite2)) {
            spacerPartyRequired = true;
        }
    }


    /**
     * @return true -> if the blacklist contains the message
     **/
    private Boolean blacklistChecker(String m) {
        for (String a : FancyChatFilter.blacklistStart) {
            if (m.startsWith(a)) {
                return true;
            }
        }
        for (String a : blacklistContains) {
            if (m.contains(a)) {
                return true;
            }
        }
        for (String a : blacklistEnds) {
            if (m.endsWith(a)) {
                return true;
            }
        }
        return false;
    }

    //------------------------------------------------------------------------------------------------------------------

    /**
     * prints the message to the clientchat
     *
     * @param message the message
     **/
    private void printMessage(String message) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(
                new ChatComponentText(message));
    }

    /**
     * Method that handel's the mouse input and sets the scroll-height.
     * Is fired every client tick
     *
     * @firedBY -> this.onClientTick()
     */
    public void handleMouseInput(int i) {

        if (i != 0) {
            if (i > 0) {
                if ((scroll + SCROLLED_PER_NOTCH) > fancyChatMessages.messageBuffer.size() - MAX_SCROLLIST_LINES) {
                    scroll = fancyChatMessages.messageBuffer.size();
                } else {
                    scroll += SCROLLED_PER_NOTCH;
                }

            } else if (i < 0) {
                if ((scroll - SCROLLED_PER_NOTCH) <= MAX_SCROLLIST_LINES) {
                    scroll = MAX_SCROLLIST_LINES;
                } else {
                    scroll -= SCROLLED_PER_NOTCH;
                }
            }
        }
    }


}


