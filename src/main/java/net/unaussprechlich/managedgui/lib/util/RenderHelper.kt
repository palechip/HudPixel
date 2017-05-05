/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */
package net.unaussprechlich.managedgui.lib.util

import net.minecraft.util.ResourceLocation
import net.unaussprechlich.managedgui.lib.container.Container

object RenderHelper {

    fun renderContainer(container: Container) {

        renderContainerBackground(
                container.xStartPadding,
                container.yStartPadding,
                container.backgroundRGBA,
                container.widthPadding,
                container.heightPadding,
                container.backgroundImage
        )

        if(!container.border.isEmpty)
        RenderUtils.renderBorder(
                container.xStartBorder,
                container.yStartBorder,
                container.widthBorder,
                container.heightBorder,
                container.border,
                container.borderRGBA
        )
    }

    private fun renderContainerBackground(xStart: Int, yStart: Int, color: ColorRGBA, width: Int, height: Int, image: ResourceLocation?) {
        if (image == null) RenderUtils.renderBoxWithColor(xStart, yStart, width, height, color)
        else RenderUtils.texture_modularRect(xStart , yStart, width, height, image)
    }

}
