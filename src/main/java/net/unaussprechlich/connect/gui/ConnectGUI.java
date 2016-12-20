package net.unaussprechlich.connect.gui;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.unaussprechlich.managedgui.lib.elements.GUI;
import net.unaussprechlich.managedgui.lib.elements.defaults.container.DefTextContainer;
import net.unaussprechlich.managedgui.lib.event.events.Event;
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;
import net.unaussprechlich.managedgui.lib.util.EnumRGBA;

/**
 * ConnectGUI Created by unaussprechlich on 20.12.2016.
 * Description:
 **/
public class ConnectGUI extends GUI {

    private DefTextContainer connectVersionContainer = new DefTextContainer("");
    private DefTextContainer testContainer = new DefTextContainer(EnumChatFormatting.DARK_AQUA + "Connect v0.1");
    private DefTextContainer test2Container = new DefTextContainer(EnumChatFormatting.DARK_AQUA + "Das ist ein test");


    public ConnectGUI() {
        registerChild(connectVersionContainer);
        connectVersionContainer.registerChild(testContainer);
        connectVersionContainer.registerChild(test2Container);

        setXStart(2);
        setYStart(2);

        connectVersionContainer.getBorder().BOTTOM(2).TOP(2).LEFT(2).RIGHT(2);
        connectVersionContainer.setBorderRGBA(EnumRGBA.RED.get());
        connectVersionContainer.setBackgroundRGBA(EnumRGBA.GREY_T.get());
        connectVersionContainer.setWidth(100);
        connectVersionContainer.setHeight(20);

        testContainer.getBorder().BOTTOM(1);
        testContainer.setBorderRGBA(EnumRGBA.RED.get());
        testContainer.setBackgroundRGBA(EnumRGBA.GREEN.get());
        testContainer.setWidth(100);
        testContainer.setHeight(10);

        test2Container.setYOffset(10);
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
        return true;
    }

    @Override
    public boolean doOpenGUI(GuiOpenEvent e) {
        return true;
    }


}
