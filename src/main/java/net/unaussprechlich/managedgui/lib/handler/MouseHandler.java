/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.unaussprechlich.managedgui.lib.GuiManagerMG;
import org.lwjgl.input.Mouse;

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
        @SuppressWarnings("LocalVariableDeclarationSideOnly") Minecraft mc = Minecraft.getMinecraft();
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
            GuiManagerMG.onMouseMove(mX, mY);
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
                GuiManagerMG.onClick(ClickType.SINGLE);
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
                GuiManagerMG.onClick(ClickType.DOUBLE);
            }
        }
    }

    private static void handleMouseScroll() {

        GuiManagerMG.onScroll(Mouse.getDWheel());
    }
}
