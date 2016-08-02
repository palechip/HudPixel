package com.palechip.hudpixelmod.modulargui.components;

import com.palechip.hudpixelmod.detectors.HypixelNetworkDetector;
import com.palechip.hudpixelmod.games.Game;
import com.palechip.hudpixelmod.modulargui.HudPixelModularGuiProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.OldServerPinger;

import java.net.UnknownHostException;

public class PingAndFpsModularGuiProvider extends HudPixelModularGuiProvider {
    @Override
    public boolean doesMatchForGame(Game game) {
        return true;
    }

    @Override
    public void setupNewGame() {

    }

    @Override
    public void onGameStart() {

    }

    @Override
    public void onGameEnd() {

    }

    @Override
    public void onTickUpdate() {

    }

    @Override
    public void onChatMessage(String textMessage, String formattedMessage) {

    }

    @Override
    public boolean showElement() {
        return Minecraft.getMinecraft().getCurrentServerData() != null;
    }

    @Override
    public String content() {
        return pingOrFps == PingOrFps.PING ? Minecraft.getDebugFPS() + "" : getStaticRenderingString();
    }

    @Override
    public boolean ignoreEmptyCheck() {
        return false;
    }

    @Override
    public String getAfterstats() {
        return null;
    }

    public enum PingOrFps {PING, FPS}

    public PingAndFpsModularGuiProvider(PingOrFps pingOrFps) {
        this.pingOrFps = pingOrFps;
    }

    PingOrFps pingOrFps;

    private static final int pingCooldwonMs = 2000;
    private static long nextTimeStamp;
    private static long lastValidPing;
    private static OldServerPinger serverPinger = new OldServerPinger();
    private static String pingString;

    /**
     * A methode that returns the last valid ping and updates it if necessary
     * @return "Ping:" + your last ping in ms
     */
    public static String getStaticRenderingString() {

        // updates the ping if the last ping validation has expired
        if(System.currentTimeMillis() >= nextTimeStamp){
            updatePing();
        }

        // updates the current renderString
        if(Minecraft.getMinecraft().getCurrentServerData() == null || Minecraft.getMinecraft() == null) {
            lastValidPing = 0;
            return pingString = "Irrelevant";
        }

        if(Minecraft.getMinecraft().getCurrentServerData().pingToServer != lastValidPing
                && Minecraft.getMinecraft().getCurrentServerData().pingToServer > 0){
            lastValidPing = Minecraft.getMinecraft().getCurrentServerData().pingToServer;
            pingString =
                    // EnumChatFormatting.WHITE
                           /* + */""
                    + lastValidPing
                    + "ms";
        }
        return pingString;

    }

    /**
     * the function who updates your ping. Every ping request is done in a external thread,
     * to not block the mainthread while waiting for the response.
     */
    private static void updatePing(){
        nextTimeStamp = System.currentTimeMillis() + pingCooldwonMs;

        //starting external Thread to not block the mainthread
        new Thread("pingThread"){
            @Override
            public void run(){
                try {
                    if(HypixelNetworkDetector.isHypixelNetwork)
                        if(Minecraft.getMinecraft().getCurrentServerData() != null)
                            serverPinger.ping(Minecraft.getMinecraft().getCurrentServerData());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
