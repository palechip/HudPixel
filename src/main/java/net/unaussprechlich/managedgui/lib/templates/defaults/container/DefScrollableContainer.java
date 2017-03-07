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
import net.unaussprechlich.managedgui.lib.ConstantsMG;
import net.unaussprechlich.managedgui.lib.container.Container;
import net.unaussprechlich.managedgui.lib.container.ContainerFrame;
import net.unaussprechlich.managedgui.lib.event.util.Event;
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;
import net.unaussprechlich.managedgui.lib.util.ColorRGBA;
import net.unaussprechlich.managedgui.lib.util.EnumEventState;
import net.unaussprechlich.managedgui.lib.util.RGBA;
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

    public ArrayList<Container> getScrollElements() {
        return scrollElements;
    }

    private int pixelSize = 0;
    private int pixelPos  = 0;

    private ArrayList<Container> scrollElements  = new ArrayList<>();
    private ArrayList<Integer>   spacerPositions = new ArrayList<>();


    public DefScrollableContainer(ColorRGBA color, int width, int height, IScrollSpacerRenderer spacer) {
        super(width, height, 100, 50, color);
        this.spacer = spacer;
        this.spacerHeight = spacer.getSpacerHeight();
    }

    public void registerScrollElement(Container container){
        if((scrollElements.size() + 1) > MAX_STORED){
            unregisterScrollElement(scrollElements.get(scrollElements.size()-1));
        }
        if(container instanceof DefChatMessageContainer){
            ((DefChatMessageContainer) container).setHeightCallback(data -> updateWithoutAnimation());
        }
        registerChild(container);
        scrollElements.add(container);
        updateWithoutAnimation();
    }

    public void unregisterScrollElement(Container container){
        unregisterChild(container);
        scrollElements.remove(container);
        updateWithoutAnimation();
    }

    private int getMaxScrollIndex(){
        if(PIXEL_PER_INDEX == 0) return 0;
        return (pixelSize - getHeight()) / PIXEL_PER_INDEX;
    }

    private int getScrollBarPosY(){
        int scrollHeight = getHeight() - 45;
        if(getMaxScrollIndex() != 0)
            return getYStart() + 5 + (scrollHeight - Math.round((scrollHeight) * (((float)indexScroll)/((float)(pixelSize - getHeight()) / (float)PIXEL_PER_INDEX))));
        else return getYStart() + 5 + getHeight() - 20;
    }


    @Override
    protected boolean doClientTickLocal() {
        scrollAnimation();

        if(isScrollByBar){
            float i = getHeight() - 50;
            int mY = MouseHandler.INSTANCE.getMY()  - getYStart() - 25;
            if(mY < 0) pixelPos = pixelSize - getHeight();
            else if(mY > i) pixelPos = 0;
            else pixelPos = (Math.round(Math.abs(mY - i ) * ((float)(pixelSize - getHeight()) / i)));
            updateWithoutAnimation();
        }


        return true;
    }

    @Override
    protected boolean doRenderTickLocal(int xStart, int yStart, int width, int height, EnumEventState ees) {

        if(ees.equals(EnumEventState.PRE)){
            RenderUtils.renderBoxWithColor(xStart- 5, yStart - 5, width + 100, height + 100, getBackgroundRGBA());
            spacerPositions.forEach(y -> spacer.render(xStart, yStart+y, width));
        }

        if(ees.equals(EnumEventState.POST)){
            RenderUtils.renderBoxWithColor(xStart, yStart - 1, width, 7, getBackgroundRGBA());
            RenderUtils.rect_fade_horizontal_s1_d1(xStart, yStart + 6, width, 30, getBackgroundRGBA() ,
                                                   new ColorRGBA(getBackgroundRGBA().getRed(), getBackgroundRGBA().getGreen(), getBackgroundRGBA().getBlue(), 0));

            if(pixelSize < height) return true;

            int scroll = Math.round(Math.abs(pixelPos -(pixelSize - height)) * ((float)(height - 50)/(float)(pixelSize-height)));

            RenderUtils.renderRectWithInlineShadow_s1_d1(xStart + width - 10, yStart + 10, height - 20, 4, RGBA.BLACK_LIGHT.get(), ConstantsMG.DEF_BACKGROUND_RGBA, 2);
            RenderUtils.renderRectWithInlineShadow_s1_d1(xStart + width - 11, yStart + scroll + 10, 30, 6, RGBA.P1B1_596068.get(), RGBA.NULL.get(), 2);
        }
        return true;
    }

    private int getPixelPosFromIndex(){
        return PIXEL_PER_INDEX * indexScroll;
    }

    private int getIndexFromPixelPos(){
        return pixelPos / PIXEL_PER_INDEX;
    }

    @Override
    protected boolean doChatMessageLocal(ClientChatReceivedEvent e) {
        return true;
    }

    private boolean isScrollByBar = false;
    @Override
    protected boolean doClickLocal(MouseHandler.ClickType clickType, boolean isThisContainer) {
        if(clickType.equals(MouseHandler.ClickType.DRAG)){
            if(this.checkIfMouseOver(getXStart() + getWidth() - 11, getScrollBarPosY(), 6, 30)){
                isScrollByBar = true;
            }
        }

        if(clickType.equals(MouseHandler.ClickType.DROP)){
            if(isScrollByBar){
                isScrollByBar = false;
            }
        }
        return true;

    }

    @Override
    protected boolean doScrollLocal(int i, boolean isThisContainer) {
        if(!isThisContainer) return true;

        if (i != 0) {
            if (i > 0) {
                if((indexScroll + 1) * PIXEL_PER_INDEX > pixelSize - getHeight())
                    return true;
                indexScroll++;
                isScollUP = true;
            } else {
                if(indexScroll - 1 < 0)
                    return true;
                indexScroll--;
                isScollUP = false;
            }
            updateWithAnimation();
        }
        return true;
    }



    private void updateWithAnimation(){
        scrollAnimated = PIXEL_PER_INDEX;
    }
    private void updateWithoutAnimation(){
        spacerPositions.clear();
        pixelSize = scrollElements.stream().mapToInt(Container::getHeight).sum() + scrollElements.size() * spacerHeight;
        indexScroll = getIndexFromPixelPos();

        int offset = 0;

        for(Container con :  scrollElements){
            con.setYOffset(pixelPos - offset + getHeight() - pixelSize - scrollAnimated);
            spacerPositions.add(pixelPos - offset + con.getHeight() + getHeight() - pixelSize - scrollAnimated);
            offset -= con.getHeight() + spacerHeight;
        }
    }

    private boolean isScollUP = false;
    private int scrollAnimated = PIXEL_PER_INDEX;
    private void scrollAnimation(){

        if(scrollAnimated <= 0) return;
        scrollAnimated -= 1;
        spacerPositions.clear();
        pixelSize = scrollElements.stream().mapToInt(Container::getHeight).sum() + scrollElements.size() * spacerHeight;
        pixelPos = getPixelPosFromIndex();

        int offset = 0;

        if(isScollUP)
            for(Container con :  scrollElements){
                con.setYOffset(getPixelPosFromIndex() - offset + getHeight() - pixelSize - scrollAnimated);
                spacerPositions.add(indexScroll * PIXEL_PER_INDEX - offset + con.getHeight() + getHeight() - pixelSize - scrollAnimated);
                offset -= con.getHeight() + spacerHeight;
            }
        else
            for(Container con :  scrollElements){
                con.setYOffset(getPixelPosFromIndex() - offset + getHeight() - pixelSize + scrollAnimated);
                spacerPositions.add(indexScroll * PIXEL_PER_INDEX - offset + con.getHeight() + getHeight() - pixelSize + scrollAnimated);
                offset -= con.getHeight() + spacerHeight;
            }
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

    @Override
    protected boolean doResizeLocal(int width, int height) {
        scrollElements.forEach(container -> container.setWidth(getWidth()));
        indexScroll = 0;
        updateWithoutAnimation();
        return true;
    }
}
