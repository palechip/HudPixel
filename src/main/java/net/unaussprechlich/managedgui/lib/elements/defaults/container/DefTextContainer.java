/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.elements.defaults.container;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.unaussprechlich.managedgui.lib.elements.Container;
import net.unaussprechlich.managedgui.lib.event.util.Event;
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;
import net.unaussprechlich.managedgui.lib.util.EnumEventState;
import net.unaussprechlich.managedgui.lib.util.FontHelper;

/**
 * DefTextContainer Created by unaussprechlich on 20.12.2016.
 * Description:
 **/
public class DefTextContainer extends Container{

    private String text = "";
    private boolean isShadow = false;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        super.setWidth(FontHelper.widthOfString(text));
        super.setHeight(9);
    }

    public DefTextContainer(String text) {
        setText(text);
    }

    public boolean isShadow() {
        return isShadow;
    }

    public void setShadow(boolean shadow) {
        isShadow = shadow;
    }

    @Override
    public void setWidth(int width) {
        throw new UnsupportedOperationException("[ManagedGuiLib][DefTextContainer] setWidth() is handled automatically use setPadding() instead!");
    }

    @Override
    public void setHeight(int width) {
        throw new UnsupportedOperationException("[ManagedGuiLib][DefTextContainer] setHeight() is handled automatically use setPadding() instead!");
    }

    @Override
    protected boolean doClientTickLocal() {
        return true;
    }

    @Override
    protected boolean doRenderTickLocal(int xStart, int yStart, int width, int height, EnumEventState ees) {
        if(ees == EnumEventState.POST){
            if(isShadow) FontHelper.drawWithShadow(text, xStart, yStart);
            else         FontHelper.draw(text, xStart, yStart);
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
        return true;
    }

    @Override
    protected <T extends Event> boolean doEventBusLocal(T e) {
        return true;
    }

    @Override
    protected boolean doOpenGUILocal(GuiOpenEvent e) {
        return true;
    }
}