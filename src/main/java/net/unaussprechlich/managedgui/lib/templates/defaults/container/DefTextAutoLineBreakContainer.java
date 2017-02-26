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
import net.unaussprechlich.managedgui.lib.CONSTANTS;
import net.unaussprechlich.managedgui.lib.container.Container;
import net.unaussprechlich.managedgui.lib.event.util.Event;
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;
import net.unaussprechlich.managedgui.lib.util.EnumEventState;
import net.unaussprechlich.managedgui.lib.util.FontHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * DefTextAutoLineBreakContainer Created by unaussprechlich on 21.12.2016.
 * Description:
 **/
public class DefTextAutoLineBreakContainer extends Container{

    private String text       = "";
    private List<String>  renderList = new ArrayList<>();
    private boolean isShadow = false;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        renderList.clear();
        this.text = text;
        if(FontHelper.widthOfString(this.text) <= this.getWidth())
            renderList.add(text);
        else
            renderList = FontHelper.getFrontRenderer().listFormattedStringToWidth(text, getWidth());
        super.setHeight(CONSTANTS.TEXT_Y_OFFSET * renderList.size());
    }

    public void setShadow(boolean shadow) {
        isShadow = shadow;
    }

    public boolean isShadow() {
        return isShadow;
    }

    public DefTextAutoLineBreakContainer(String text, int width) {
        super.setWidth(width);
        setText(text);
    }

    private void render(int xStart, int yStart){
        for(String s : renderList){
            if(isShadow) FontHelper.drawWithShadow(s, xStart, yStart);
            else         FontHelper.draw(s, xStart, yStart);
            yStart += CONSTANTS.TEXT_Y_OFFSET;
        }
    }

    @Override
    public void setWidth(int width) {
        throw new UnsupportedOperationException("[ManagedGuiLib][DefTextAutoLineBreakContainer] setWidth() is handled automatically use setPadding() instead!");
    }

    @Override
    public void setHeight(int width) {
        throw new UnsupportedOperationException("[ManagedGuiLib][DefTextAutoLineBreakContainer] setHeight() is handled automatically use setPadding() instead!");
    }

    @Override
    protected boolean doClientTickLocal() {
        return true;
    }

    @Override
    protected boolean doRenderTickLocal(int xStart, int yStart, int width, int height, EnumEventState ees) {
        if(ees == EnumEventState.POST){
            render(xStart, yStart);
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
        return true;
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
