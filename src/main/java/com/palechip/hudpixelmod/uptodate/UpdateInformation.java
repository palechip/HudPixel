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

import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.HudPixelProperties;

/**
 * Used to parse the json structure used in update.json
 * @author palechip
 *
 */
public class UpdateInformation {
    private String latest;
    private String updateLink;
    private String updateReason;

    /**
     * Get the latest version in this channel and Minecraft version.
     * @return A String representation of the version. The parts are separated by points.
     */
    public String getLatestVersion() {
        // check if there is a latest version
        if(this.latest != null && !this.latest.isEmpty()) {
            return this.latest;
        } else {
            // assume that the current version is the latest one
            return HudPixelProperties.VERSION;
        }
    }

    /**
     * Get the Link to download the Update.
     * @return A String representation of the link.
     */
    public String getUpdateLink() {
        // check if the value is valid
        if(this.updateLink != null && (this.updateLink.startsWith("http://") || this.updateLink.startsWith("https://"))) {
            return this.updateLink;
        } else {
            // return a default link
            return "https://github.com/palechip/HudPixel/releases";
        }
    }

    /**
     * Get the reason for the update.
     */
    public String getUpdateReason() {
        // check if there is a value
        if(this.updateReason != null && !this.updateReason.isEmpty()) {
            return this.updateReason;
        } else {
            // return a generic message
            return "Fixes and Features";
        }
    }
}
