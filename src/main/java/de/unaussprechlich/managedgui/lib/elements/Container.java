package de.unaussprechlich.managedgui.lib.elements;

import de.unaussprechlich.managedgui.lib.helper.Child;
import de.unaussprechlich.managedgui.lib.helper.MouseHandler;
import de.unaussprechlich.managedgui.lib.helper.RenderHelper;
import de.unaussprechlich.managedgui.lib.util.ColorRGBA;
import de.unaussprechlich.managedgui.lib.util.storage.StorageFourSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

/******************************************************************************

 * Copyright (c) 2016 unaussprechlich

 *******************************************************************************/
public abstract class Container implements Child {

    public String name;

    public StorageFourSide padding = new StorageFourSide();
    public StorageFourSide margin = new StorageFourSide();
    public StorageFourSide border = new StorageFourSide();

    public short height;
    public short width;
    public short xStart;
    public short yStart;
    public short xStartInner;
    public short yStartInner;
    public ColorRGBA borderRGBA = new ColorRGBA();
    public ColorRGBA backgroundRGBA = new ColorRGBA();

    public ResourceLocation backgroundImage;

    abstract boolean onClientTickLocal();

    abstract boolean onRenderTickLocal(short xStart, short yStart, short width, short height);

    abstract boolean onChatMessageLocal(ClientChatReceivedEvent e);

    abstract boolean onClickLocal(MouseHandler.ClickType clickType, boolean isThisContainer);

    abstract boolean onScrollLocal(int i, boolean isThisContainer);

    abstract boolean onMouseMoveLocal(int mX, int mY);

    //CONSTRUCTOR
    public Container(String name) {

    }

    private boolean checkIfMouseOverContainer(int mX, int mY) {
        return (mX > xStart + margin.LEFT && mX < xStartInner + padding.RIGHT + border.RIGHT + width
                && mY > yStart + margin.TOP && mY < yStartInner + padding.BOTTOM + border.BOTTOM + height);
    }

    @Override
    public void onClientTick() {
        if (!onClientTickLocal()) return;
        xStartInner = (short) (xStart + padding.LEFT + border.LEFT + margin.LEFT);
        yStartInner = (short) (yStart + padding.TOP + border.TOP + margin.TOP);
    }

    @Override
    public void onRenderTick() {
        if (!onRenderTickLocal(xStartInner, yStartInner, width, height)) return;
        RenderHelper.renderContainer(this);
    }

    @Override
    public void onChatMessage(ClientChatReceivedEvent e) {
        if (!onChatMessageLocal(e)) return;
    }

    @Override
    public void onClick(MouseHandler.ClickType clickType) {
        boolean isThisContainer = checkIfMouseOverContainer(MouseHandler.getmX(), MouseHandler.getmY());
        if (!onClickLocal(clickType, isThisContainer)) return;
    }

    @Override
    public void onScroll(int i) {
        boolean isThisContainer = checkIfMouseOverContainer(MouseHandler.getmX(), MouseHandler.getmY());
        if (!onScrollLocal(i, isThisContainer)) return;
    }

    @Override
    public void onMouseMove(int mX, int mY) {
        if (!onMouseMoveLocal(mX, mY)) return;
    }
}
