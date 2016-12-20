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
import net.unaussprechlich.managedgui.lib.event.events.Event;
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;
import net.unaussprechlich.managedgui.lib.util.EnumEventState;
import net.unaussprechlich.managedgui.lib.util.FontHelper;

/**
 * DefTextContainer Created by unaussprechlich on 20.12.2016.
 * Description:
 **/
public class DefTextContainer extends Container{

    private String text = "";

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public DefTextContainer(String text) {
        this.text = text;
    }

    @Override
    protected boolean doClientTickLocal() {
        return true;
    }

    @Override
    protected boolean doRenderTickLocal(int xStart, int yStart, int width, int height, EnumEventState ees) {
        if(ees == EnumEventState.POST){
            FontHelper.drawWithShadow(text, xStart, yStart);
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
