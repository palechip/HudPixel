package net.unaussprechlich.project.connect.gui;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.unaussprechlich.managedgui.lib.event.EnumDefaultEvents;
import net.unaussprechlich.managedgui.lib.event.util.Event;
import net.unaussprechlich.managedgui.lib.gui.GUI;
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefScrollableContainer;
import net.unaussprechlich.managedgui.lib.templates.defaults.container.DefTextContainer;
import net.unaussprechlich.managedgui.lib.templates.defaults.container.IScrollSpacerRenderer;
import net.unaussprechlich.managedgui.lib.templates.tabs.containers.TabContainer;
import net.unaussprechlich.managedgui.lib.templates.tabs.containers.TabListElementContainer;
import net.unaussprechlich.managedgui.lib.templates.tabs.containers.TabManager;
import net.unaussprechlich.managedgui.lib.util.DisplayUtil;
import net.unaussprechlich.managedgui.lib.util.EnumRGBA;
import net.unaussprechlich.managedgui.lib.util.RenderUtils;

/**
 * ChatGUI Created by Alexander on 24.02.2017.
 * Description:
 **/
public class ChatGUI extends GUI{

    private final TabManager tabManager = new TabManager();

    private static final int WIDTH  = 500;
    private static final int HEIGHT = 200;


    public ChatGUI(){

        registerChild(tabManager);

        DefScrollableContainer elementAllContainer = new DefScrollableContainer(EnumRGBA.GREY_COOL.get(), WIDTH, HEIGHT, new IScrollSpacerRenderer() {
            @Override
            public void render(int xStart, int yStart, int width) {
                RenderUtils.renderBoxWithColor(xStart + 2, yStart, width-4, 1, EnumRGBA.RED.get());
            }

            @Override
            public int getSpacerHeight() {
                return 3;
            }
        });


        tabManager.registerTab(new TabContainer(new TabListElementContainer("ALL", EnumRGBA.WHITE.get(), tabManager), elementAllContainer, tabManager));
        //tabManager.registerTab(new TabContainer(new TabListElementContainer("Party", EnumRGBA.BLUE.get(), tabManager), new TabElementContainer(WIDTH, HEIGHT), tabManager));
        //tabManager.registerTab(new TabContainer(new TabListElementContainer("Guild", EnumRGBA.GREEN.get(), tabManager), new TabElementContainer(WIDTH, HEIGHT), tabManager));
        //tabManager.registerTab(new TabContainer(new TabListElementContainer("Private", EnumRGBA.RED.get(), tabManager), new TabElementContainer(WIDTH, HEIGHT), tabManager));

        for (int i = 0; i < 50; i++){
            elementAllContainer.registerScrollElement(new DefTextContainer("Das ist ein test! -> " + i));
        }

        updatePosition();
    }

    private void updatePosition(){
        setXStart(0);
        setYStart(DisplayUtil.getScaledMcHeight() - HEIGHT - 5 - TabListElementContainer.ELEMENT_HEIGHT);
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
