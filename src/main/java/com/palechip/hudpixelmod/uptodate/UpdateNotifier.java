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
package com.palechip.hudpixelmod.uptodate;

import java.util.ArrayList;

import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.client.FMLClientHandler;

import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.HudPixelProperties;
import com.palechip.hudpixelmod.detectors.HypixelNetworkDetector;
import com.palechip.hudpixelmod.util.ChatMessageComposer;

/**
 * Checks if there is an update and sends a notification if there is one.
 * @author palechip
 *
 */
public class UpdateNotifier {
    private boolean hasUpdate;
    private boolean wasNotificationSent = false;
    private UpdateInformation updateInformation;

    public UpdateNotifier(UpdateInformation updateInformation) {
        this.updateInformation = updateInformation;
        this.hasUpdate = this.isVersionGreater(updateInformation.getLatestVersion());
    }

    /**
     * Send the notification message.
     */
    public void onTick() {
        // check if an update message needs to be sent
        if(this.hasUpdate && !this.wasNotificationSent) {
            // check if MC is ready to sent a chat message
            if(HypixelNetworkDetector.isHypixelNetwork && FMLClientHandler.instance().getClientPlayerEntity() != null) {
                // mark the message as sent
                this.wasNotificationSent = true;
                // send the message
                new ChatMessageComposer("Update available: " , EnumChatFormatting.DARK_PURPLE).appendMessage(new ChatMessageComposer(this.updateInformation.getLatestVersion(), EnumChatFormatting.GREEN)).send();
                new ChatMessageComposer(this.updateInformation.getUpdateReason() , EnumChatFormatting.GOLD).addFormatting(EnumChatFormatting.ITALIC).send();
                new ChatMessageComposer("Download here: ", EnumChatFormatting.DARK_PURPLE).appendMessage(new ChatMessageComposer(this.updateInformation.getUpdateLink(), EnumChatFormatting.YELLOW).makeLink(this.updateInformation.getUpdateLink())).send();
                // also update rendering vars to make sure it notices the update
                HudPixelMod.instance().renderer.loadRenderingProperties(this);
                
            }
        }
    }

    /**
     * Compares the mods version with the given version. 
     * @return True if the given version is greater and an update is available.
     */
    public boolean isVersionGreater(String version) {
        // check if the versions are equal. In this case we don't need to compare anything
        if(HudPixelProperties.VERSION.equals(version)) {
            return false;
        }

        try {
            // convert the mod version into an integer array
            ArrayList<Integer> modVersion = new ArrayList<Integer>();
            for(String s : HudPixelProperties.VERSION.split("[.]")) {
                modVersion.add(Integer.valueOf(s));
            }
            // convert the latest version into an integer array
            ArrayList<Integer> latestVersion = new ArrayList<Integer>();
            for(String s : version.split("[.]")) {
                latestVersion.add(Integer.valueOf(s));
            }
            // compare the versions step by step
            for(int i = 0; i < modVersion.size(); i++ ) {
                // the mod version is smaller than the latest version. There is an update!
                if(modVersion.get(i) < latestVersion.get(i)) {
                    return true;
                }
                // the mod version happens to be bigger than the latest version. This means there can't be an update.
                if(modVersion.get(i) > latestVersion.get(i)) {
                    return false;
                }
            }
        } catch (Exception ignored) {
            // casting error which is ignored
        }
        // comparation failed, return false
        return false;
    }
    
    /**
     * Is there an update for HudPixel?
     */
    public boolean hasUpdate() {
        return this.hasUpdate;
    }
    
    /**
     * Get the UpdateInformation behind this object.
     */
    public UpdateInformation getUpdateInformation() {
        return this.updateInformation;
    }
}
