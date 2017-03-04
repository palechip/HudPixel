/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.templates.tabs.containers;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.unaussprechlich.managedgui.lib.Constants;
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefBackgroundContainer;
import net.unaussprechlich.managedgui.lib.util.*;

/**
 * TabListElementContainer Created by Alexander on 24.02.2017.
 * Description:
 **/
public class TabListElementContainer extends DefBackgroundContainer {

    public static final int ELEMENT_HEIGHT = 17;

    private final TabManager tabManager;

    public void setOpen(boolean open) {
        isOpen = open;
    }

    private boolean isOpen = false;
    private ColorRGBA color;
    private String title;

    public TabListElementContainer(String title, ColorRGBA color, TabManager tabManager) {
        super(Constants.DEF_BACKGROUND_RGBA, FontUtil.widthOfString(title) + 10, ELEMENT_HEIGHT);
        this.color = color;
        this.title = title;
        this.tabManager = tabManager;
    }

    @Override
    protected boolean doClickLocal(MouseHandler.ClickType clickType, boolean isThisContainer) {
        return true;
    }

    @Override
    protected boolean doMouseMoveLocal(int mX, int mY) {
        return true;
    }

    @Override
    protected boolean doRenderTickLocal(int xStart, int yStart, int width, int height, EnumEventState ees) {
        if(ees.equals(EnumEventState.PRE)) return  true;

        if(isHover() && !isOpen){
            ColorRGBA color = new ColorRGBA(60, 60, 70, 255);
            RenderUtils.renderRectWithColorBlendFade_s1_d0(xStart, yStart, getWidth(), ELEMENT_HEIGHT, Constants.DEF_BACKGROUND_RGBA, color, color, color);

        }

        if(!isOpen && !isHover()){

            ColorRGBA color = new ColorRGBA(50, 50, 60, 255);

            RenderUtils.renderRectWithColorBlendFade_s1_d0(xStart + this.getWidth() - 8, yStart, 8, ELEMENT_HEIGHT, Constants.DEF_BACKGROUND_RGBA, color, color, RGBA.P1B1_DEF.get());
        }

        if(isOpen)  RenderUtils.renderBoxWithColor(xStart, yStart, width, 2, color);
        else        RenderUtils.renderBoxWithColor(xStart, yStart + ELEMENT_HEIGHT -2, width, 2, color);
        FontUtil.getINSTANCE().drawFormatted(ChatFormatting.GRAY + title, xStart + 5, yStart + 4);

        return true;
    }
}
