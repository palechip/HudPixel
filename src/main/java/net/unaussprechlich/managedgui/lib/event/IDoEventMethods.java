/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.event;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.unaussprechlich.managedgui.lib.event.bus.IEvent;
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;

/**
 * IDoEventMethods Created by unaussprechlich on 18.12.2016.
 * Description:
 **/
public interface IDoEventMethods {

    default boolean doClientTick(){return true;}
    default boolean doRender(){return true;}
    default boolean doChatMessage(ClientChatReceivedEvent e){return true;}
    default boolean doMouseMove(int mX, int mY){return true;}
    default boolean doScroll(int i){return true;}
    default boolean doClick(MouseHandler.ClickType clickType){return true;}
    default boolean doEventBus(IEvent event){return true;}
    default boolean doOpenGUI(GuiOpenEvent e){return true;}

}
