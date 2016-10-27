package de.unaussprechlich.managedgui.lib.helper;

import de.unaussprechlich.managedgui.lib.ManagedGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import org.lwjgl.input.Mouse;

/******************************************************************************

 * Copyright (c) 2016 unaussprechlich

 *******************************************************************************/
public class MouseHandler {

    private static int scale;
    private static int mX;
    private static int mY;

    public static int getMcScale() {
        return scale;
    }

    public static int getmX() {
        return mX;
    }

    public static int getmY() {
        return mY;
    }

    public enum ClickType {
        SINGLE, DOUBLE
    }

    public static void onClientTick() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.gameSettings.guiScale == 0) {
            scale = new ScaledResolution(mc).getScaleFactor();
        } else {
            scale = mc.gameSettings.guiScale;
        }
        int newmX = Mouse.getX() / scale;
        int newmY = (mc.displayHeight - Mouse.getY()) / scale;

        if (newmX != mX || newmY != mY) {
            mX = newmX;
            mY = newmY;
            ManagedGui.getChildRegistry().onMouseMove(mX, mY);
        }

        if (!(mc.currentScreen instanceof GuiIngameMenu || mc.currentScreen instanceof GuiChat || mc.currentScreen instanceof GuiInventory))
            return;
        handleMouseClick();
        handleMouseScroll();

    }

    private static final long clickDelay = 1000;
    private static long lastTimeClicked;
    private static boolean doubleClick = false;

    private static void handleMouseClick() {

        if (System.currentTimeMillis() > (lastTimeClicked + clickDelay)) {
            if (doubleClick) {
                ManagedGui.getChildRegistry().onClick(ClickType.SINGLE);
            }
            if (Mouse.isButtonDown(0))
                lastTimeClicked = System.currentTimeMillis();

            doubleClick = false;

        } else if (System.currentTimeMillis() < (lastTimeClicked + clickDelay)) {
            if (!Mouse.isButtonDown(0) && !doubleClick) {
                doubleClick = true;
                return;
            }

            if (Mouse.isButtonDown(0) && doubleClick) {
                doubleClick = false;
                ManagedGui.getChildRegistry().onClick(ClickType.DOUBLE);
            }
        }
    }

    private static void handleMouseScroll() {

        ManagedGui.getChildRegistry().onScroll(Mouse.getDWheel());
    }
}
