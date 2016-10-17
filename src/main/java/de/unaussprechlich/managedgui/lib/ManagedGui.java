package de.unaussprechlich.managedgui.lib;

import de.unaussprechlich.managedgui.lib.helper.ChildRegistry;
import de.unaussprechlich.managedgui.lib.helper.MouseHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/******************************************************************************

 * Copyright (c) 2016 unaussprechlich

 *******************************************************************************/
public class ManagedGui {

    private static ManagedGui instance;
    private static ChildRegistry childRegistry = new ChildRegistry();

    public static ChildRegistry getChildRegistry() {
        return childRegistry;
    }

    public static ManagedGui Instance() {
        if (instance == null) instance = new ManagedGui();
        return instance;
    }

    private ManagedGui() {
        setup();
    }

    private void setup() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent e) {
        try {
            MouseHandler.onClientTick();
            childRegistry.onClientTick();
        } catch (Exception ex) {
            //TODO print some nice debuging
            ex.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post e) {
        try {
            childRegistry.onRender();
        } catch (Exception ex) {
            //TODO print some nice debuging
            ex.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onOpenGui(GuiOpenEvent e) {
        try {


        } catch (Exception ex) {
            //TODO print some nice debuging
            ex.printStackTrace();
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onChatMessage(ClientChatReceivedEvent e) {
        try {
            childRegistry.onChatMessage(e);

        } catch (Exception ex) {
            //TODO print some nice debuging
            ex.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
