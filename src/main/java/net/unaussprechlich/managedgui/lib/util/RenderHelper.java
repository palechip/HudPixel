/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */
package net.unaussprechlich.managedgui.lib.util;

import net.minecraft.util.ResourceLocation;
import net.unaussprechlich.managedgui.lib.elements.Container;

public class RenderHelper {

    public static void renderContainer(Container container) {

        renderContainerBackground(
                container.getXStartPadding(),
                container.getYStartPadding(),
                container.getBackgroundRGBA(),
                container.getWidthPadding(),
                container.getHeightPadding(),
                container.getBackgroundImage()
        );

        RenderUtils.renderBorder(
                container.getXStartBorder(),
                container.getYStartBorder(),
                container.getWidthBorder(),
                container.getHeightBorder(),
                container.getBorder(),
                container.getBorderRGBA()
        );
    }

    private static void renderContainerBackground(int xStart, int yStart, ColorRGBA color, int width, int height, ResourceLocation image) {
        if (image == null) RenderUtils.renderBoxWithColor(xStart, yStart, width, height, color);
        else RenderUtils.drawModalRectWithCustomSizedTexture(xStart, yStart, width, height, image, color.getALPHAf());
    }

}
