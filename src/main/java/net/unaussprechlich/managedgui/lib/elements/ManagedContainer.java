/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */
package net.unaussprechlich.managedgui.lib.elements;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.unaussprechlich.managedgui.lib.child.ChildRegistry;
import net.unaussprechlich.managedgui.lib.child.IChild;
import net.unaussprechlich.managedgui.lib.event.bus.IEvent;
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;
import net.unaussprechlich.managedgui.lib.util.ColorRGBA;
import net.unaussprechlich.managedgui.lib.util.EnumEventState;
import net.unaussprechlich.managedgui.lib.util.RenderHelper;
import net.unaussprechlich.managedgui.lib.util.storage.Matrix4Side;

public abstract class ManagedContainer extends ChildRegistry implements IChild {

    public Matrix4Side padding = new Matrix4Side();
    public Matrix4Side margin  = new Matrix4Side();
    public Matrix4Side border  = new Matrix4Side();

    public short height;
    public short width;
    public short xStart;
    public short yStart;
    public short xStartInner;
    public short yStartInner;

    public ColorRGBA borderRGBA;
    public ColorRGBA backgroundRGBA;

    public ResourceLocation backgroundImage;

    abstract boolean onClientTickLocal();

    abstract boolean onRenderTickLocal(
            int xStart,
            int yStart,
            int width,
            int height,
            EnumEventState ees
    );

    abstract boolean doChatMessageLocal(
            ClientChatReceivedEvent e
    );

    abstract boolean doClickLocal(
            MouseHandler.ClickType clickType,
            boolean isThisContainer
    );

    abstract boolean doScrollLocal(
            int i,
            boolean isThisContainer
    );

    abstract boolean doMouseMoveLocal(
            int mX,
            int mY
    );

    abstract boolean doEventBusLocal(
            IEvent iEvent
    );

    abstract boolean doOpenGUILocal(
            GuiOpenEvent e
    );

    private boolean checkIfMouseOverContainer(int mX, int mY) {
        return (mX > xStart + margin.LEFT && mX < xStartInner + padding.RIGHT + border.RIGHT + width
                && mY > yStart + margin.TOP && mY < yStartInner + padding.BOTTOM + border.BOTTOM + height);
    }

    @Override
    public boolean doClientTick() {
        if (!onClientTickLocal()) return false;
        xStartInner = (short) (xStart + padding.LEFT + border.LEFT + margin.LEFT);
        yStartInner = (short) (yStart + padding.TOP + border.TOP + margin.TOP);
        return true;
    }



    @Override
    public boolean doRender(){
        if (!onRenderTickLocal(xStartInner, yStartInner, width, height, EnumEventState.PRE)) return false;
        RenderHelper.renderContainer(this);
        if (!onRenderTickLocal(xStartInner, yStartInner, width, height, EnumEventState.POST)) return false;
        return true;
    }

    @Override
    public boolean doChatMessage(ClientChatReceivedEvent e) {
        return doChatMessageLocal(e);
    }

    @Override
    public boolean doClick(MouseHandler.ClickType clickType) {
        boolean isThisContainer = checkIfMouseOverContainer(MouseHandler.getmX(), MouseHandler.getmY());
        return doClickLocal(clickType, isThisContainer);
    }

    @Override
    public boolean doScroll(int i) {
        boolean isThisContainer = checkIfMouseOverContainer(MouseHandler.getmX(), MouseHandler.getmY());
        return doScrollLocal(i, isThisContainer);
    }

    @Override
    public boolean doMouseMove(int mX, int mY) {
        return doMouseMoveLocal(mX, mY);
    }

    @Override
    public boolean doEventBus(IEvent event) {
        return doEventBusLocal(event);
    }

    @Override
    public boolean doOpenGUI(GuiOpenEvent e) {
        return doOpenGUILocal(e);
    }
}
