/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */
package net.unaussprechlich.managedgui.lib.util;

import net.minecraft.util.ResourceLocation;
import net.unaussprechlich.managedgui.lib.elements.ManagedContainer;
import net.unaussprechlich.managedgui.lib.util.storage.Matrix4Side;
import net.unaussprechlich.managedgui.lib.util.storage.MatrixC1R2;

public class RenderHelper {

    public static void renderContainer(ManagedContainer container) {
        MatrixC1R2<Integer> innerSize = processInnerSize(container.width, container.height, container.padding);
        renderContainerBackground(container.xStart + container.margin.LEFT + container.border.LEFT,container.yStart + container.margin.TOP + container.border.TOP, container.backgroundRGBA, innerSize.t0, innerSize.t1, container.backgroundImage);
        RenderUtils.renderBorder(container.xStart + container.margin.LEFT, container.yStart + container.margin.TOP, innerSize.t0, innerSize.t1, container.border, container.borderRGBA);
    }

    private static void renderContainerBackground(int xStart, int yStart, ColorRGBA color, int width, int height, ResourceLocation image) {
        if (image == null) RenderUtils.renderBoxWithColor(xStart, yStart, width, height, color);
        else RenderUtils.drawModalRectWithCustomSizedTexture(xStart, yStart, width, height, image, color.getALPHAf());
    }

    private static MatrixC1R2<Integer> processInnerSize(int width, int height, Matrix4Side padding) {
        return new MatrixC1R2<Integer>(width + padding.LEFT + padding.RIGHT, height + padding.BOTTOM + padding.TOP);
    }
}
