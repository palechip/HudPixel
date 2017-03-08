/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.handler

import com.palechip.hudpixelmod.extended.HudPixelExtendedEventHandler
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.gui.GuiIngameMenu
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.gui.inventory.GuiInventory
import net.unaussprechlich.managedgui.lib.GuiManagerMG
import org.lwjgl.input.Mouse

object MouseHandler {

    var mcScale: Int = 0
        private set
    var mX: Int = 0
        private set
    var mY: Int = 0
        private set


    enum class ClickType {
        SINGLE, DOUBLE, DRAG, DROP
    }

    fun onClientTick() {
        val mc = Minecraft.getMinecraft()
        if (mc.gameSettings.guiScale == 0) {
            mcScale = ScaledResolution(mc).scaleFactor
        } else {
            mcScale = mc.gameSettings.guiScale
        }
        val newmX = Mouse.getX() / mcScale
        val newmY = (mc.displayHeight - Mouse.getY()) / mcScale

        if (newmX != mX || newmY != mY) {
            mX = newmX
            mY = newmY
            GuiManagerMG.onMouseMove(mX, mY)
        }

        if (!(mc.currentScreen is GuiIngameMenu || mc.currentScreen is GuiChat || mc.currentScreen is GuiInventory))
            return
        handleMouseClick()
    }

    private val clickDelay: Long = 150
    private var lastTimeClicked: Long = 0
    private var mouseButtonDown = false
    private var doubleClick = false

    fun handleMouseClick() {

        val isDown = Mouse.isButtonDown(0)

        if (!mouseButtonDown && isDown) {
            GuiManagerMG.onClick(ClickType.DRAG)
        }

        if (mouseButtonDown && !isDown) {
            mouseButtonDown = false
            doubleClick = false
            GuiManagerMG.onClick(ClickType.DROP)
        }

        if (System.currentTimeMillis() + clickDelay < lastTimeClicked) {
            if (!mouseButtonDown && isDown) {
                mouseButtonDown = true
                GuiManagerMG.onClick(ClickType.DOUBLE)
            }
        }

        if (!mouseButtonDown && isDown && !doubleClick) {
            mouseButtonDown = true
            GuiManagerMG.onClick(ClickType.SINGLE)
            lastTimeClicked = System.currentTimeMillis()
        }

        handleMouseScroll()


    }

    private fun handleMouseScroll() {
        Mouse.poll()
        val i = Mouse.getDWheel()

        if (i != 0) {
            GuiManagerMG.onScroll(i)

            //TODO: FIX THAT
            HudPixelExtendedEventHandler.handleMouseScroll(i)

        }
    }
}
