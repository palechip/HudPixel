package com.palechip.hudpixelmod.extended.fancychat;

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
class FancyChatFilter {

    //------------------------------------------------------------------------------------------------------------------
    /**
     * @TRIGGER: Every time the client receives a message STARTING with one
     * of the strings, given by this triggerlist, the fancy chat will be triggered.
     */
    static final String[] fancyChatTriggers = {
            "Guild > ",
            "Party > ",
            "[BLU][Lv",
            "[RED][Lv",
            "[SHOUT]",
            "[@]"
    };
    //------------------------------------------------------------------------------------------------------------------
    /**
     * @BLACKLIST: Every string in this blacklist will be filtered out if the
     * message received by the client STARTS with the blacklisted string.
     */
    static final String[] blacklistStart = {
            "You can put your mouse cursor over a weapon's name ",
            "DON'T LEAVE! You'll receive rewards at the end of the",
            "You are wounded.",
            "You are no longer wounded.",
            "This is still recharging!",
            "You didn't target a player!",
            "Your enemy is too far away!"
    };
    //------------------------------------------------------------------------------------------------------------------
    /**
     * @BLACKLIST: Every string in this blacklist will be filtered out if the
     * message received by the client CONTAINS with the blacklisted string.
     */
    static final String[] blacklistContains = {

    };
    //------------------------------------------------------------------------------------------------------------------
    /**
     * @BLACKLIST: Every string in this blacklist will be filtered out if the
     * message received by the client ENDS with the blacklisted string.
     */
    static final String[] blacklistEnds = {
            "'s Hunger is already full!",
            "'s Exercise is already full!",
            "'s Thirst is already full!"
    };

}
