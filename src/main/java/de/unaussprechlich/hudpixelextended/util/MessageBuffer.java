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
package de.unaussprechlich.hudpixelextended.util;


import de.unaussprechlich.hudpixelextended.fancychat.FancyChat;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.util.ArrayList;
import java.util.List;

public class MessageBuffer {

    private int maxBufferSize;
    public ArrayList<String> messageBuffer = new ArrayList<String>();

    public MessageBuffer(int maxSize) {
        this.maxBufferSize = maxSize;
    }


    public void addMinecraftEntry(String message) {
        FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRendererObj;
        List messageList = fontRenderer.listFormattedStringToWidth(message, FancyChat.FIELD_WIDTH);

        for (Object o : messageList) {
            messageBuffer.add(0, (String) o);
            checkSize();
        }
    }

    public List<String> getLast(int size) {
        return getSubArray(0, size);
    }

    private void checkSize() {
        if (messageBuffer.size() >= maxBufferSize) {
            for (int i = messageBuffer.size(); i >= maxBufferSize; i--) {
                messageBuffer.remove(i - 1);
            }
        }
    }

    public List<String> getSubArray(int startIndex, int endIndex) {
        return messageBuffer.subList(startIndex, endIndex);
    }
}
