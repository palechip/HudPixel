package net.unaussprechlich.managedgui.lib.elements;

import net.unaussprechlich.managedgui.lib.helper.Child;
import net.unaussprechlich.managedgui.lib.helper.ChildRegistry;
import net.unaussprechlich.managedgui.lib.helper.MouseHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

/* *****************************************************************************

 * Copyright (c) 2016 unaussprechlich

 *******************************************************************************/
public class Screen implements Child {

    private ChildRegistry childRegistry = new ChildRegistry();
    private GuiScreen guiScreen;
    private String id;

    public Screen(String id, GuiScreen guiScreen) {
        this.id = id;
        this.guiScreen = guiScreen;
    }

    private boolean checkDisplayType() {
        return Minecraft.getMinecraft().currentScreen.getClass().equals(guiScreen.getClass());
    }

    @Override
    public void onClientTick() {
        if (!checkDisplayType()) return;
        childRegistry.onClientTick();
    }

    @Override
    public void onRenderTick() {
        if (!checkDisplayType()) return;
        childRegistry.onRender();
    }

    @Override
    public void onChatMessage(ClientChatReceivedEvent e) {
        if (!checkDisplayType()) return;
        childRegistry.onChatMessage(e);
    }

    @Override
    public void onClick(MouseHandler.ClickType clickType) {
        if (!checkDisplayType()) return;
        childRegistry.onClick(clickType);
    }

    @Override
    public void onScroll(int i) {
        if (!checkDisplayType()) return;
        childRegistry.onScroll(i);
    }

    @Override
    public void onMouseMove(int mX, int mY) {
        if (!checkDisplayType()) return;
        childRegistry.onMouseMove(mX, mY);
    }
}
