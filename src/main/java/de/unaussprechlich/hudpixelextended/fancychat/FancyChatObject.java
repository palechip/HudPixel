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
package de.unaussprechlich.hudpixelextended.fancychat;

import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.util.List;

class FancyChatObject {

    private long timestamp;
    private List message;
    private int size = 1;


    public int getSize() {return size;}
    long getTimestamp() {return timestamp;}
    public List getMessage() {return message;}

    FancyChatObject(String message){
        FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRendererObj;
        this.message = fontRenderer.listFormattedStringToWidth(message,FancyChat.FIELD_WIDTH);
        this.size = this.message.size();

        this.timestamp = System.currentTimeMillis();
    }


    int drawTextField(int xStart, int yStart, FontRenderer fontRenderer){
        for(Object s : message){
            fontRenderer.drawStringWithShadow((String) s, xStart , yStart, 0xffffff);
            yStart += FancyChat.RENDERING_HEIGHT_OFFSET;
        }
        return size;
    }
}
