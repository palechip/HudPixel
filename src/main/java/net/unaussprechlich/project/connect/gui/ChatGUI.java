package net.unaussprechlich.project.connect.gui;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.unaussprechlich.managedgui.lib.elements.GUI;
import net.unaussprechlich.managedgui.lib.elements.tabs.TabManagerContainer;
import net.unaussprechlich.managedgui.lib.elements.tabs.containers.TabContainer;
import net.unaussprechlich.managedgui.lib.elements.tabs.containers.TabElementContainer;
import net.unaussprechlich.managedgui.lib.elements.tabs.containers.TabListElementContainer;
import net.unaussprechlich.managedgui.lib.elements.tabs.containers.TabListManager;
import net.unaussprechlich.managedgui.lib.event.EnumDefaultEvents;
import net.unaussprechlich.managedgui.lib.event.util.Event;
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;
import net.unaussprechlich.managedgui.lib.util.DisplayUtil;
import net.unaussprechlich.managedgui.lib.util.EnumRGBA;

/**
 * ChatGUI Created by Alexander on 24.02.2017.
 * Description:
 **/
public class ChatGUI extends GUI{

    private TabManagerContainer tabManager = new TabManagerContainer(new TabListManager());

    private static final int WIDTH  = 500;
    private static final int HEIGHT = 200;


    public ChatGUI(){
        registerChild(tabManager);

        tabManager.addTab(new TabContainer(new TabListElementContainer("ALL", EnumRGBA.WHITE.get()), new TabElementContainer(WIDTH, HEIGHT)));
        tabManager.addTab(new TabContainer(new TabListElementContainer("HudPixel", EnumRGBA.RED.get()), new TabElementContainer(WIDTH, HEIGHT)));
        tabManager.addTab(new TabContainer(new TabListElementContainer("Guild", EnumRGBA.GREEN.get()), new TabElementContainer(WIDTH, HEIGHT)));
        tabManager.addTab(new TabContainer(new TabListElementContainer("Party", EnumRGBA.BLUE.get()), new TabElementContainer(WIDTH, HEIGHT)));

        updatePosition();
    }

    private void updatePosition(){
        setXStart(5);
        setYStart(DisplayUtil.getScaledMcHeight() - HEIGHT - 5 - TabListElementContainer.ELEMENT_HEIGHT);
    }

    public TabManagerContainer getTabManager() {
        return tabManager;
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
        if(event.getID() == EnumDefaultEvents.SCREEN_RESIZE.get()){
            updatePosition();
        }
        return true;
    }

    @Override
    public boolean doOpenGUI(GuiOpenEvent e) {
        return true;
    }
}
