package net.unaussprechlich.project.connect.gui;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.unaussprechlich.managedgui.lib.elements.GUI;
import net.unaussprechlich.managedgui.lib.elements.defaults.container.DefNotificationContainer;
import net.unaussprechlich.managedgui.lib.event.EnumDefaultEvents;
import net.unaussprechlich.managedgui.lib.event.util.EnumTime;
import net.unaussprechlich.managedgui.lib.event.util.Event;
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * NotificationGUI Created by Alexander on 23.02.2017.
 * Description:
 **/
public class NotificationGUI extends GUI{

    private static final int SPACING = 1;

    private ArrayList<DefNotificationContainer> notifications = new ArrayList<>();

    public void addNotification(DefNotificationContainer notify){
        notifications.add(notify);
        registerChild(notify);
        updatePositions();
    }

    private void updatePositions(){
        int yPos = 0;
        for(DefNotificationContainer notify : notifications){
            notify.setYOffset(yPos);
            yPos += notify.getHeight() + SPACING;
        }
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
        return false;
    }

    @Override
    public boolean doMouseMove(int mX, int mY) {
        return false;
    }

    @Override
    public boolean doScroll(int i) {
        return false;
    }

    @Override
    public boolean doClick(MouseHandler.ClickType clickType) {
        return false;
    }

    @Override
    public <T extends Event> boolean doEventBus(T event) {
        if(event.getID() == EnumDefaultEvents.TIME.get() && event.getData() == EnumTime.SEC_1){
            notifications.removeAll(notifications.stream().filter(cont -> cont.getShowtime_sec() <= 0).collect(Collectors.toList()));
            updatePositions();
        }
        return true;
    }

    @Override
    public boolean doOpenGUI(GuiOpenEvent e) {
        return true;
    }
}
