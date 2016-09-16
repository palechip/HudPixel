/*******************************************************************************
 * HudPixel Reloaded (github.com/palechip/HudPixel), an unofficial Minecraft Mod for the Hypixel Network
 * <p>
 * Copyright (c) 2014-2015 palechip (twitter.com/palechip) and contributors
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
package com.palechip.hudpixelmod;

import com.palechip.hudpixelmod.util.GameType;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

import java.util.List;

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
        // only when the event starts
        if (event.phase == Phase.START && HudPixelMod.instance().gameDetector.getCurrentGame().getConfiguration().getModID() == GameType.WARLORDS.getModID()) {
            try {
                Minecraft mc = Minecraft.getMinecraft();
                // isHypixelNetwork if the world exists
                if (mc.theWorld == null) {
                    return;
                }

                // cycle through the list of entities
                List loadedEntities = mc.theWorld.getLoadedEntityList();
                for (int i = 0; i < loadedEntities.size(); i++) {
                    Entity e = (Entity) loadedEntities.get(i);
                    // the crash report shows that the entity is EntityItem
                    if (e != null && e instanceof EntityItem) {
                        EntityItem item = (EntityItem) e;
                        // item.getEntityItem().getMetaData() causes the NullPointerException
                        // this means getEntityItem() must return null
                        try {
                            item.getEntityItem().getMetadata();
                        } catch (Exception ex) {
                            // we found the corrupt item
                            HudPixelMod.instance().logInfo("Found corrupt Item which causes a crash. Will be removed! You won't crash <3!");
                            // kill the entity
                            e.setDead();
                            // get some data needed to remove the entity instantly
                            int x = e.chunkCoordX;
                            int z = e.chunkCoordZ;
                            World world = mc.theWorld;

                            // remove it from the chunk if it was there. (Looks a little bit different than in World.updateEntities() because the original method wasn't visible)
                            if (e.addedToChunk && world.getChunkProvider().chunkExists(x, z)) {
                                world.getChunkFromChunkCoords(x, z).removeEntity(e);
                            }

                            // remove it from the list
                            world.loadedEntityList.remove(i--);
                            // I don't know what this does but it seems to be necessary to keep track of some values ...
                            world.onEntityRemoved(e);

                            // the server may try to add it back in regular intervals. Then the same process will happen again.
                        }
                    }
                }

            } catch (Exception e) {
                HudPixelMod.instance().logDebug("CTF CRASH PREVENTION EXCEPTION!");
                e.printStackTrace();
            }
        }
    }

}
