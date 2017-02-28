package net.unaussprechlich.project.connect.gui;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.unaussprechlich.managedgui.lib.event.EnumDefaultEvents;
import net.unaussprechlich.managedgui.lib.event.util.Event;
import net.unaussprechlich.managedgui.lib.gui.GUI;
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;
import net.unaussprechlich.managedgui.lib.templates.defaults.container.*;
import net.unaussprechlich.managedgui.lib.templates.tabs.containers.TabContainer;
import net.unaussprechlich.managedgui.lib.templates.tabs.containers.TabListElementContainer;
import net.unaussprechlich.managedgui.lib.templates.tabs.containers.TabManager;
import net.unaussprechlich.managedgui.lib.util.ColorRGBA;
import net.unaussprechlich.managedgui.lib.util.DisplayUtil;
import net.unaussprechlich.managedgui.lib.util.RGBA;
import net.unaussprechlich.managedgui.lib.util.RenderUtils;

import static com.mojang.realmsclient.gui.ChatFormatting.*;

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

        DefScrollableContainer scrollALL = new DefScrollableContainer(RGBA.P1B1_DEF.get(), WIDTH , HEIGHT - 17, new IScrollSpacerRenderer() {
            @Override
            public void render(int xStart, int yStart, int width) {
                RenderUtils.renderRectWithColorFadeHorizontal_s1_d1(xStart + 25, yStart, width - 45, 2, RGBA.P1B1_DEF.get(), new ColorRGBA(30, 30, 30, 255));
            }

            @Override
            public int getSpacerHeight() {
                return 2;
            }
        });

        DefChatMessageContainer test1 = new DefChatMessageContainer(
                AQUA + "[MVP" + GREEN + "+" + AQUA + "]" + "unaussprechlich",
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.",
                new DefPictureContainer(),
                WIDTH
        );

        DefChatMessageContainer test2 = new DefChatMessageContainer(
                WHITE + "aussprechlich",
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.",
                new DefPictureContainer(),
                WIDTH
        );

        DefChatMessageContainer test3 = new DefChatMessageContainer(
                WHITE + "betbetebntebn",
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.",
                new DefPictureContainer(),
                WIDTH
        );

        DefChatMessageContainer test4 = new DefChatMessageContainer(
                WHITE + "betbetebnteeerebn",
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.",
                new DefPictureContainer(),
                WIDTH
        );

        DefChatMessageContainer test5 = new DefChatMessageContainer(
                WHITE + "betbetebnteerbtbebebn",
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.",
                new DefPictureContainer(),
                WIDTH
        );

        test1.addMessage("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.");
        test1.addMessage("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.");
        test2.addMessage("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.");
        test2.addMessage("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.");
        test3.addMessage("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.");
        test3.addMessage("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.");
        test4.addMessage("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.");
        test4.addMessage("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.");
        test5.addMessage("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.");
        test5.addMessage("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.");

        scrollALL.registerScrollElement(test1);
        scrollALL.registerScrollElement(test2);
        scrollALL.registerScrollElement(test3);
        scrollALL.registerScrollElement(test4);
        scrollALL.registerScrollElement(test5);

        tabManager.registerTab(new TabContainer(new TabListElementContainer("ALL", RGBA.WHITE.get(), tabManager), scrollALL, tabManager));
        tabManager.registerTab(new TabContainer(new TabListElementContainer("Party", RGBA.BLUE.get(), tabManager), new DefBackgroundContainerFrame(WIDTH, HEIGHT, RGBA.BLUE.get()), tabManager));
        tabManager.registerTab(new TabContainer(new TabListElementContainer("Guild", RGBA.GREEN.get(), tabManager), new DefBackgroundContainerFrame(WIDTH, HEIGHT, RGBA.GREEN.get()), tabManager));
        tabManager.registerTab(new TabContainer(new TabListElementContainer("Private", RGBA.RED.get(), tabManager), new DefBackgroundContainerFrame(WIDTH, HEIGHT, RGBA.RED.get()), tabManager));

        updatePosition();
    }

    private void updatePosition(){
        tabManager.setXOffset(5);
        tabManager.setYOffset(DisplayUtil.getScaledMcHeight() - HEIGHT - 17 - 30);
    }


    @Override
    public boolean doClientTick() {
        //TODO OFFSET RENDERING IS BROKEN! MAYBE IN CONTAINER_FRAME
        tabManager.setYOffset(DisplayUtil.getScaledMcHeight() - HEIGHT  );
        tabManager.setXOffset(0);
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
