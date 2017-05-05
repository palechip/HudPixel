/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.util

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.unaussprechlich.managedgui.lib.helper.CustomFontRenderer


/**
 * FontUtil Created by unaussprechlich on 20.12.2016.
 * Description:
 */
object FontUtil {

    val fontRenderer: CustomFontRenderer
        get() = CustomFontRenderer.getINSTANCE()


    fun getStringWidth(s: String): Int {
        return fontRenderer.getStringWidth(s)
    }

    fun getCharWidth(c: Char): Int {
        return getStringWidth(c + "")
    }

    fun drawWithColor(text: String, xStart: Int, yStart: Int, color : ColorRGBA){
        fontRenderer.reset()
        fontRenderer.renderString(text, xStart, yStart, color)
    }

    fun draw(text: String, xStart: Int, yStart: Int) {
        fontRenderer.drawFormatted(text, xStart, yStart)
    }

    fun drawWithShadow(text: String, xStart: Int, yStart: Int) {
        fontRenderer.drawStringWithShadow(text, xStart.toFloat(), yStart.toFloat(), 0xffffff)
    }

    val frontRendererMC: FontRenderer
        get() = Minecraft.getMinecraft().fontRendererObj

    fun getStringWidthMC(s: String): Int {
        return frontRendererMC.getStringWidth(s)
    }

    fun getCharWidthMC(c: Char): Int {
        return getStringWidthMC(c + "")
    }

    fun drawMC(text: String, xStart: Int, yStart: Int) {
        frontRendererMC.drawString(text, xStart, yStart, 0xffffff)
    }

    fun drawWithShadowMC(text: String, xStart: Int, yStart: Int) {
        frontRendererMC.drawStringWithShadow(text, xStart.toFloat(), yStart.toFloat(), 0xffffff)
    }

}
