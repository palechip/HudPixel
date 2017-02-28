/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.templates.defaults.container;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.unaussprechlich.managedgui.lib.container.Container;
import net.unaussprechlich.managedgui.lib.event.util.Event;
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;
import net.unaussprechlich.managedgui.lib.util.EnumEventState;
import net.unaussprechlich.managedgui.lib.util.RGBA;
import net.unaussprechlich.managedgui.lib.util.RenderUtils;

/**
 * DefPictureContainer Created by Alexander on 27.02.2017.
 * Description:
 **/
public class DefPictureContainer extends Container {

    private final ResourceLocation resourceLocation;

    public DefPictureContainer(int width, int height, ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
        setWidth(width);
        setHeight(height);
    }

    public DefPictureContainer(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    public DefPictureContainer() {
        this.resourceLocation = null;

    }

    @Override
    protected boolean doClientTickLocal() {
        return true;
    }

    @Override
    protected boolean doRenderTickLocal(int xStart, int yStart, int width, int height, EnumEventState ees) {
                if(ees.equals(EnumEventState.POST)) {
            if (resourceLocation == null) {
                RenderUtils.renderBoxWithColor(xStart, yStart, width, height, RGBA.RED.get());
                RenderUtils.renderLoadingBar(xStart + width / 2 - 5, yStart + height / 2 - 3);
            } else {
                RenderUtils.drawModalRectWithCustomSizedTexture(xStart, yStart, getWidth(), getHeight(), resourceLocation, 1f);
            }
        }
        return true;
    }

    @Override
    protected boolean doChatMessageLocal(ClientChatReceivedEvent e) {
        return true;
    }

    @Override
    protected boolean doClickLocal(MouseHandler.ClickType clickType, boolean isThisContainer) {
        return true;
    }

    @Override
    protected boolean doScrollLocal(int i, boolean isThisContainer) {
        return true;
    }

    @Override
    protected boolean doMouseMoveLocal(int mX, int mY) {
        return false;
    }

    @Override
    protected <T extends Event> boolean doEventBusLocal(T iEvent) {
        return true;
    }

    @Override
    protected boolean doOpenGUILocal(GuiOpenEvent e) {
        return true;
    }
}
