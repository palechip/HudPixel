/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.templates.defaults.container;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.unaussprechlich.managedgui.lib.container.Container;
import net.unaussprechlich.managedgui.lib.container.callback.ICallbackUpdateHeight;
import net.unaussprechlich.managedgui.lib.databases.Player.PlayerModel;
import net.unaussprechlich.managedgui.lib.event.EnumDefaultEvents;
import net.unaussprechlich.managedgui.lib.event.util.EnumTime;
import net.unaussprechlich.managedgui.lib.event.util.Event;
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;
import net.unaussprechlich.managedgui.lib.helper.DateHelper;
import net.unaussprechlich.managedgui.lib.util.EnumEventState;
import net.unaussprechlich.managedgui.lib.util.storage.ContainerSide;

import java.util.Date;

/**
 * DefChatMessageContainer Created by Alexander on 27.02.2017.
 * Description:
 **/
public class DefChatMessageContainer extends Container {

    private static int SPACE = 4;

    private DefTextListAutoLineBreakContainer message_con;
    private DefTextContainer username_con;
    private DefPictureContainer avatar_con;

    private ICallbackUpdateHeight callback;

    private final PlayerModel player;
    private final DateHelper date;

    public DefChatMessageContainer(PlayerModel player, String message, DefPictureContainer avatar_con, int width) {
        this.date = new DateHelper();
        this.player = player;
        this.avatar_con = avatar_con;
        this.username_con = new DefTextContainer(player.getRankName() + ChatFormatting.GRAY + ChatFormatting.ITALIC + "  " + date.getDateTimeTextPassed());
        setWidth(width);
        setup(message);
    }

    public void setHeightCallback(ICallbackUpdateHeight callback) {
        this.callback = callback;
    }

    public String getPlayername(){
        return player.getName();
    }

    private void setup(String message){
        avatar_con.setMargin(SPACE);
        avatar_con.setWidth(18);
        avatar_con.setHeight(18);
        username_con.setMargin(new ContainerSide().BOTTOM(4).TOP(4));
        username_con.setXOffset(avatar_con.getWidthMargin());
        message_con = new DefTextListAutoLineBreakContainer(message, getWidth() - avatar_con.getWidthMargin() - SPACE - 14, data -> update());
        message_con.setXYOffset(avatar_con.getWidthMargin(), username_con.getHeightMargin() + 2);

        registerChild(message_con);
        registerChild(avatar_con);
        registerChild(username_con);

        update();
    }

    public void addMessage(String s){
        message_con.addEntry(s);
        update();
    }

    private void update(){
        super.setHeight(SPACE * 2  + username_con.getHeightMargin() + message_con.getHeightMargin());
        message_con.setWidth(getWidth() - avatar_con.getWidthMargin() - SPACE - 14);
        if(callback != null){
            callback.call(getHeight());
        }
    }

    private static String getStyledTime(){
        Date date = new Date();
        return ChatFormatting.DARK_GRAY +"" + ChatFormatting.ITALIC + " " + date.getHours() + ":"  + date.getMinutes();
    }

    @Override
    public void setHeight(int height) {
        throw new UnsupportedOperationException("[ManagedGuiLib][DefMessageContainer] setHeight() is handled automatically use setPadding() instead!");
    }

    @Override
    protected boolean doClientTickLocal() {
        return true;
    }

    @Override
    protected boolean doRenderTickLocal(int xStart, int yStart, int width, int height, EnumEventState ees) {
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
        if(iEvent.getID() != EnumDefaultEvents.TIME.get()) return true;
        if(iEvent.getData().equals(EnumTime.SEC_5)){
                username_con.setText(player.getRankName() + ChatFormatting.GRAY + ChatFormatting.ITALIC + "  " + date.getDateTimeTextPassed());
        }
        return true;
    }

    @Override
    protected boolean doOpenGUILocal(GuiOpenEvent e) {
        return true;
    }

    @Override
    protected boolean doResizeLocal(int width, int height) {
        update();
        return true;
    }
}
