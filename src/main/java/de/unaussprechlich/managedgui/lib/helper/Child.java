package de.unaussprechlich.managedgui.lib.helper;

import net.minecraftforge.client.event.ClientChatReceivedEvent;

/**
 * Created by kecka on 26.08.2016.
 */
public interface Child {

    void onClientTick();

    void onRenderTick();

    void onChatMessage(ClientChatReceivedEvent e);

    void onClick(MouseHandler.ClickType clickType);

    void onScroll(int i);

    void onMouseMove(int mX, int mY);

}
