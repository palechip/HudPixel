package com.palechip.hudpixelmod.extended.cooldowndisplay;

import com.palechip.hudpixelmod.detectors.GameDetector;
import com.palechip.hudpixelmod.extended.HudPixelExtendedEventHandler;
import com.palechip.hudpixelmod.extended.util.IEventHandler;
import com.palechip.hudpixelmod.games.IGameEvents;
import com.palechip.hudpixelmod.util.GameType;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.util.ArrayList;

public class CooldownDisplayManager implements IEventHandler, IGameEvents{

    static ArrayList<CooldownDisplayModule> cdModules = new ArrayList<CooldownDisplayModule>();
    private static float xStart; //TODO via config
    private static float yStart; //TODO via config
    private static float offset = 25;
    private static CooldownDisplayManager instance;

    public CooldownDisplayManager getInstance(){
        if(instance == null) instance = new CooldownDisplayManager();
        return instance;
    }

    private CooldownDisplayManager(){
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
        if(cdModules.isEmpty()) return;
        for(int i = 0; i <= cdModules.size(); i++)
            cdModules.get(i).renderModule(xStart + i * offset, yStart + i *offset);
    }

    @Override
    public void handleMouseInput(int i, int mX, int mY) {

    }

    @Override
    public void onMouseClick(int mX, int mY) {

    }

    @Override
    public void onGameStart() {
        cdModules = CooldownManagerFactory.setCooldownDisplay(GameType.getTypeByID(GameDetector.currentGame.getConfiguration().getModID()));
    }

    @Override
    public void onGameEnd() {
        cdModules.clear();
    }
}
