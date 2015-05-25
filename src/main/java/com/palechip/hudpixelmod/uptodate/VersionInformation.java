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

/**
 * Used to parse the json structure in v*.*.*.*.json
 * @author palechip
 *
 */
public class VersionInformation {
    private String version;
    private int gameVersion;
    private boolean deactivated;
    private String deactivationMessage;
    private String deactivationLink;
    
    /**
     * Get the version which these information belong to.
     * @return A String representation of the version. The parts are separated by points.
     */
    public String getVersion() {
        if(this.version != null && !this.version.isEmpty()) {
            return this.version;
        } else {
            return HudPixelMod.VERSION;
        }
    }
    
    /**
     * Get the version of the latest games.json file
     * @return the version of the games.json file or -1
     */
    public int getGameVersion() {
        if(this.gameVersion > 0) {
            return this.gameVersion;
        } else {
            return -1;
        }
    }
    
    /**
     * Is this version deactivated because of a severe bug or banned features?
     */
    public boolean isDeactivated() {
        return this.deactivated;
    }
    
    /**
     * Get the reason for the deactivation.
     * @return A reason or "No Reason Provided!"
     */
    public String getDeactivationMessage() {
        if(this.deactivationMessage != null && !this.deactivationMessage.isEmpty()) {
            return this.deactivationMessage;
        } else {
            return "No Reason Provided!";
        }
    }
    
    /**
     * Get a link with further information about the deactivation.
     * @return A link or "No Link Provided!"
     */
    public String getDeactivationLink() {
        if(this.deactivationLink != null && (this.deactivationLink.startsWith("http://") || this.deactivationLink.startsWith("https://"))) {
            return this.deactivationLink;
        } else {
            return "No Link Provided!";
        }
    }
}
