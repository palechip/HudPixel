/*******************************************************************************
 * HudPixel Reloaded (github.com/palechip/HudPixel), an unofficial Minecraft Mod for the Hypixel Network
 *
 * Copyright (c) 2014-2015 palechip (twitter.com/palechip) and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package com.palechip.hudpixelmod.gui;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;

import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.api.interaction.Queue;
import com.palechip.hudpixelmod.api.interaction.callbacks.BoosterResponseCallback;
import com.palechip.hudpixelmod.api.interaction.representations.Booster;
import com.palechip.hudpixelmod.chat.BoosterQueueCommandParser;
import com.palechip.hudpixelmod.config.HudPixelConfig;
import com.palechip.hudpixelmod.games.GameManager;

import net.minecraftforge.fml.client.FMLClientHandler;

public class BoosterDisplay implements BoosterResponseCallback{
    private static final int REQUEST_COOLDOWN = 30000; // = 30s
    private static final int REFRESH_TIMEOUT = 120000; // 90s this is how often it refreshes when the chat gui stays open (e.g. when the person is afk)
    private static final String TITLE = EnumChatFormatting.RED + "Boosters";
    public static final int QUICK_LOAD_BUTTON_HEIGHT = 20;

    private BoosterDisplay instance;
    private long lastRequest;
    private ArrayList<String> renderingStrings;

    private ArrayList<Booster> tippedBoosters;
    private ArrayList<Booster> activeBoosters;
    private HashMap<String, Long> unprocessedTips;

    private boolean isInChatGui;
    private boolean hasFailed;
    private boolean isLoading;
    
    private GuiButton quickLoadButton;
    private boolean quickLoadLock = false;
    
    private BoosterQueueCommandParser commandParser;

    public BoosterDisplay() {
        this.instance = this;
        this.renderingStrings = new ArrayList<String>();
        this.tippedBoosters = new ArrayList<Booster>();
        this.activeBoosters = new ArrayList<Booster>();
        this.unprocessedTips = new HashMap<String, Long>();
        // params id:-10 x:doesn't matter y:doesn't matter h:doesn't matter w:20 displayString:Tip all
        this.quickLoadButton = new GuiButton(-10, 0,0, 50, QUICK_LOAD_BUTTON_HEIGHT, "Quick Load");
        this.commandParser = new BoosterQueueCommandParser(this);
    }

    /**
     * Updates the rendering strings for the booster display.
     * @param matchUnprocessedTips Should unprocessed tips be matched to the boosters and be cleared? Set to true if new boosters were loaded!
     */
    private void updateRenderStrings(boolean matchUnprocessedTips) {
        // copy the activeBoosters Array so it can't be modified during this process
        ArrayList<Booster> activeBoostersCache = new ArrayList<Booster>(this.activeBoosters);

        // create a new replacement list
        ArrayList<String> newRenderingStrings = new ArrayList<String>();
        newRenderingStrings.add(TITLE + (isLoading ? "(Loading...)" : (hasFailed ? "(Loading failed!)" : "")));

        ArrayList<Booster> tippedBoostersCache = new ArrayList<Booster>(this.tippedBoosters.size());
        // clean tippedBoosters
        for(Booster booster : this.tippedBoosters) {
            // if the booster is still tipped
            if(booster.isTipped()) {
                // re-add it to the new array
                tippedBoostersCache.add(booster);
            }
        }
        
        // replace the array
        this.tippedBoosters = tippedBoostersCache;

        for (Booster booster : activeBoostersCache) {
            // Check if this booster was tipped while it wasn't loaded by the display
            if(matchUnprocessedTips && !this.unprocessedTips.isEmpty()) {
                // check if there is an unprocessed tip with this name
                if(this.unprocessedTips.containsKey(booster.getOwner())) {
                    // set the booster tipped
                    booster.setTippingTime(this.unprocessedTips.get(booster.getOwner()));
                    this.tippedBoosters.add(booster);
                }
            }

            // Add all active boosters. Tipped ones are white. Untipped ones are green.
            newRenderingStrings.add(EnumChatFormatting.GOLD + GameManager.getGameManager().getGameConfiguration(booster.getGameID()).getShortName() + ": " + (this.tippedBoosters.contains(booster) ? EnumChatFormatting.WHITE : EnumChatFormatting.GREEN) + booster.getOwner());
        }

        // if the unprocessed tips were matched, clear the list
        if(matchUnprocessedTips) {
            this.unprocessedTips.clear();
        }

        // set the rendering strings
        renderingStrings = newRenderingStrings;
    }

    /**
     * Listen to the /booster queue output and tips which the player sent
     */
    public void onChatMessage(String textMessage, String formattedMessage) {
        // check the settings
        if(HudPixelConfig.useAPI && HudPixelConfig.displayNetworkBoosters) {
            // listen for the /booster queue command
            this.commandParser.onChat(textMessage);
            
            // handle a tip which the player sent
            if(textMessage.contains("You sent a") && textMessage.contains("tip of")) {
                // cut the extra stuff
                String name = textMessage.substring(0, textMessage.indexOf(" in"));
                
                // correctly handle the rank tag
                if(textMessage.contains("]")) {
                    name = name.substring(name.indexOf("]") + 2);
                } else {
                    name = name.substring(name.indexOf(" to ") + 4);
                }
                
                // set all boosters with this name to tipped
                boolean foundIt = false;
                for (Booster booster : activeBoosters) {
                    if(booster.getOwner().equalsIgnoreCase(name)) {
                        // save the tipping time and the state
                        booster.tip();
                        // and add it to the tipped boosters
                        this.tippedBoosters.add(booster);
                        // we found a booster and don't need to save
                        foundIt = true;
                    }
                }
                
                // if no booster 
                if(!foundIt) {
                    this.unprocessedTips.put(name, System.currentTimeMillis());
                }

                // refresh the display strings to reflect the newly tipped booster(s)
                this.updateRenderStrings(false);
            }
        }
    }
    
    /**
     * Detects when GuiChat is opened or left and if the boosters need to be refreshed.
     */
    public void onClientTick() {
        Minecraft mc = FMLClientHandler.instance().getClient();
        
        // if the GuiChat was just entered in a lobby
        if((mc.currentScreen instanceof GuiChat && HudPixelMod.instance().gameDetector.isInLobby() && !this.isInChatGui)) {
            // save the state
            this.isInChatGui = true;
            // activate the quick load button
            if(HudPixelConfig.displayQuickLoadButton && HudPixelMod.instance().renderer.isHUDShown) {
                this.quickLoadButton.visible = true;
                this.quickLoadButton.enabled = true;
                this.quickLoadLock = false;
            }
            // request the boosters
            this.requestBoosters();
        }
        
        // check if the boosters need to be refreshed
        if(this.isInChatGui && System.currentTimeMillis() > this.lastRequest + REFRESH_TIMEOUT) {
            // refresh them
            this.requestBoosters();
        }
        
        // if the user left the GuiChat
        if(!(mc.currentScreen instanceof GuiChat)) {
            // save the state
            this.isInChatGui = false;
            // hide the button
            this.quickLoadButton.visible = false;
            this.quickLoadButton.enabled = false;
        }
    }

    /**
     * Injects the Quick Load button into the button list
     * @param event provides the buttonList
     */
    public void onInitGui(InitGuiEvent event) {
        // only if we are in the chat
        if(event.gui instanceof GuiChat) {
            // inject it
            event.buttonList.add(this.quickLoadButton);
        }
    }

    /**
     * Listen to the button presses
     * @param event contains information about which button was press
     */
    public void onGuiActionPerformed(ActionPerformedEvent event) {
        if(event.gui instanceof GuiChat && event.button.id == this.quickLoadButton.id && !this.quickLoadLock) {
            // Only let the button fire once. Then you have to reopen the chat gui.
            this.quickLoadLock = true;
            this.quickLoadButton.enabled = false;
            // Run /booster queue
            FMLClientHandler.instance().getClient().thePlayer.sendChatMessage("/booster queue");
        }
    }
    
    /**
     * A helper method to draw the semi-transparent background and to set the button accordingly.
     */
    public void render(int rectX1, int rectY1, int rectX2, int rectY2, int buttonX, int buttonY, int buttonWidth) {
        if(HudPixelConfig.useAPI && HudPixelConfig.displayNetworkBoosters) {
            // Draw the semi-transparent background
            Gui.drawRect(rectX1, rectY1, rectX2, rectY2, 1879048192);
        }
        if(HudPixelConfig.displayQuickLoadButton) {
            // move the quick load button
            this.quickLoadButton.xPosition = buttonX;
            this.quickLoadButton.yPosition = buttonY;
            this.quickLoadButton.width = buttonWidth;
        }
    }

    /**
     * Requests the Network Boosters from the API. Checks config and cooldown.
     */
    private void requestBoosters() {
        // check the config settings
        if(HudPixelConfig.useAPI && HudPixelConfig.displayNetworkBoosters) {
            // check if enough time has past
            if(System.currentTimeMillis() > lastRequest + REQUEST_COOLDOWN) {
                // save the time of the request
                lastRequest = System.currentTimeMillis();
                // tell the queue that we need boosters
                Queue.getInstance().getBoosters(instance);
                // show the user that we are loading date
                this.isLoading = true;
                // make the loading text be displayed
                this.updateRenderStrings(false);
            }
        }
    }

    /**
     * Get the renderingStrings. They aren't updated with in this method.
     */
    public ArrayList<String> getRenderingStrings() {
        return renderingStrings;
    }

    @Override
    /**
     * Handles when the Public API answers a booster request.
     */
    public void onBoosterResponse(ArrayList<Booster> boosters) {
        // we aren't loading anymore
        this.isLoading = false;

        if(boosters != null) {
            // we've got a list, so this wasn't a fail
            this.hasFailed = false;

            ArrayList<Booster> newActiveBoosters = new ArrayList<Booster>();

            // get the active ones
            for (Booster booster : boosters) {
                // is there less than the full duration remaining
                if(booster.getRemainingTime() < booster.getTotalLength()) {
                    // it's active
                    newActiveBoosters.add(booster);
                    // load the name of the owner if only a uuid is available
                    booster.getOwner();
                }
            }
            
            // update the activeBoosters
            this.activeBoosters = newActiveBoosters;
        } else {
            // nothing was returned, we failed
            this.hasFailed = true;
        }
        // make it display (only match unprocessed tips if the request was successful
        this.updateRenderStrings(!this.hasFailed);
    }
    
    /**
     * Handles a list of boosters gathered by the /booster queue command.
     */
    public void onBoosterQueueCommandParsed(ArrayList<Booster> boosters) {
        // copy the activeBoosters Array so it can't be modified during this process
        ArrayList<Booster> activeBoostersCache = new ArrayList<Booster>(this.activeBoosters);
        // create a new list with the update boosters
        ArrayList<Booster> updatedActiveBooster = new ArrayList<Booster>();

        // go through all new boosters
        for(Booster newBooster : boosters) {
            // save if an old booster in the same game was found 
            boolean found = false;
            
            // look for an existing booster of the same game type
            for(Booster oldBooster : activeBoostersCache) {
                // if the old booster and the new booster are in the same game
                if(newBooster.getGameID() == oldBooster.getGameID()) {
                    found = true;
                    // check if they represent the same booster
                    if(newBooster.getOwner().equals(oldBooster.getOwner())) {
                        // in this case add the old booster which may contain more information because it may be gotten from the API
                        updatedActiveBooster.add(oldBooster);
                    } else {
                        // in this case the booster is new and need to be added
                        updatedActiveBooster.add(newBooster);
                    }
                    break;
                }
            }
            
            // if the booster just started and there was no previous one
            if(!found) {
                updatedActiveBooster.add(newBooster);
            }
        }
        // update the activeBoosters
        this.activeBoosters = updatedActiveBooster;
        // make it display
        this.updateRenderStrings(true);
    }

}
