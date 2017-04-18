/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.templates.defaults.container

import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.unaussprechlich.managedgui.lib.container.Container
import net.unaussprechlich.managedgui.lib.event.util.Event
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.util.EnumEventState
import net.unaussprechlich.managedgui.lib.util.RGBA
import net.unaussprechlich.managedgui.lib.util.RenderUtils

/**
 * DefPictureContainer Created by Alexander on 27.02.2017.
 * Description:
 */
class DefPictureContainer : Container {

    constructor(width: Int, height: Int, resourceLocation: ResourceLocation) {
        backgroundImage = resourceLocation
        setWidth(width)
        setHeight(height)
        backgroundRGBA = RGBA.NULL.get()
    }

    constructor(resourceLocation: ResourceLocation) {
        backgroundImage = resourceLocation
    }

    constructor()

    override fun doClientTickLocal(): Boolean {
        return true
    }

    override fun doRenderTickLocal(xStart: Int, yStart: Int, width: Int, height: Int, ees: EnumEventState): Boolean {
        if (ees == EnumEventState.POST) {
            if (backgroundImage == null) {
                RenderUtils.renderBoxWithColor(xStart , yStart, width, height, RGBA.P1B5_0B271F.get())
                RenderUtils.iconRender_loadingBar(xStart + width / 2 - 5, yStart + height / 2 - 3 + 2)
            } else {
                //RenderUtils.texture_modularRect(xStart , yStart, 0, 0,  getWidth(), getHeight(), getWidth(), getHeight(), backgroundImage, 1f)
            }
        }
        return true
    }

    override fun doChatMessageLocal(e: ClientChatReceivedEvent): Boolean {
        return true
    }

    override fun doClickLocal(clickType: MouseHandler.ClickType, isThisContainer: Boolean): Boolean {
        return true
    }

    override fun doScrollLocal(i: Int, isThisContainer: Boolean): Boolean {
        return true
    }

    override fun doMouseMoveLocal(mX: Int, mY: Int): Boolean {
        return false
    }

    override fun <T : Event<*>> doEventBusLocal(iEvent: T): Boolean {
        return true
    }

    override fun doOpenGUILocal(e: GuiOpenEvent): Boolean {
        return true
    }

    override fun doResizeLocal(width: Int, height: Int): Boolean {
        return true
    }
}
