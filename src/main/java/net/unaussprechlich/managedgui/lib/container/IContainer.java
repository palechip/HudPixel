/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.container;

import net.minecraft.util.ResourceLocation;
import net.unaussprechlich.managedgui.lib.util.ColorRGBA;
import net.unaussprechlich.managedgui.lib.util.storage.ContainerSide;

/**
 * IContainer Created by unaussprechlich on 20.12.2016.
 * Description:
 **/
public interface IContainer {

    int getXOffset();
    int getYOffset();

    int getYStart();
    int getXStart();
    int getYStartMargin();
    int getXStartMargin();
    int getYStartPadding();
    int getXStartPadding();
    int getYStartBorder();
    int getXStartBorder();

    int getHeight();
    int getWidth();
    int getWidthMargin();
    int getHeightMargin();
    int getWidthBorder();
    int getHeightBorder();
    int getWidthPadding();
    int getHeightPadding();

    boolean isVisible();
    boolean isHover();

    ColorRGBA getBorderRGBA();
    ColorRGBA getBackgroundRGBA();

    ResourceLocation getBackgroundImage();

    ContainerSide getPadding();
    ContainerSide getMargin();
    ContainerSide getBorder();

    void setVisible(boolean visible);
    void setHeight(int height);
    void setWidth(int width);
    void setYOffset(int yOffset);
    void setXOffset(int xOffset);
    void setXYOffset(int xOffset, int yOffset);
    void setBorderRGBA(ColorRGBA borderRGBA);
    void setBackgroundRGBA(ColorRGBA backgroundRGBA);
    void setBackgroundImage(ResourceLocation backgroundImage);
    void setPadding(int value);
    void setMargin(int value);
    void setBorder(int value);
    void setPadding(ContainerSide padding);
    void setMargin(ContainerSide margin);
    void setBorder(ContainerSide border);


}
