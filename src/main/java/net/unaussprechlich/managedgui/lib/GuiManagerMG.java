/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.unaussprechlich.managedgui.lib.elements.GUI;
import net.unaussprechlich.managedgui.lib.event.events.EnumTime;
import net.unaussprechlich.managedgui.lib.event.events.Event;
import net.unaussprechlich.managedgui.lib.event.events.TimeEvent;
import net.unaussprechlich.managedgui.lib.exceptions.NameInUseException;
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;
import net.unaussprechlich.managedgui.lib.util.LoggerHelperMG;

import java.util.HashMap;

/**
 * GuiManagerMG Created by unaussprechlich on 18.12.2016.
 * Description:
 **/
public class GuiManagerMG {

    //DEFAULT TYPED ----------------------------------------------------------------------------------------------------

    public enum EnumGUITypes {
        TEST, CHAT, ESC, INGAME
    }

    //FIELDS -----------------------------------------------------------------------------------------------------------

    private static HashMap<EnumGUITypes, GUI> GUIs        = new HashMap<>();
    private static HashMap<String, GUI>       GUIsDynamic = new HashMap<>();

    //SINGLETONE -------------------------------------------------------------------------------------------------------

    private static GuiManagerMG INSTANCE;

    public static GuiManagerMG getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new GuiManagerMG();
        return INSTANCE;
    }

    private GuiManagerMG() {}

    //GETTERS ----------------------------------------------------------------------------------------------------------
    public static GUI getGUI(EnumGUITypes guiType){
        return GUIs.get(guiType);
    }

    public static GUI getGUI(String guiName){
        return GUIsDynamic.get(guiName);
    }


    //METHODS ----------------------------------------------------------------------------------------------------------

    public static <T extends GUI> void addGUI(String name, T GUI){
        if(GUIsDynamic.containsKey(name)) {throw new NameInUseException(name, "GuiManagerMG");}
        GUIsDynamic.put(name, GUI);
    }


    public static void setup(){
        LoggerHelperMG.logInfo("Setting up GuiManager!");
        for (EnumGUITypes type : EnumGUITypes.values()){
            GUIs.put(type, ManagedGui.getISetupHelper().loadDefaultGUI(type));
        }
    }


    private static short tick;
    private static short sec;
    private static short min;

    private static void processTimeEvents(){
        tick ++;

        if(tick%2 == 0)
            postEvent(new TimeEvent(EnumTime.TICK_2));
        if(tick%5 == 0)
            postEvent(new TimeEvent(EnumTime.TICK_5));
        if(tick%10 == 0)
            postEvent(new TimeEvent(EnumTime.TICK_10));
        if(tick%15 == 0)
            postEvent(new TimeEvent(EnumTime.TICK_15));

        if(tick >= 20){
            tick = 0;
            sec ++;

            postEvent(new TimeEvent(EnumTime.SEC_1));

            if(sec%2 == 0)
                postEvent(new TimeEvent(EnumTime.SEC_2));
            if(sec%5 == 0)
                postEvent(new TimeEvent(EnumTime.SEC_5));
            if(sec%10 == 0)
                postEvent(new TimeEvent(EnumTime.SEC_10));
            if(sec%15 == 0)
                postEvent(new TimeEvent(EnumTime.SEC_15));
            if(sec%30 == 0)
                postEvent(new TimeEvent(EnumTime.SEC_30));


            if(sec >= 60) {
                sec = 0;
                min++;

                postEvent(new TimeEvent(EnumTime.MIN_1));

                if (min % 2 == 0)
                    postEvent(new TimeEvent(EnumTime.MIN_2));
                if (min % 2 == 0)
                    postEvent(new TimeEvent(EnumTime.MIN_2));
                if (min % 5 == 0)
                    postEvent(new TimeEvent(EnumTime.MIN_5));
                if (min % 10 == 0)
                    postEvent(new TimeEvent(EnumTime.MIN_10));
                if (min % 15 == 0)
                    postEvent(new TimeEvent(EnumTime.MIN_15));
                if (min % 20 == 0)
                    postEvent(new TimeEvent(EnumTime.MIN_20));
                if (min % 30 == 0)
                    postEvent(new TimeEvent(EnumTime.MIN_30));

                if (min >= 60)
                    min = 0;

            }
        }
    }

    //EVENT_HANDLING ---------------------------------------------------------------------------------------------------

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent e) {
        if(ManagedGui.isIsDisabled()) return;
        processTimeEvents();
        try {
            MouseHandler.onClientTick();

            //LoggerHelperMG.logInfo("--------------------------------------------------------------------------------");

            GUIs.values().forEach(GUI::onClientTick);
            GUIsDynamic.values().forEach(GUI::onClientTick);

        } catch (Exception ex) {
            //TODO print some nice debuging
            ex.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post e) {
        if(ManagedGui.isIsDisabled()) return;
        if(e.type != RenderGameOverlayEvent.ElementType.ALL || e.isCanceled()) return;
        try {

            GUIs.values().forEach(gui -> gui.onRender(0, 0));
            GUIsDynamic.values().forEach(gui -> gui.onRender(0, 0));

        } catch (Exception ex) {
            //TODO print some nice debuging
            ex.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onOpenGui(GuiOpenEvent e) {
        if(ManagedGui.isIsDisabled()) return;
        try {

            GUIs.values().forEach((gui) -> gui.onOpenGui(e));
            GUIsDynamic.values().forEach((gui) -> gui.onOpenGui(e));

        } catch (Exception ex) {
            //TODO print some nice debuging
            ex.printStackTrace();
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onChatMessage(ClientChatReceivedEvent e) {
        if(ManagedGui.isIsDisabled()) return;
        try {

            GUIs.values().forEach((gui) -> gui.onChatMessage(e));
            GUIsDynamic.values().forEach((gui) -> gui.onChatMessage(e));

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static <T extends Event> void postEvent(T e){
        if(ManagedGui.isIsDisabled()) return;
        try {

            GUIs.values().forEach((gui) -> gui.onEventBus(e));
            GUIsDynamic.values().forEach((gui) -> gui.onEventBus(e));

        } catch (Exception ex) {
            //TODO print some nice debuging
            ex.printStackTrace();
        }
    }

    public static void onClick(MouseHandler.ClickType clickType){
        if(ManagedGui.isIsDisabled()) return;
        try {
            GUIs.values().forEach((gui) -> gui.onClick(clickType));
            GUIsDynamic.values().forEach((gui) -> gui.onClick(clickType));
        } catch (Exception ex) {
            //TODO print some nice debuging
            ex.printStackTrace();
        }
    }

    public static void onScroll(int i){
        if(ManagedGui.isIsDisabled()) return;
        try {
            GUIs.values().forEach((gui) -> gui.onScroll(i));
            GUIsDynamic.values().forEach((gui) -> gui.onScroll(i));
        } catch (Exception ex) {
            //TODO print some nice debuging
            ex.printStackTrace();
        }
    }

    public static void onMouseMove(int mX, int mY){
        if(ManagedGui.isIsDisabled()) return;
        try {
            GUIs.values().forEach((gui) -> gui.onMouseMove(mX, mY));
            GUIsDynamic.values().forEach((gui) -> gui.onMouseMove(mX, mY));
        } catch (Exception ex) {
            //TODO print some nice debuging
            ex.printStackTrace();
        }
    }

}
