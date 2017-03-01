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
import net.unaussprechlich.managedgui.lib.util.FontUtil;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * DefTextListContainer Created by unaussprechlich on 21.12.2016.
 * Description:
 **/
public class DefTextListContainer extends Container{

    private ArrayList<String> textList = new ArrayList<>();

    public void addEntry(String s){
        textList.add(s);
        updateSize();
    }

    public void setTextList(ArrayList<String> textList){
        this.textList = textList;
        updateSize();
    }

    public int getListSize(){
        if(textList.isEmpty()) return 0;
        return textList.size();
    }

    public void clearAll(){
        textList.clear();
    }

    public void removeEntry(String s){
        textList.removeAll(textList.stream()
                                   .filter(s1 -> s1.equalsIgnoreCase(s))
                                   .collect(Collectors.toList())
        );
    }

    protected void updateSize(){
        super.setWidth(FontUtil.widthOfString(textList.stream()
                                                        .sorted((e1, e2) -> e1.length() > e2.length() ? -1 : 1)
                                                        .findFirst()
                                                        .orElse(""))
        );
        super.setHeight(textList.size() * CONSTANTS.TEXT_Y_OFFSET);
    }

    public DefTextListContainer(ArrayList<String> textList) {
        this.textList = textList;
        updateSize();
    }

    private void render(int xStart, int yStart){
        for(String s : textList){
            FontUtil.drawWithShadow(s, xStart, yStart);
            yStart += CONSTANTS.TEXT_Y_OFFSET;
        }
    }

    protected void setWidthLocal(int width){
        super.setWidth(width);
    }

    protected void setHeightLocal(int height){
        super.setHeight(height);
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

    @Override
    protected boolean doResizeLocal(int width, int height) {
        return true;
    }
}
