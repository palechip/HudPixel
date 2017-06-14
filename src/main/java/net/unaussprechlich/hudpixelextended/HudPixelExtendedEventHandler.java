package net.unaussprechlich.hudpixelextended;


import eladkay.hudpixel.GameDetector;
import eladkay.hudpixel.HudPixelMod;
import eladkay.hudpixel.config.EasyConfigHandler;
import eladkay.hudpixel.modulargui.ModularGuiHelper;
import eladkay.hudpixel.util.ChatMessageComposer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.unaussprechlich.hudpixelextended.onlinefriends.OnlineFriendManager;
import net.unaussprechlich.hudpixelextended.statsviewer.StatsViewerManager;
import net.unaussprechlich.hudpixelextended.util.IEventHandler;
import net.unaussprechlich.hudpixelextended.util.gui.FancyListManager;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

import static eladkay.hudpixel.util.DisplayUtil.getMcScale;


public class HudPixelExtendedEventHandler {

    private static final long clickDelay = 1000;

    private static long lastTimeClicked;
    private static boolean doubleClick = false;
    private long lastSystemTime = System.currentTimeMillis();
    private int delay = 20 * 1000; //20s

    private static final ArrayList<IEventHandler> iEventArrayList = new ArrayList<>();

    public static void registerIEvent(IEventHandler iEventHandler) {
        iEventArrayList.add(iEventHandler);
    }

    public static void unregisterIEvent(IEventHandler iEventHandler) {
        iEventArrayList.remove(iEventHandler);
    }

    public static ArrayList<IEventHandler> getIeventBuffer() {
        return new ArrayList<>(iEventArrayList);
    }


    private static void mouseClickEvent() {
        Minecraft mc = Minecraft.getMinecraft();
        if (!(mc.currentScreen instanceof GuiIngameMenu || mc.currentScreen instanceof GuiChat)) return;

        if (System.currentTimeMillis() > (lastTimeClicked + clickDelay) && Mouse.isButtonDown(0)) {
            doubleClick = false;
            lastTimeClicked = System.currentTimeMillis();

        } else if (System.currentTimeMillis() < (lastTimeClicked + clickDelay)) {
            if (!Mouse.isButtonDown(0) && !doubleClick) {
                doubleClick = true;
                return;
            }

            if (Mouse.isButtonDown(0) && doubleClick) {
                doubleClick = false;
                int scale = getMcScale();

                int mX = Mouse.getX() / scale;
                int mY = (mc.displayHeight - Mouse.getY()) / scale;
                for (IEventHandler iE : getIeventBuffer()) {
                    iE.onMouseClick(mX, mY);
                }
            }
        }
    }

    public static void handleMouseScroll(int i) {
        mouseClickEvent();

        Minecraft mc = Minecraft.getMinecraft();
        if (!(mc.currentScreen instanceof GuiIngameMenu || mc.currentScreen instanceof GuiChat)) return;

        int scale = getMcScale();

        int mX = Mouse.getX() / scale;
        int mY = (mc.displayHeight - Mouse.getY()) / scale;
        //int i = Mouse.getDWheel();
        for (IEventHandler iE : getIeventBuffer()) {
            iE.handleMouseInput(i, mX, mY);
        }
    }

    public static void onGameStart() {

    }

    public static void onGameEnd() {
        ModularGuiHelper.onGameEnd();
    }

