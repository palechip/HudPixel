/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.util

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.unaussprechlich.managedgui.lib.GuiManagerMG
import net.unaussprechlich.managedgui.lib.event.events.ScaleChangedEvent

object DisplayUtil {

    private var prevScale = 0

    fun onClientTick() {
        if (prevScale != mcScale) {
            prevScale = mcScale
            GuiManagerMG.postEvent(ScaleChangedEvent(mcScale))
        }
    }

    val mcScale: Int
        get() {
            val mc = Minecraft.getMinecraft()
            var scale = 1
            if (mc.gameSettings.guiScale == 0) {
                val res = ScaledResolution(mc)
                scale = res.scaleFactor
            } else {
                scale = mc.gameSettings.guiScale
            }
            return scale
        }

    val scaledMcWidth: Int
        get() = Minecraft.getMinecraft().displayWidth / mcScale

    val scaledMcHeight: Int
        get() = Minecraft.getMinecraft().displayHeight / mcScale
}
