package com.palechip.hudpixelmod.extended.statsviewer;

import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.util.GameType;
import com.palechip.hudpixelmod.extended.HudPixelExtended;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraftforge.client.event.RenderPlayerEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
public class StatsViewerManager {

    private static HashMap<String, StatsViewerRender> statsViewerRenderMap = new HashMap<String, StatsViewerRender>();

    /**
     * Renders the stats above the player
     * @param event RenderPlayerEvent
     */
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {

        //returns if when the rendered player is the user
        if(event.entityPlayer.getUniqueID().equals(HudPixelExtended.UUID)) return;

        //renders every entry in the map
        if(statsViewerRenderMap.containsKey(event.entityPlayer.getName()))
            statsViewerRenderMap.get(event.entityPlayer.getName()).onRenderPlayer(event);
    }

    /**
     * Function to create and delete the current shown stats
     */
    public static void onClientTick(){

        ArrayList<String> removeFromMap = new ArrayList<String>();

        //deletes the entry if the showduration has expired
        for(Map.Entry<String, StatsViewerRender> entry : statsViewerRenderMap.entrySet()){
            if(entry.getValue().getExpireTimestamp() <= System.currentTimeMillis())
                removeFromMap.add(entry.getKey());
        }

        for(String s : removeFromMap){
            statsViewerRenderMap.remove(s);
        }

        Minecraft mc = Minecraft.getMinecraft();

        //don't display if the player is not sneaking
        if(mc.thePlayer == null || !mc.thePlayer.isSneaking())return;

        //checks if there is a new stats viewer to add
        if(mc.objectMouseOver != null && mc.objectMouseOver.entityHit != null){
            if(mc.objectMouseOver.entityHit instanceof EntityOtherPlayerMP){
                if(!statsViewerRenderMap.containsKey(mc.objectMouseOver.entityHit.getName())){
                    statsViewerRenderMap.put(mc.objectMouseOver.entityHit.getName(), new StatsViewerRender(
                        GameType.getTypeByID(HudPixelMod.instance().gameDetector.getCurrentGame().getConfiguration().getModID()),
                        mc.objectMouseOver.entityHit.getName()));
                }
            }
        }
    }
}
