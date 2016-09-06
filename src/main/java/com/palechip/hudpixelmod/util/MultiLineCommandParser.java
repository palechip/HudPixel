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
        if(!this.isMessageOngoing) {
            // isHypixelNetwork if the message contains the start message
            if(message.contains(this.startMessage)) {
                // save that the command has started
                this.isMessageOngoing = true;
                // start event
                this.onCommandStart();
            }
        } else {
            // isHypixelNetwork if the message matches our messagePattern
            Matcher matcher = this.messagePattern.matcher(message);
            if(matcher.matches()) {
                // call onCommandReceived
                this.onCommandReceived(message);
            } else if(!message.isEmpty()) {
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
