/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.templates.defaults.container

import net.unaussprechlich.managedgui.lib.container.Container
import net.unaussprechlich.managedgui.lib.handler.MouseHandler
import net.unaussprechlich.managedgui.lib.util.FontUtil
import net.unaussprechlich.managedgui.lib.util.RGBA
import net.unaussprechlich.managedgui.lib.util.RenderUtils


open class DefButtonStandardContainer(var text : String,
                              clickedListener: (MouseHandler.ClickType, Container)  -> Unit
) : DefButtonContainer(FontUtil.getStringWidth(text) + 20, 17 , clickedListener, { xStart, yStart, width, height ->
    RenderUtils.drawBorderBox(xStart, yStart , width, height, RGBA.P1B1_596068.get(), RGBA.P1B1_DEF.get())
    FontUtil.drawWithColor(text, xStart + 10, yStart + 4, RGBA.P1B1_596068.get())
},{ xStart, yStart, width, height ->
    RenderUtils.drawBorderBox(xStart, yStart , width, height, RGBA.WHITE.get(), RGBA.P1B1_DEF.get())
    FontUtil.drawWithColor(text, xStart + 10, yStart + 4, RGBA.WHITE.get())
})