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
import net.unaussprechlich.managedgui.lib.event.EnumDefaultEvents;
import net.unaussprechlich.managedgui.lib.event.util.EnumTime;
import net.unaussprechlich.managedgui.lib.event.util.Event;
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;
import net.unaussprechlich.managedgui.lib.util.ColorRGBA;
import net.unaussprechlich.managedgui.lib.util.EnumEventState;
import net.unaussprechlich.managedgui.lib.util.RenderUtils;

/**
 * DefNotificationContainer Created by unaussprechlich on 30.12.2016.
 * Description:
 **/
public class DefNotificationContainer extends DefBackgroundContainer{

    private String message;
    private String title;
    private ColorRGBA color;

    private int showtime_sec;

    private DefTextContainer titleContainer;
    private DefTextAutoLineBreakContainer messageContainer;


    public DefNotificationContainer(String message, String title, ColorRGBA color, int showtime_sec) {
        super(ConstantsMG.DEF_BACKGROUND_RGBA, 400, 100);
        this.message = message;
        this.title = title;
        this.color = color;
        this.showtime_sec = showtime_sec;

        titleContainer = new DefTextContainer(this.title);
        messageContainer = new DefTextAutoLineBreakContainer(this.message, getWidth() - 10);

        titleContainer.setXOffset(6);
        titleContainer.setYOffset(3);

        messageContainer.setXOffset(6);
        messageContainer.setYOffset(4 + titleContainer.getHeight());

        updateHeight();

        registerChild(titleContainer);
        registerChild(messageContainer);
    }

    public int getShowtime_sec() {
        return showtime_sec;
    }

    public void setShowtime_sec(int showtime_sec) {
        this.showtime_sec = showtime_sec;
    }

    private void updateHeight(){
        setHeight(messageContainer.getHeight() + titleContainer.getHeight() + 5);
    }

    @Override
    protected boolean doClientTickLocal() {
        return true;
    }

    @Override
    protected boolean doRenderTickLocal(int xStart, int yStart, int width, int height, EnumEventState ees) {
        if(showtime_sec < 0)        return false;
        if(ees == EnumEventState.PRE)return true;

        RenderUtils.renderBoxWithColor(xStart, yStart, 2, getHeight(), color);

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
        return true;
    }

    @Override
    protected boolean doMouseMoveLocal(int mX, int mY) {
        return true;
    }

    @Override
    protected <T extends Event> boolean doEventBusLocal(T iEvent) {
        if(iEvent.getID() == EnumDefaultEvents.TIME.get() && iEvent.getData() == EnumTime.SEC_1) showtime_sec--;
        return true;
    }

    @Override
    protected boolean doOpenGUILocal(GuiOpenEvent e) {
        return true;
    }
}
