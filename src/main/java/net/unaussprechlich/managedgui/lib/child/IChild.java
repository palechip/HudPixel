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

public interface IChild {

    void onClientTick();

    void onRender(int xStart, int yStart);

    void onChatMessage(ClientChatReceivedEvent e);

    void onClick(MouseHandler.ClickType clickType);

    void onScroll(int i);

    void onMouseMove(int mX, int mY);

    <T extends Event> void onEventBus(T event);

    void onOpenGui(GuiOpenEvent event);

    void onResize();



}
