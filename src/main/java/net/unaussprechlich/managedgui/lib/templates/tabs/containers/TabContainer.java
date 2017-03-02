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

import javax.annotation.Nonnull;

/**
 * TabContainer Created by Alexander on 24.02.2017.
 * Description:
 **/
@Nonnull
public class TabContainer extends Container{

    private Container               container;
    private TabListElementContainer tabListElement;

    private final TabManager tabManager;

    public TabContainer(@Nonnull TabListElementContainer tabListElement, @Nonnull Container container, @Nonnull TabManager tabManager){
        this.tabListElement = tabListElement;
        this.container = container;
        this.tabManager = tabManager;
        setup();
    }

    private void setup(){
        registerChild(this.container);
        registerChild(this.tabListElement);
        container.setYOffset(TabListElementContainer.ELEMENT_HEIGHT);
        tabListElement.registerClickedListener((clickType, container) -> {
            if(clickType.equals(MouseHandler.ClickType.SINGLE)) setActive();
        });
        setClosed();
    }

    public void setActive(){
        tabManager.setTabActive(this);
    }

    public Container getContainer() {
        return container;
    }

    public TabListElementContainer getTabListElement() {
        return tabListElement;
    }

    public void setClosed(){
        tabListElement.setOpen(false);
        container.setVisible(false);
    }

    public void setOpen(){
        tabListElement.setOpen(true);
        container.setVisible(true);
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

    @Override
    protected boolean doResizeLocal(int width, int height) {
        return true;
    }
}
