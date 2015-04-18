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
package com.palechip.hudpixelmod;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.relauncher.FMLRelaunchLog;

/**
 * A creative solution to the Warlords CTF crash. Removes the corrupt item using a trick.
 * @author palechip
 *
 */
public class WarlordsCTFCrashPrevention {

    public WarlordsCTFCrashPrevention() {
        FMLCommonHandler.instance().bus().register(this);
    }


    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderTick(RenderTickEvent event) {
        if(event.phase == Phase.START) {
            try {
                Minecraft mc = Minecraft.getMinecraft();
                // check if the world exists
                if(mc.theWorld == null) {
                    return;
                }

                // cycle through the list of entities
                List loadedEntities = mc.theWorld.getLoadedEntityList();
                for(int i = 0; i < loadedEntities.size(); i++) {
                    Entity e = (Entity) loadedEntities.get(i);
                    // the crash report shows that the entity is EntityItem
                    if(e != null && e instanceof EntityItem) {
                        EntityItem item = (EntityItem) e;
                        // item.getEntityItem().getMetaData() causes the NullPointerException
                        // this means getEntityItem() must return null
                        try {
                            item.getEntityItem().getMetadata();
                        } catch(Exception ex) {
                            // we found the corrupt item
                            HudPixelMod.instance().logInfo("Found corrupt Item which causes a crash. Will be removed! You won't crash <3!");
                            // the trick is to unload the Item
                            ArrayList<Entity> toUnload = new ArrayList<Entity>();
                            toUnload.add(e);
                            mc.theWorld.unloadEntities(toUnload);
                            // the server will try to add it back but FML will deny it with a message this
                            // [Client thread/WARN] [FML/]: Attempted to add a EntityItem to the world with a invalid item at (88.09,  40.13, 240.81), this is most likely a config issue between you and the server. Please double check your configs
                            // I would really like to suppress the log spam but I've found no way to do it. :(
                        }
                    }
                }

            } catch(Exception e) {
                HudPixelMod.instance().logDebug("CTF CRASH PREVENTION EXCEPTION!");
                e.printStackTrace();
            }
        }
    }

}
