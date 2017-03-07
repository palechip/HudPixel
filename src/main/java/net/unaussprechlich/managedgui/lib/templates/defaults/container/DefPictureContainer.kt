/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.templates.defaults.container

import net.minecraft.client.renderer.GlStateManager
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

    private val resourceLocation: ResourceLocation?
    private var textureHeight = 0
    private var textuewWidth = 0


    constructor(width: Int, height: Int, resourceLocation: ResourceLocation) {
        this.resourceLocation = resourceLocation
        setWidth(width)
        setHeight(height)
    }

    constructor(resourceLocation: ResourceLocation) {
        this.resourceLocation = resourceLocation
    }

    constructor() {
        this.resourceLocation = null

    }

    override fun doClientTickLocal(): Boolean {
        return true
    }

    override fun doRenderTickLocal(xStart: Int, yStart: Int, width: Int, height: Int, ees: EnumEventState): Boolean {
        if (ees == EnumEventState.POST) {
            if (resourceLocation == null) {
                RenderUtils.renderBoxWithColor(xStart, yStart, width, height, RGBA.RED.get())
                RenderUtils.iconRender_loadingBar(xStart + width / 2 - 5, yStart + height / 2 - 3)
            } else {
                GlStateManager.enableBlend()
                RenderUtils.texture_modularRect(xStart , yStart, getWidth(), getHeight(), 0, 0, getWidth(), getHeight(), resourceLocation, 1f)
                GlStateManager.disableBlend()
                //RenderUtils.texture_modularRect(xStart, yStart, getWidth(), getHeight(), resourceLocation, 1f);
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
