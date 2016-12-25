package net.unaussprechlich.project.connect.gui;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.unaussprechlich.managedgui.lib.elements.GUI;
import net.unaussprechlich.managedgui.lib.elements.defaults.container.DefBackgroundContainer;
import net.unaussprechlich.managedgui.lib.elements.defaults.container.DefTextListAutoLineBreakContainer;
import net.unaussprechlich.managedgui.lib.elements.defaults.container.DefTextListContainer;
import net.unaussprechlich.managedgui.lib.event.EnumDefaultEvents;
import net.unaussprechlich.managedgui.lib.event.util.EnumTime;
import net.unaussprechlich.managedgui.lib.event.util.Event;
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;
import net.unaussprechlich.managedgui.lib.util.ColorRGBA;
import net.unaussprechlich.managedgui.lib.util.EnumRGBA;

import java.util.ArrayList;

/**
 * ConnectGUI Created by unaussprechlich on 20.12.2016.
 * Description:
 **/
public class ConnectGUI extends GUI {

    private String test = "";

    private DefTextListContainer testwef = new DefTextListContainer(new ArrayList<>());

    private final DefBackgroundContainer            testBackgroundCont = new DefBackgroundContainer(EnumRGBA.GREY_T.get(), 10, 10);
    private final DefTextListAutoLineBreakContainer textListContainer  = new DefTextListAutoLineBreakContainer(new ArrayList<String>() {{
        add("ONE");
        add("TWO haha keine ahning warum ich das jetzt so mache :O");
        add("THREE");
        add("FOUR");
    }}, 100);

    public ConnectGUI() {
        registerChild(testBackgroundCont);
        testBackgroundCont.registerChild(textListContainer);

        testBackgroundCont.setHeight(textListContainer.getHeight());
        testBackgroundCont.setWidth(textListContainer.getWidth());
        testBackgroundCont.setBorder(1);
        testBackgroundCont.setBorderRGBA(EnumRGBA.RED.get());
        testBackgroundCont.setXOffset(20);
        testBackgroundCont.setYOffset(20);
    }

    @Override
    public boolean doClientTick() {
        testBackgroundCont.setBackgroundRGBA(new ColorRGBA(0, 0, 0, 125));
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
        if(event.getID() == EnumDefaultEvents.TIME.get()){
            EnumTime  enumTime = (EnumTime) event.getData();
            if(enumTime.equals(EnumTime.SEC_15)){
                System.out.println("SEC_1");
                textListContainer.setWidth(100);
                testBackgroundCont.setHeight(textListContainer.getHeight());
                testBackgroundCont.setWidth(textListContainer.getWidth());
                return true;
            }else if(enumTime.equals(EnumTime.SEC_5)){
                System.out.println("Test:" + enumTime);
                textListContainer.setWidth(300);
                testBackgroundCont.setHeight(textListContainer.getHeight());
                testBackgroundCont.setWidth(textListContainer.getWidth());
                return true;
            } else if(enumTime.equals(EnumTime.TICK_5)){
                test += "a";
                textListContainer.addEntry("Das ist ein Test :D rwggrewergwgrwergwewrgewgerwegwegwegegwewgegwewg" +  test);
                testBackgroundCont.setHeight(textListContainer.getHeight());
                testBackgroundCont.setWidth(textListContainer.getWidth());
                return true;
            }else if(enumTime.equals(EnumTime.SEC_30)){
                test = "";
                testBackgroundCont.setHeight(textListContainer.getHeight());
                testBackgroundCont.setWidth(textListContainer.getWidth());
                textListContainer.clearAll();
                return true;
            }
        }

        return true;
    }

    @Override
    public boolean doOpenGUI(GuiOpenEvent e) {
        return true;
    }


}
