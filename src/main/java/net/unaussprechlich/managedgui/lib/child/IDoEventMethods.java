/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.child;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.unaussprechlich.managedgui.lib.event.util.Event;
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;

/**
 * IDoEventMethods Created by unaussprechlich on 18.12.2016.
 * Description:
 **/
public interface IDoEventMethods {

    boolean doClientTick();
    boolean doRender(int xStart, int yStart);
    boolean doChatMessage(ClientChatReceivedEvent e);
    boolean doMouseMove(int mX, int mY);
    boolean doScroll(int i);
    boolean doClick(MouseHandler.ClickType clickType);
    <T extends Event> boolean doEventBus(T event);
    boolean doOpenGUI(GuiOpenEvent e);
    boolean doResize();
    int getXStart();
    int getYStart();

}
