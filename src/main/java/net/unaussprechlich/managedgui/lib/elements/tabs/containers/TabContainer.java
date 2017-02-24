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

/**
 * TabContainer Created by Alexander on 24.02.2017.
 * Description:
 **/
public class TabContainer extends Container{

    private TabElementContainer     tabElement;
    private TabListElementContainer tabListElement;

    public TabContainer(TabListElementContainer tabListElement, TabElementContainer tabElement){
        this.tabListElement = tabListElement;
        this.tabElement = tabElement;
        setup();
    }

    private void setup(){
        registerChild(this.tabElement);
        tabElement.setYOffset(TabListElementContainer.ELEMENT_HEIGHT);
    }

    public TabElementContainer getTabElement() {
        return tabElement;
    }

    public TabListElementContainer getTabListElement() {
        return tabListElement;
    }

    public void setClosed(){
        tabListElement.setOpen(false);
        setVisible(false);
    }

    public void setOpen(){
        tabListElement.setOpen(true);
        setVisible(true);
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
        return true;
    }

    @Override
    protected boolean doOpenGUILocal(GuiOpenEvent e) {
        return true;
    }
}
