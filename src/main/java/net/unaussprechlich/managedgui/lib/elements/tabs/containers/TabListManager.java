/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.elements.tabs.containers;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.unaussprechlich.managedgui.lib.elements.Container;
import net.unaussprechlich.managedgui.lib.event.util.Event;
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;
import net.unaussprechlich.managedgui.lib.util.EnumEventState;

import java.util.ArrayList;

/**
 * TabListManager Created by Alexander on 24.02.2017.
 * Description:
 **/
public class TabListManager extends Container{

    private ArrayList<TabListElementContainer> listElements = new ArrayList<>();

    public void registerTab(TabContainer tabContainer){
        listElements.add(tabContainer.getTabListElement());
        registerChild(tabContainer.getTabListElement());
        updatePositions();
    }

    public void unregisterTab(TabContainer tabContainer){
        listElements.remove(tabContainer.getTabListElement());
        unregisterChild(tabContainer.getTabListElement());
        updatePositions();
    }

    private void updatePositions(){
        int offset = 0;
        for(TabListElementContainer element : listElements){
            element.setXOffset(offset);
            offset += element.getWidth();
        }
    }

    @Override
    protected boolean doClientTickLocal(){
        return true;
    }

    @Override
    protected boolean doRenderTickLocal(int xStart, int yStart, int width, int height, EnumEventState ees) {
       return true;
    }

    @Override
    protected boolean doChatMessageLocal(ClientChatReceivedEvent e) {
        return true;
    }

    @Override
    protected boolean doClickLocal(MouseHandler.ClickType clickType, boolean isThisContainer) {
        return true;
    }

    @Override
    protected boolean doScrollLocal(int i, boolean isThisContainer) {
        return true;
    }

    @Override
    protected boolean doMouseMoveLocal(int mX, int mY) {
        return true;
    }

    @Override
    protected <T extends Event> boolean doEventBusLocal(T iEvent) {
        return true;
    }

    @Override
    protected boolean doOpenGUILocal(GuiOpenEvent e) {
        return true;
    }
}
