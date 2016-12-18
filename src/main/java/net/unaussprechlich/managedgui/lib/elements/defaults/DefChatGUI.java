/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.elements.defaults;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.unaussprechlich.managedgui.lib.elements.GUI;
import net.unaussprechlich.managedgui.lib.event.bus.IEvent;
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;

/**
 * DefChatGUI Created by unaussprechlich on 18.12.2016.
 * Description:
 **/
public class DefChatGUI extends GUI {


    @Override
    public boolean doClientTick() {
        return Minecraft.getMinecraft().currentScreen instanceof GuiChat;
    }

    @Override
    public boolean doRender() {
        return Minecraft.getMinecraft().currentScreen instanceof GuiChat;
    }

    @Override
    public boolean doChatMessage(ClientChatReceivedEvent e) {
        return Minecraft.getMinecraft().currentScreen instanceof GuiChat;
    }

    @Override
    public boolean doMouseMove(int mX, int mY) {
        return Minecraft.getMinecraft().currentScreen instanceof GuiChat;
    }

    @Override
    public boolean doScroll(int i) {
        return Minecraft.getMinecraft().currentScreen instanceof GuiChat;
    }

    @Override
    public boolean doClick(MouseHandler.ClickType clickType) {
        return Minecraft.getMinecraft().currentScreen instanceof GuiChat;
    }

    @Override
    public boolean doEventBus(IEvent event) {
        return Minecraft.getMinecraft().currentScreen instanceof GuiChat;
    }

    @Override
    public boolean doOpenGUI(GuiOpenEvent e) {
        return Minecraft.getMinecraft().currentScreen instanceof GuiChat;
    }
}
