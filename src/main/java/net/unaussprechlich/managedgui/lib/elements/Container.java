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
import net.unaussprechlich.managedgui.lib.event.events.Event;
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;
import net.unaussprechlich.managedgui.lib.util.ColorRGBA;
import net.unaussprechlich.managedgui.lib.util.EnumEventState;
import net.unaussprechlich.managedgui.lib.util.EnumRGBA;
import net.unaussprechlich.managedgui.lib.util.RenderHelper;
import net.unaussprechlich.managedgui.lib.util.storage.ContainerSide;

public abstract class Container extends ChildRegistry implements IContainer, IChild {

    private int height = 0;
    private int width  = 0;
    private int xStart = 0;
    private int yStart = 0;

    private int xOffset = 0;
    private int yOffset = 0;

    private boolean visible = true;

    private ContainerSide border = new ContainerSide();
    private ContainerSide margin = new ContainerSide();
    private ContainerSide padding = new ContainerSide();

    private ColorRGBA borderRGBA = EnumRGBA.TRANSPARENT.get();
    private ColorRGBA backgroundRGBA = EnumRGBA.TRANSPARENT.get();

    private ResourceLocation backgroundImage = null;

    //ABSTRACT STUFF ---------------------------------------------------------------------------------------------------

    protected abstract boolean doClientTickLocal();

    protected abstract boolean doRenderTickLocal(
            int xStart,
            int yStart,
            int width,
            int height,
            EnumEventState ees
    );

    protected abstract boolean doChatMessageLocal(
            ClientChatReceivedEvent e
    );

    protected abstract boolean doClickLocal(
            MouseHandler.ClickType clickType,
            boolean isThisContainer
    );

    protected abstract boolean doScrollLocal(
            int i,
            boolean isThisContainer
    );

    protected abstract boolean doMouseMoveLocal(
            int mX,
            int mY
    );

    protected abstract <T extends Event> boolean doEventBusLocal(
            T iEvent
    );

    protected abstract boolean doOpenGUILocal(
            GuiOpenEvent e
    );

    //METHODS ----------------------------------------------------------------------------------------------------------

    private boolean checkIfMouseOverContainer(int mX, int mY) {
        return (mX > getXStartBorder() && mX < xStart + padding.RIGHT() + border.RIGHT() + width
                && mY > getYStartBorder() && mY < yStart + padding.BOTTOM() + border.BOTTOM() + height);
    }

    //EVENT STUFF ------------------------------------------------------------------------------------------------------

    @Override
    public boolean doClientTick() {
        //LoggerHelperMG.logInfo("Test: Container.doClientTick()");
        return doClientTickLocal();
    }



    @Override
    public boolean doRender(int xStart, int yStart){
        this.xStart = xStart + xOffset + padding.LEFT() + margin.LEFT() + border.LEFT();
        this.yStart = yStart + yOffset + padding.TOP()  + margin.TOP()  + border.LEFT();

        if(!visible) return false;

        if (!doRenderTickLocal(this.xStart, this.yStart, width, height, EnumEventState.PRE)) return false;
        RenderHelper.renderContainer(this);
        if (!doRenderTickLocal(this.xStart, this.yStart, width, height, EnumEventState.POST)) return false;
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
    public <T extends Event> boolean doEventBus(T event) {
        return doEventBusLocal(event);
    }

    @Override
    public boolean doOpenGUI(GuiOpenEvent e) {
        return doOpenGUILocal(e);
    }


    //GETTER -----------------------------------------------------------------------------------------------------------

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getWidthMargin() {
        return getWidthBorder() + margin.LEFT() + margin.RIGHT();
    }

    @Override
    public int getHeightMargin() {
        return getHeightBorder() + margin.TOP() + margin.LEFT();
    }

    @Override
    public int getWidthBorder() {
        return getWidthPadding() + border.LEFT() + border.RIGHT();
    }

    @Override
    public int getHeightBorder() {
        return getHeightPadding() + border.TOP() + border.BOTTOM();
    }

    @Override
    public int getWidthPadding() {
        return width + padding.LEFT() + padding.RIGHT();
    }

    @Override
    public int getHeightPadding() {
        return height + padding.TOP() + padding.BOTTOM();
    }

    @Override
    public int getXOffset() {
        return xOffset;
    }

    @Override
    public int getYOffset() {
        return yOffset;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public int getYStart() {
        return yStart;
    }

    @Override
    public int getXStart() {
        return xStart;
    }

    @Override
    public int getYStartMargin() {
        return getYStartBorder() - margin.TOP();
    }

    @Override
    public int getXStartMargin() {
        return getXStartBorder() - margin.LEFT();
    }

    @Override
    public int getYStartPadding() {
        return yStart - padding.TOP();
    }

    @Override
    public int getXStartPadding() {
        return xStart - padding.LEFT();
    }

    @Override
    public int getYStartBorder() {
        return getYStartPadding() - border.TOP();
    }

    @Override
    public int getXStartBorder() {
        return getXStartPadding() - border.LEFT();
    }

    @Override
    public ColorRGBA getBorderRGBA() {
        return borderRGBA;
    }

    @Override
    public ColorRGBA getBackgroundRGBA() {
        return backgroundRGBA;
    }

    @Override
    public ResourceLocation getBackgroundImage() {
        return backgroundImage;
    }

    @Override
    public ContainerSide getPadding() {
        return padding;
    }

    @Override
    public ContainerSide getMargin() {
        return margin;
    }

    @Override
    public ContainerSide getBorder() {
        return border;
    }

    //SETTER -----------------------------------------------------------------------------------------------------------

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void setYOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    @Override
    public void setXOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    @Override
    public void setBorderRGBA(ColorRGBA borderRGBA) {
        this.borderRGBA = borderRGBA;
    }

    @Override
    public void setBackgroundRGBA(ColorRGBA backgroundRGBA) {
        this.backgroundRGBA = backgroundRGBA;
    }

    @Override
    public void setBackgroundImage(ResourceLocation backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    @Override
    public void setPadding(ContainerSide padding) {
        this.padding = padding;
    }

    @Override
    public void setMargin(ContainerSide margin) {
        this.margin = margin;
    }

    @Override
    public void setBorder(ContainerSide border) {
        this.border = border;
    }
}
