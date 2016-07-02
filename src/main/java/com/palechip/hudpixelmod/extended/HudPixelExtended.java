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

package com.palechip.hudpixelmod.extended;

import com.palechip.hudpixelmod.extended.boosterdisplay.BoosterManager;
import com.palechip.hudpixelmod.extended.footballdisplay.FootballDisplay;
import com.palechip.hudpixelmod.extended.onlinefriends.OnlineFriendManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

import java.util.UUID;

public class HudPixelExtended {

    private static HudPixelExtended hudPixelExtendedInstance = null;
    private static HudPixelExtendedEventHandler hudPixelExtendedEventHandler = new HudPixelExtendedEventHandler();
    public static OnlineFriendManager onlineFriendsManager = new OnlineFriendManager();
    public static UUID UUID;
    public static BoosterManager boosterManager;
    public static FootballDisplay footballDisplay = null;

    private HudPixelExtended(){}

    public static HudPixelExtended getInstance(){
        if(hudPixelExtendedInstance != null){
            return hudPixelExtendedInstance;
        } else {
            hudPixelExtendedInstance = new HudPixelExtended();
            return hudPixelExtendedInstance;
        }
    }

    public void setup(){

        UUID = Minecraft.getMinecraft().getSession().getProfile().getId();
        boosterManager = new BoosterManager();

        MinecraftForge.EVENT_BUS.register(hudPixelExtendedEventHandler);

    }
}
