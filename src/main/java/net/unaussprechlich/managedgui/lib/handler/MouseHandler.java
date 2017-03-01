/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.handler;

import com.palechip.hudpixelmod.extended.HudPixelExtendedEventHandler;
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
        SINGLE, DOUBLE, DRAG, DROP
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
            GuiManagerMG.onMouseMove(mX, mY);
        }

        if (!(mc.currentScreen instanceof GuiIngameMenu || mc.currentScreen instanceof GuiChat || mc.currentScreen instanceof GuiInventory))
            return;
        handleMouseClick();
        handleMouseScroll();

    }

    private static final long clickDelay = 150;
    private static long lastTimeClicked;
    private static boolean mouseButtonDown = false;
    private static boolean doubleClick = false;

    private static void handleMouseClick() {

        boolean isDown = Mouse.isButtonDown(0);

        if(!mouseButtonDown && isDown){
            GuiManagerMG.onClick(ClickType.DRAG);
        }


        if(mouseButtonDown && !isDown){
            mouseButtonDown = false;
            doubleClick = false;
            GuiManagerMG.onClick(ClickType.DROP);
        }

        if((System.currentTimeMillis() + clickDelay) < lastTimeClicked){
            if(!mouseButtonDown && isDown){
                mouseButtonDown = true;
                GuiManagerMG.onClick(ClickType.DOUBLE);
            }
        }

        if(!mouseButtonDown && isDown && !doubleClick){
            mouseButtonDown = true;
            GuiManagerMG.onClick(ClickType.SINGLE);
            lastTimeClicked = System.currentTimeMillis();
        }


    }

    private static void handleMouseScroll() {
        Mouse.poll();
        int i = Mouse.getDWheel();

        if(i != 0){
            GuiManagerMG.onScroll(i);

            //TODO: FIX THAT
            HudPixelExtendedEventHandler.handleMouseScroll(i);

        }
    }
}
