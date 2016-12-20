/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.elements.defaults.gui;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.unaussprechlich.managedgui.lib.elements.GUI;
import net.unaussprechlich.managedgui.lib.event.events.Event;
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;

/**
 * DefGUI Created by unaussprechlich on 18.12.2016.
 * Description:
 **/
public class DefGUI extends GUI {


    @Override
    public boolean doClientTick() {
        return true;
    }

    @Override
    public boolean doRender(int xStart, int yStart) {
        return true;
    }

    @Override
    public boolean doChatMessage(ClientChatReceivedEvent e) {
        return true;
    }

    @Override
    public boolean doMouseMove(int mX, int mY) {
        return true;
    }

    @Override
    public boolean doScroll(int i) {
        return true;
    }

    @Override
    public boolean doClick(MouseHandler.ClickType clickType) {
        return true;
    }

    @Override
    public <T extends Event>  boolean doEventBus(T event) {
        return true;
    }

    @Override
    public boolean doOpenGUI(GuiOpenEvent e) {
        return true;
    }
}
