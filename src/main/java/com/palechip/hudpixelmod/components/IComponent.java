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
package com.palechip.hudpixelmod.components;

public interface IComponent {
    /**
     * This is called when the mod has detected that the player joined a game of this type.
     * It should reset the rendered strings to the default ones.
     */
    public void setupNewGame();
    
    /**
     * Called when the game starts.
     */
    public void onGameStart();
    
    /**
     * Called when the game ends.
     */
    public void onGameEnd();
    
    /**
     * If the game is running, it'll receive ticks to update the rendered strings.
     */
    public void onTickUpdate();
    
    /**
     * If the game is running, it'll receive the chat messages which the client receives.
     */
    public void onChatMessage(String textMessage, String formattedMessage);
    
    /**
     * Gets the string the component wants to render.
     * @return
     */
    public String getRenderingString();
}
