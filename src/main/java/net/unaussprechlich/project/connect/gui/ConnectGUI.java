package net.unaussprechlich.project.connect.gui;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.unaussprechlich.managedgui.lib.GuiManagerMG;
import net.unaussprechlich.managedgui.lib.event.EnumDefaultEvents;
import net.unaussprechlich.managedgui.lib.event.util.EnumTime;
import net.unaussprechlich.managedgui.lib.event.util.Event;
import net.unaussprechlich.managedgui.lib.gui.GUI;
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;

/**
 * ConnectGUI Created by unaussprechlich on 20.12.2016.
 * Description:
 **/
public class ConnectGUI extends GUI {

    private static NotificationGUI notificationGUI = new NotificationGUI();
    private static ChatGUI chatGUI = new ChatGUI();

    public ConnectGUI() {
        GuiManagerMG.addGUI("NotificationGUI", notificationGUI);
        GuiManagerMG.addGUI("ConnectChatGui" , chatGUI);
    }

    public static NotificationGUI getNotificationGUI() {
        return notificationGUI;
    }

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
    public <T extends Event> boolean doEventBus(T event) {
        if(!(event.getID() == EnumDefaultEvents.TIME.get() && event.getData() == EnumTime.SEC_15)) return true;
        return true;
    }

    @Override
    public boolean doOpenGUI(GuiOpenEvent e) {
        return true;
    }


}
