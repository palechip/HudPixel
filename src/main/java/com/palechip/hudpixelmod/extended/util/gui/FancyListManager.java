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
package com.palechip.hudpixelmod.extended.util.gui;

import com.palechip.hudpixelmod.extended.HudPixelExtendedEventHandler;
import com.palechip.hudpixelmod.extended.util.IEvent;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

public abstract class FancyListManager implements IEvent{

    private int indexScroll = 0;
    private int shownObjects;

    protected ArrayList<FancyListObject> fancyListObjects = new ArrayList<FancyListObject>();

    public int size(){
        return fancyListObjects.size();
    }

    public FancyListManager(int shownObjects){
        this.shownObjects = shownObjects;
        HudPixelExtendedEventHandler.registerIEvent(this);
    }

    /**
     * this really renders nothing it just sets rge right offset for each element
     */
    protected void renderDisplay(){
        float xStart = 2;
        float yStart = 2;

        if(indexScroll > 0)
            fancyListObjects.get(indexScroll -1).onRenderTick(true,xStart, yStart);
        yStart += 13;

        for(int i = indexScroll; i <= indexScroll + shownObjects -1; i++) {
            fancyListObjects.get(i).onRenderTick(false, xStart, yStart);
            yStart += 25;
        }

        if(indexScroll + shownObjects <= size() -1)
            fancyListObjects.get(indexScroll + shownObjects).onRenderTick(true,xStart, yStart);
    }

    /**
     * handels the scrollinput and processes the right index for the list
     * @param i scroll input
     */
    @Override
    public void handleScrollInput(int i){
        if(Minecraft.getMinecraft().displayHeight - Mouse.getY() > (26 * shownObjects + 28) * 2) return;
        if(Mouse.getX() > 280) return;

        if (i != 0) {
            if (i < 0) {
                if(indexScroll >= (size() - shownObjects)){
                    indexScroll = size() - shownObjects;
                } else {
                    indexScroll++;
                }
            } else if (i > 0) {
                if(indexScroll - 1 <= 0){
                    indexScroll = 0;
                } else {
                    indexScroll--;
                }
            }
        }
    }

}
