package com.palechip.hudpixelmod.extended.cooldowndisplay;

import com.palechip.hudpixelmod.GameDetector;
import com.palechip.hudpixelmod.extended.HudPixelExtendedEventHandler;
import com.palechip.hudpixelmod.extended.util.IEventHandler;
import com.palechip.hudpixelmod.util.ConfigPropertyBoolean;
import com.palechip.hudpixelmod.util.ConfigPropertyInt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.util.ArrayList;

import static com.palechip.hudpixelmod.extended.cooldowndisplay.CooldownManagerFactory.setCooldownDisplay;

public class CooldownDisplayManager implements IEventHandler {

    @ConfigPropertyBoolean(catagory = "general", id = "cooldownDisplay", comment = "The Cooldown Tracker", def = false)
    public static boolean enabled = false;
    static ArrayList<CooldownDisplayModule> cdModules = new ArrayList<CooldownDisplayModule>();
    private static CooldownDisplayManager instance;

    public static CooldownDisplayManager getInstance() {
        if (instance == null) instance = new CooldownDisplayManager();
        return instance;
    }

    private CooldownDisplayManager() {
        HudPixelExtendedEventHandler.registerIEvent(this);
    }

    int count = 0;

    @Override
    public void onClientTick() {
        count++;
        if (count > 40) {
            count = 0;
            cdModules = setCooldownDisplay(GameDetector.getCurrentGameType());
        }
        if (cdModules.isEmpty()) return;
        for (CooldownDisplayModule cdM : cdModules)
            cdM.onClientTick();
    }


    @Override
    public void onRender() {

        if (cdModules.isEmpty() || !enabled) return;

        Minecraft mc = Minecraft.getMinecraft();
        int scale;
        if (mc.gameSettings.guiScale == 0) {
            ScaledResolution res = new ScaledResolution(mc);
            scale = res.getScaleFactor();
        } else {
            scale = mc.gameSettings.guiScale;
        }

        float xCenter = (Minecraft.getMinecraft().displayWidth / 2 / scale) - ((cdModules.size() / 2) * 25) + 4;
        float yCenter = Minecraft.getMinecraft().displayHeight / 2 / scale;
        for (int i = 0; i < cdModules.size(); i++)
            cdModules.get(i).renderModule(xCenter + xOffsetCooldownDisplay + (i * 25), yCenter + yOffsetCooldownDisplay);
    }
    @ConfigPropertyInt(catagory = "hudpixel", id = "yOffsetCooldownDisplay", comment = "Y offset of cooldown display", def = 25)
    public static int yOffsetCooldownDisplay = 25;
    @ConfigPropertyInt(catagory = "hudpixel", id = "xOffsetCooldownDisplay", comment = "X offset of cooldown display", def = 0)
    public static int xOffsetCooldownDisplay = 0;

    @Override
    public void handleMouseInput(int i, int mX, int mY) {
    }

    @Override
    public void onMouseClick(int mX, int mY) {
    }

    @Override
    public void onChatReceived(ClientChatReceivedEvent e) throws Throwable {
    }
}
