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
import net.unaussprechlich.managedgui.lib.event.bus.IEvent;
import net.unaussprechlich.managedgui.lib.exceptions.NameInUseException;
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;

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

    public static void addGUI(String name, GUI GUI){
        if(GUIsDynamic.containsKey(name)) {throw new NameInUseException(name, "GuiManagerMG");}
    }


    public static void setup(){
        for (EnumGUITypes type : EnumGUITypes.values()){
            GUIs.put(type, ManagedGui.getISetupHelper().loadDefaultGUI(type));
        }
    }

    //EVENT_HANDLING ---------------------------------------------------------------------------------------------------

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent e) {
        if(ManagedGui.isIsDisabled()) return;
        try {
            MouseHandler.onClientTick();

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

            GUIs.values().forEach(GUI::onRender);
            GUIsDynamic.values().forEach(GUI::onRender);

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

    public static void postEvent(IEvent e){
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
