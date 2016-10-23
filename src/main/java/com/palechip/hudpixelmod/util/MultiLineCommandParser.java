/***********************************************************************************************************************
 HudPixelReloaded - License

 The repository contains parts of Minecraft Forge and its dependencies. These parts have their licenses
 under forge-docs/. These parts can be downloaded at files.minecraftforge.net.This project contains a
 unofficial copy of pictures from the official Hypixel website. All copyright is held by the creator!
 Parts of the code are based upon the Hypixel Public API. These parts are all in src/main/java/net/hypixel/api and
 subdirectories and have a special copyright header. Unfortunately they are missing a license but they are obviously
 intended for usage in this kind of application. By default, all rights are reserved.
 The original version of the HudPixel Mod is made by palechip and published under the MIT license.
 The majority of code left from palechip's creations is the component implementation.The ported version to
 Minecraft 1.8.9 and up HudPixel Reloaded is made by PixelModders/Eladkay and also published under the MIT license
 (to be changed to the new license as detailed below in the next minor update).

 For the rest of the code and for the build the following license applies:

 # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
 #  HudPixel by PixelModders, Eladkay & unaussprechlich is licensed under a Creative Commons         #
 #  Attribution-NonCommercial-ShareAlike 4.0 International License with the following restrictions.  #
 #  Based on a work at HudPixelExtended & HudPixel.                                                  #
 # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #

 Restrictions:

 The authors are allowed to change the license at their desire. This license is void for members of PixelModders and
 to unaussprechlich, except for clause 3. The licensor cannot revoke these freedoms in most cases, as long as you follow
 the following license terms and the license terms given by the listed above Creative Commons License, however in extreme
 cases the authors reserve the right to revoke all rights for usage of the codebase.

 1. PixelModders, Eladkay & unaussprechlich are the authors of this licensed material. GitHub contributors are NOT
 considered authors, neither are members of the HudHelper program. GitHub contributers still hold the rights for their
 code, but only when it is used separately from HudPixel and any license header must indicate that.
 2. You shall not claim ownership over this project and repost it in any case, without written permission from at least
 two of the authors.
 3. You shall not make money with the provided material. This project is 100% non commercial and will always stay that
 way. This clause is the only one remaining, should the rest of the license be revoked. The only exception to this
 clause is completely cosmetic features. Only the authors may sell cosmetic features for the mod.
 4. Every single contibutor owns copyright over his contributed code when separated from HudPixel. When it's part of
 HudPixel, it is only governed by this license, and any copyright header must indicate that. After the contributed
 code is merged to the release branch you cannot revoke the given freedoms by this license.
 5. If your own project contains a part of the licensed material you have to give the authors full access to all project
 related files.
 6. You shall not act against the will of the authors regarding anything related to the mod or its codebase. The authors
 reserve the right to take down any infringing project.
 **********************************************************************************************************************/
package com.palechip.hudpixelmod.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides basic functionality in order to parse complex command outputs like /booster queue and /friend list
 * @author palechip
 */
public abstract class MultiLineCommandParser {
    protected final String startMessage;
    protected final Pattern messagePattern;
    private boolean isMessageOngoing = false;

    /**
     * Creates a new MultiLineCommandParser.
     * @param startMessage The message which starts the command output. i.e. the command title.
     * @param messagePattern A regex which matches all messages which belong to the command.
     */
    public MultiLineCommandParser(String startMessage, String messagePattern) {
        this.startMessage = startMessage;
        this.messagePattern = Pattern.compile(messagePattern);
    }

    /**
     * Has to be called with every chat message.
     * @param message The chat message without formatting.
     */
    public void onChat(String message) {
        // if there is no command output in progress
        if (!this.isMessageOngoing) {
            // isHypixelNetwork if the message contains the start message
            if (message.contains(this.startMessage)) {
                // save that the command has started
                this.isMessageOngoing = true;
                // start event
                this.onCommandStart();
            }
        } else {
            // isHypixelNetwork if the message matches our messagePattern
            Matcher matcher = this.messagePattern.matcher(message);
            if (matcher.matches()) {
                // call onCommandReceived
                this.onCommandReceived(message);
            } else if (!message.isEmpty()) {
                // seems like the command output is over, stop trying to match the messages
                this.isMessageOngoing = false;
                // stop event
                this.onCommandEnd();
            }
        }
    }

    /**
     * Gets called when a chat message arrives which belongs to the the listened command.
     * @param commandMessage The message which matches the messagePattern.
     */
    protected abstract void onCommandReceived(String commandMessage);

    /**
     * Gets called when the command starts and before the first message is received.
     * Can be used to create collection arrays.
     */
    protected abstract void onCommandStart();

    /**
     * Gets called when the command is finished.
     * Can be used to process collected information.
     */
    protected abstract void onCommandEnd();
}
