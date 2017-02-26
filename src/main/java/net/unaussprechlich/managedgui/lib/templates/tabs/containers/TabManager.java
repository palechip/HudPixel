/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.templates.tabs.containers;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.unaussprechlich.managedgui.lib.container.Container;
import net.unaussprechlich.managedgui.lib.event.util.Event;
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;
import net.unaussprechlich.managedgui.lib.util.EnumEventState;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * TabManager Created by Alexander on 24.02.2017.
 * Description:
 **/
public class TabManager extends Container{


    private ArrayList<TabContainer> tabs = new ArrayList<>();

    private TabContainer activeTab;

    public TabManager(){
    }

    public void setTabActive(TabContainer tab){
        if(activeTab == tab) return;
        if(activeTab != null) activeTab.setClosed();
        activeTab = tab;
        activeTab.setOpen();
    }


    public void registerTab(TabContainer tab){
        if(activeTab == null)setTabActive(tab);
        registerChild(tab);
        tabs.add(tab);
        updatePositions();
    }

    public void unregisterTab(TabContainer tab){
        unregisterChild(tab);
        tabs.remove(tab);
        updatePositions();
    }

    private void updatePositions(){
        int offset = 0;
        for(TabListElementContainer element : tabs.stream().map(TabContainer::getTabListElement).collect(Collectors.toList())){
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
