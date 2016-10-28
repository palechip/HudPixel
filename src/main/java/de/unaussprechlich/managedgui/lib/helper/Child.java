package de.unaussprechlich.managedgui.lib.helper;

import net.minecraftforge.client.event.ClientChatReceivedEvent;

/* *****************************************************************************

 * Copyright (c) 2016 unaussprechlich

 *******************************************************************************/
public interface Child {

    void onClientTick();

    void onRenderTick();

    void onChatMessage(ClientChatReceivedEvent e);

    void onClick(MouseHandler.ClickType clickType);

    void onScroll(int i);

    void onMouseMove(int mX, int mY);

}
