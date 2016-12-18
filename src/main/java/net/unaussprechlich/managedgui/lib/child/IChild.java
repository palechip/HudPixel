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
import net.unaussprechlich.managedgui.lib.event.bus.IEvent;
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;

public interface IChild {

    void onClientTick();

    void onRender();

    void onChatMessage(ClientChatReceivedEvent e);

    void onClick(MouseHandler.ClickType clickType);

    void onScroll(int i);

    void onMouseMove(int mX, int mY);

    void onEventBus(IEvent event);

    void onOpenGui(GuiOpenEvent event);

}
