/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */
package net.unaussprechlich.managedgui.lib.gui;

import net.minecraft.client.Minecraft;
import net.unaussprechlich.managedgui.lib.child.ChildRegistry;
import net.unaussprechlich.managedgui.lib.child.IChild;

public abstract class GUI extends ChildRegistry implements IChild {

    private int xStart = 0;
    private int yStart = 0;

    public void setXStart(int xStart) {
        this.xStart = xStart;
    }

    public void setYStart(int yStart) {
        this.yStart = yStart;
    }

    protected static boolean isInGameGUIShown() {
        return Minecraft.getMinecraft() != null && Minecraft.getMinecraft().inGameHasFocus;
    }

    protected static boolean isChatGUIShown(){
        return true;
    }

    @Override
    public int getXStart() {
        return xStart;
    }

    @Override
    public int getYStart() {
        return yStart;
    }
}
