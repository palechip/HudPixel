package com.palechip.hudpixelmod.extended.cooldowndisplay;

import com.palechip.hudpixelmod.detectors.GameDetector;
import com.palechip.hudpixelmod.extended.HudPixelExtendedEventHandler;
import com.palechip.hudpixelmod.extended.util.IEventHandler;
import com.palechip.hudpixelmod.games.GameEventObserver;
import com.palechip.hudpixelmod.games.IGameEvents;
import com.palechip.hudpixelmod.util.GameType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.util.ArrayList;

import static com.palechip.hudpixelmod.extended.cooldowndisplay.CooldownManagerFactory.setCooldownDisplay;

public class CooldownDisplayManager implements IEventHandler, IGameEvents{

    static ArrayList<CooldownDisplayModule> cdModules = new ArrayList<CooldownDisplayModule>();
    private static float xStart = 0; //TODO via config
    private static float yStart = 20; //TODO via config
    private static float offset = 25;
    private static CooldownDisplayManager instance;

    public static CooldownDisplayManager getInstance(){
        if(instance == null) instance = new CooldownDisplayManager();
        return instance;
    }

    private CooldownDisplayManager(){
        GameEventObserver.registerClient(this);
        HudPixelExtendedEventHandler.registerIEvent(this);
    }


    @Override
    public void onClientTick() {
        if(cdModules.isEmpty()) return;
        for(CooldownDisplayModule cdM : cdModules)
            cdM.onClientTick();
    }

    @Override
    public void onChatReceived(ClientChatReceivedEvent e) throws Throwable {

    }

    @Override
    public void onRender() {
        Minecraft mc = Minecraft.getMinecraft();
        int scale;
        if(mc.gameSettings.guiScale == 0){
            ScaledResolution res = new ScaledResolution( mc);
            scale = res.getScaleFactor();
        } else {
            scale = mc.gameSettings.guiScale;
        }

        if(cdModules.isEmpty()) return;
        float xCenter = (Minecraft.getMinecraft().displayWidth / 2 / scale) - ((cdModules.size()/2) * offset) + 4;
        float yCenter = Minecraft.getMinecraft().displayHeight  / 2 / scale;
        for(int i = 0; i < cdModules.size(); i++)
            cdModules.get(i).renderModule(xCenter + xStart + (i * offset), yCenter + yStart);
    }

    @Override
    public void handleMouseInput(int i, int mX, int mY) {

    }

    @Override
    public void onMouseClick(int mX, int mY) {

    }

    @Override
    public void onGameStart() {
        cdModules = setCooldownDisplay(GameType.getTypeByID(GameDetector.currentGame.getConfiguration().getModID()));
    }

    @Override
    public void onGameEnd() {
        cdModules.clear();
    }
}
