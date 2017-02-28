/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.handler;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public interface IEvents {

    default void onClientTick() {
    }

    default void everyTenTICKS(){

    }

    default void everySEC(){

    }

    default void everyFiveSEC(){

    }

    default void everyMIN(){

    }

    default void onChatReceived(ClientChatReceivedEvent e) throws Throwable {
    }

    default void onChatReceivedMessage(ClientChatReceivedEvent e, final String message) throws Throwable {
    }

    default void onRender() {
    }

    default void handleMouseInput(int i, int mX, int mY) {
    }

    default void onMouseClick(int mX, int mY) {
    }

    default void openGUI(GuiScreen guiScreen) {
    }

    default void onConfigChanged() {
    }


    default void onRenderPlayer(RenderPlayerEvent.Pre e){

    }

    default void onRenderWorld(RenderWorldLastEvent e){

    }
}

