/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.elements.tabs.containers;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.palechip.hudpixelmod.extended.util.ImageLoader;
import net.minecraft.client.renderer.GlStateManager;
import net.unaussprechlich.managedgui.lib.elements.defaults.container.DefBackgroundContainer;
import net.unaussprechlich.managedgui.lib.util.*;

import static net.unaussprechlich.managedgui.lib.util.RenderUtils.drawModalRectWithCustomSizedTexture;

/**
 * TabListElementContainer Created by Alexander on 24.02.2017.
 * Description:
 **/
public class TabListElementContainer extends DefBackgroundContainer {

    public static final int ELEMENT_HEIGHT = 17;

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    private boolean isOpen = false;
    private ColorRGBA color;
    private String title;

    public TabListElementContainer(String title, ColorRGBA color) {
        super(EnumRGBA.GREY_COOL.get(), FontHelper.widthOfString(title) + 10, ELEMENT_HEIGHT);
        this.color = color;
        this.title = title;
    }

    @Override
    protected boolean doRenderTickLocal(int xStart, int yStart, int width, int height, EnumEventState ees) {
        if(ees.equals(EnumEventState.PRE)) return  true;

        if(!isOpen){
            GlStateManager.enableBlend();

            drawModalRectWithCustomSizedTexture( //draws the image shown
                                                 xStart + this.getWidth() - 10, yStart, 0, 0,
                                                 5, 170, 10, 17, ImageLoader.shadowLocation(), 0.4f);

            GlStateManager.disableBlend();
        }





        if(isOpen)  RenderUtils.renderBoxWithColor(xStart, yStart, width, 2, color);
        else        RenderUtils.renderBoxWithColor(xStart, yStart + ELEMENT_HEIGHT -2, width, 2, color);
        FontHelper.draw(ChatFormatting.GRAY + title, xStart + 5, yStart + 4);



        return true;
    }
}
