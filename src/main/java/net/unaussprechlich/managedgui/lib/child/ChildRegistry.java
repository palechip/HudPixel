/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */
package net.unaussprechlich.managedgui.lib.child;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.unaussprechlich.managedgui.lib.event.IDoEventMethods;
import net.unaussprechlich.managedgui.lib.event.bus.IEvent;
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;

import java.util.ArrayList;

public abstract class ChildRegistry implements IDoEventMethods {

    private ArrayList<IChild> childs = new ArrayList<IChild>();

    public void registerChild(IChild child) {
        childs.add(child);
    }

    public void unregisterChild(IChild child) {
        childs.remove(child);
    }

    public void clearChilds() {
        childs.clear();
    }

    public int getChildsSize() {
        return childs.size();
    }

    public boolean containsChild(IChild child) {
        return childs.contains(child);
    }

    public ArrayList<IChild> getChilds() {
        return childs;
    }

    public void onClientTick() {
        if(!doClientTick()) return;
        childs.forEach(IChild::onClientTick);
    }

    public void onRender() {
        if(!doRender()) return;
        childs.forEach(IChild::onRender);
    }

    public void onChatMessage(ClientChatReceivedEvent e) {
        if(!doClientTick()) return;
        childs.forEach(child -> child.onChatMessage(e));
    }

    public void onMouseMove(int mX, int mY) {
        if(!doMouseMove(mX, mY)) return;
        childs.forEach(child -> child.onMouseMove(mX, mY));
    }

    public void onScroll(int i) {
        if(!doScroll(i)) return;
        childs.forEach(child -> child.onScroll(i));
    }

    public void onClick(MouseHandler.ClickType clickType) {
        if(!doClick(clickType)) return;
        childs.forEach(child -> child.onClick(clickType));
    }

    public void onEventBus(IEvent event) {
        if(!doEventBus(event)) return;
        childs.forEach(child -> child.onEventBus(event));
    }

    public void onOpenGui(GuiOpenEvent e){
        if(!doOpenGUI(e)) return;
        childs.forEach(child -> child.onOpenGui(e));
    }
}