    /**
     * prints the message to the clientchat
     *
     * @param message the message
     **/
    private static void printMessage(String message) {
        new ChatMessageComposer(message).send();
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        try {
            // This event isn't bound to the Hypixel Network
            if (eventArgs.getModID().equals(HudPixelMod.MODID)) {
                EasyConfigHandler.INSTANCE.synchronize();
                getIeventBuffer().forEach(IEventHandler::onConfigChanged);
            }
        } catch (Exception e) {
            HudPixelMod.Companion.instance().logWarn("[Extended] An exception occurred in onConfigChanged(). Stacktrace below.");
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onPlayerName(PlayerEvent.NameFormat e) {
        //StaffManager.onPlayerName(e);
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent e) {
        try {
            if (!HudPixelMod.Companion.isHypixelNetwork()) return;

            for (IEventHandler i : getIeventBuffer())
                i.onRenderWorld(e);

        } catch (Exception ex) {
            HudPixelMod.Companion.instance().logWarn("[Extended] An exception occurred in onRenderWorldLast(). Stacktrace below.");
            ex.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Pre e) {
        try {
            //Don't do anything unless we are on Hypixel
            if (HudPixelMod.Companion.isHypixelNetwork()) {
                for (IEventHandler i : getIeventBuffer())
                    i.onRenderPlayer(e);

                //just triggeres the statsrenderer if the player is waiting for the game to start
                if (GameDetector.getIsLobby() && StatsViewerManager.enabled)
                    StatsViewerManager.onRenderPlayer(e);
            }
        } catch (Exception ex) {
            HudPixelMod.Companion.instance().logWarn("[Extended] An exception occurred in onRenderPlayer(). Stacktrace below.");
            ex.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onOpenGui(GuiOpenEvent e) {
        try {
            //Don't do anything unless we are on Hypixel
            if (HudPixelMod.Companion.isHypixelNetwork()) {
                for (IEventHandler i : getIeventBuffer())
                    i.openGUI(Minecraft.getMinecraft().currentScreen);
                if (FMLClientHandler.instance().getClientPlayerEntity() != null)
                    OnlineFriendManager.getInstance();
            }
        } catch (Exception ex) {
            HudPixelMod.Companion.instance().logWarn("[Extended] An exception occurred in onOpenGui(). Stacktrace below.");
            ex.printStackTrace();
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onChatMessage(ClientChatReceivedEvent e) {
        try {
            //Don't do anything unless we are on Hypixel
            if (HudPixelMod.Companion.isHypixelNetwork()) {
                final String message = e.getMessage().getUnformattedText();

                for (IEventHandler i : getIeventBuffer())
                    i.onChatReceivedMessage(e, message);

                for (IEventHandler i : getIeventBuffer())
                    i.onChatReceived(e);
            }
        } catch (Exception ex) {
            HudPixelMod.Companion.instance().logWarn("[Extended]An exception occurred in onChatMessage(). Stacktrace below.");
            ex.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private static short tick;
    private static short sec;
    private static short min;

    private static void processTickTimes() {
        tick++;
        if (tick >= 20) {
            tick = 0;
            sec++;
            getIeventBuffer().forEach(IEventHandler::everySEC);
            if (sec >= 60) {
                sec = 0;
                min++;
                getIeventBuffer().forEach(IEventHandler::everyMIN);
                if (min >= 60)
                    min = 0;
            } else if (sec == 5) {
                getIeventBuffer().forEach(IEventHandler::everyFiveSEC);
            }
        } else if (tick == 10) {
            getIeventBuffer().forEach(IEventHandler::everyTenTICKS);
        }

    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent e) {
        try {
            //Don't do anything unless we are on Hypixel
            if (HudPixelMod.Companion.isHypixelNetwork()) {

                processTickTimes();

                getIeventBuffer().forEach(IEventHandler::onClientTick);

                FancyListManager.processLoadingBar();
                //handleMouseScroll();
                //Tick for the statsViewerManager
                if (GameDetector.getIsLobby()) {
                    //System.out.print(GameDetector.getCurrentGameType().getNm());
                    StatsViewerManager.onClientTick();
                }

                if (lastSystemTime + delay < System.currentTimeMillis()) {
                    lastSystemTime = System.currentTimeMillis();
                }

            } else if (HudPixelMod.Companion.getIS_DEBUGGING()) {

            }
        } catch (Exception ex) {
            HudPixelMod.Companion.instance().logWarn("[Extended]An exception occurred in onClientTick(). Stacktrace below.");
            ex.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onRenderTick(RenderGameOverlayEvent.Post e) {
        try {
            //Don't do anything unless we are on Hypixel
            if (HudPixelMod.Companion.isHypixelNetwork() && e.getType() == RenderGameOverlayEvent.ElementType.ALL && !e.isCancelable()) {
                getIeventBuffer().forEach(IEventHandler::onRender);

            }
        } catch (Exception ex) {
            HudPixelMod.Companion.instance().logWarn("[Extended]An exception occurred in omRenderTick). Stacktrace below.");
            ex.printStackTrace();
        }
    }
}
