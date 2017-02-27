/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.templates.defaults.container;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.unaussprechlich.managedgui.lib.container.Container;
import net.unaussprechlich.managedgui.lib.container.ContainerFrame;
import net.unaussprechlich.managedgui.lib.event.util.Event;
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;
import net.unaussprechlich.managedgui.lib.util.ColorRGBA;
import net.unaussprechlich.managedgui.lib.util.EnumEventState;
import net.unaussprechlich.managedgui.lib.util.RenderUtils;

import java.util.ArrayList;

/**
 * DefScrollableContainer Created by Alexander on 26.02.2017.
 * Description:
 **/
public class DefScrollableContainer extends ContainerFrame {

    private int indexScroll;

    private static final int PIXEL_PER_INDEX = 15;
    private static final int MAX_STORED = 50;

    private final IScrollSpacerRenderer spacer;
    private final int spacerHeight;

    private int pixelSize = 0;

    private ArrayList<Container> scrollElements  = new ArrayList<>();
    private ArrayList<Integer>   spacerPositions = new ArrayList<>();


    public DefScrollableContainer(ColorRGBA color, int width, int height, IScrollSpacerRenderer spacer) {
        super(width, height, color);
        this.spacer = spacer;
        this.spacerHeight = spacer.getSpacerHeight();
    }

    public void registerScrollElement(Container container){
        if((scrollElements.size() + 1) > MAX_STORED){
            unregisterScrollElement(scrollElements.get(scrollElements.size()-1));
        }
        registerChild(container);
        scrollElements.add(container);
        update();
    }

    public void unregisterScrollElement(Container container){
        unregisterChild(container);
        scrollElements.remove(container);
        update();
    }


    @Override
    protected boolean doClientTickLocal() {
        scrollAnimation();
        return true;
    }

    @Override
    protected boolean doRenderTickLocal(int xStart, int yStart, int width, int height, EnumEventState ees) {

        if(ees.equals(EnumEventState.PRE)){
            RenderUtils.renderBoxWithColor(xStart, yStart, width, height, getBackgroundRGBA());
            spacerPositions.forEach(y -> spacer.render(xStart, yStart+y, width));
        }

        if(ees.equals(EnumEventState.POST)){
            RenderUtils.renderBoxWithColor(xStart, yStart, width, 7, getBackgroundRGBA());
            RenderUtils.renderRectWithColorFadeHorizontal(xStart, yStart + 7, width, 30, getBackgroundRGBA() ,
                                                          new ColorRGBA(getBackgroundRGBA().getRED(), getBackgroundRGBA().getGREEN(), getBackgroundRGBA().getBLUE(), 0));
        }

        return true;
    }

    @Override
    protected boolean doChatMessageLocal(ClientChatReceivedEvent e) {
        return true;
    }

    @Override
    protected boolean doClickLocal(MouseHandler.ClickType clickType, boolean isThisContainer) {
        return true;
    }

    @Override
    protected boolean doScrollLocal(int i, boolean isThisContainer) {
        if(!isThisContainer) return true;

        if (i != 0) {
            if (i < 0) {
                indexScroll++;
            } else {
                indexScroll--;
            }
            update();
        }

        return true;
    }



    private void update(){
        //scrollAnimated = PIXEL_PER_INDEX;
        spacerPositions.clear();
        pixelSize = 0;

        for(Container con :  scrollElements){
            con.setYOffset(pixelSize - (indexScroll * PIXEL_PER_INDEX));
            spacerPositions.add(pixelSize - (indexScroll * PIXEL_PER_INDEX) + con.getHeight());
            pixelSize += con.getHeight() + spacerHeight;
        }
    }

    private int scrollAnimated = PIXEL_PER_INDEX;
    private void scrollAnimation(){
        return;
        /*
        if(scrollAnimated == 0) return;
        scrollAnimated--;
        spacerPositions.clear();

        for(Container con :  scrollElements){
            con.setYOffset(pixelSize - (indexScroll * PIXEL_PER_INDEX) - scrollAnimated );
            spacerPositions.add(pixelSize - (indexScroll * PIXEL_PER_INDEX) + con.getHeight() - scrollAnimated);
            pixelSize += con.getHeight() + spacerHeight;
        }*/
    }


    @Override
    protected boolean doMouseMoveLocal(int mX, int mY) {
        return true;
    }

    @Override
    protected <T extends Event> boolean doEventBusLocal(T iEvent) {
        return true;
    }

    @Override
    protected boolean doOpenGUILocal(GuiOpenEvent e) {
        return true;
    }
}
