/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.elements.tabs;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.unaussprechlich.managedgui.lib.elements.Container;
import net.unaussprechlich.managedgui.lib.elements.tabs.containers.TabContainer;
import net.unaussprechlich.managedgui.lib.elements.tabs.containers.TabListManager;
import net.unaussprechlich.managedgui.lib.event.util.Event;
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;
import net.unaussprechlich.managedgui.lib.util.EnumEventState;

import java.util.ArrayList;

/**
 * TabManagerContainer Created by Alexander on 24.02.2017.
 * Description:
 **/
public class TabManagerContainer extends Container{

    private ArrayList<TabContainer> tabs = new ArrayList<>();

    private TabListManager tabListManager;

    private TabContainer activeTab;

    public TabManagerContainer(TabListManager tabListManager){
        this.tabListManager = tabListManager;
        registerChild(tabListManager);
    }

    public void setTabActive(TabContainer tab){
        if(activeTab != null) activeTab.setClosed();
        activeTab = tab;
        activeTab.setOpen();
    }

    public void addTab(TabContainer tab){
        if(activeTab == null) setTabActive(tab);
        tabListManager.registerTab(tab);
        tabs.add(tab);
        registerChild(tab);
    }

    public void removeTab(TabContainer tab){
        tabListManager.unregisterTab(tab);
        tabs.remove(tab);
        unregisterChild(tab);
    }

    @Override
    protected boolean doClientTickLocal() {
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
        return false;
    }

    @Override
    protected boolean doOpenGUILocal(GuiOpenEvent e) {
        return false;
    }






}
