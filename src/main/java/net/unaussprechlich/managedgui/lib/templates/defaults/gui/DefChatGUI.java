/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.templates.defaults.gui;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.unaussprechlich.managedgui.lib.event.util.Event;
import net.unaussprechlich.managedgui.lib.gui.GUI;
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;

/**
 * DefChatGUI Created by unaussprechlich on 18.12.2016.
 * Description:
 **/
public class DefChatGUI extends GUI {


    @Override
    public boolean doClientTick() {
        return isChatGUIShown();
    }

    @Override
    public boolean doRender(int xStart, int yStart) {
        return isChatGUIShown();
    }

    @Override
    public boolean doChatMessage(ClientChatReceivedEvent e) {
        return isChatGUIShown();
    }

    @Override
    public boolean doMouseMove(int mX, int mY) {
        return isChatGUIShown();
    }

    @Override
    public boolean doScroll(int i) {
        return isChatGUIShown();
    }

    @Override
    public boolean doClick(MouseHandler.ClickType clickType) {
        return isChatGUIShown();
    }

    @Override
    public <T extends Event> boolean doEventBus(T event) {
        return isChatGUIShown();
    }

    @Override
    public boolean doOpenGUI(GuiOpenEvent e) {
        return isChatGUIShown();
    }

    @Override
    public boolean doResize() {
        return true;
    }
}
