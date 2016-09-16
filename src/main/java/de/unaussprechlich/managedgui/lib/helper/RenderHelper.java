package de.unaussprechlich.managedgui.lib.helper;

import de.unaussprechlich.managedgui.lib.elements.Container;
import de.unaussprechlich.managedgui.lib.util.ColorRGBA;
import de.unaussprechlich.managedgui.lib.util.RenderUtils;
import de.unaussprechlich.managedgui.lib.util.storage.StorageFourSide;
import de.unaussprechlich.managedgui.lib.util.storage.StorageTwoSame;
import net.minecraft.util.ResourceLocation;

/******************************************************************************

 * Copyright (c) 2016 unaussprechlich

 *******************************************************************************/
public class RenderHelper {

    public static void renderContainer(Container container) {
        StorageTwoSame<Short> innerSize = processInnerSize(container.width, container.height, container.padding);
        renderContainerBackground((short) (container.xStart + container.margin.LEFT + container.border.LEFT), (short) (container.yStart + container.margin.TOP + container.border.TOP), container.backgroundRGBA, innerSize.t0, innerSize.t1, container.backgroundImage);
        RenderUtils.renderBorder((short) (container.xStart + container.margin.LEFT), (short) (container.yStart + container.margin.TOP), innerSize.t0, innerSize.t1, container.border, container.borderRGBA);
    }

    private static void renderContainerBackground(short xStart, short yStart, ColorRGBA color, short width, short height, ResourceLocation image) {
        if (image == null) RenderUtils.renderBoxWithColor(xStart, yStart, width, height, color);
        else RenderUtils.drawModalRectWithCustomSizedTexture(xStart, yStart, width, height, image, color.ALPHA);
    }

    private static StorageTwoSame<Short> processInnerSize(short width, short height, StorageFourSide padding) {
        return new StorageTwoSame<Short>((short) (width + padding.LEFT + padding.RIGHT), (short) (height + padding.BOTTOM + padding.TOP));
    }
}
