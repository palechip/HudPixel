package net.unaussprechlich.hudpixelextended;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.unaussprechlich.hudpixelextended.boosterdisplay.BoosterManager;
import net.unaussprechlich.hudpixelextended.cooldowndisplay.CooldownDisplayManager;
import net.unaussprechlich.hudpixelextended.gui.ArmorHud;
import net.unaussprechlich.hudpixelextended.gui.EffectHud;
import net.unaussprechlich.hudpixelextended.staff.StaffManager;
import net.unaussprechlich.hudpixelextended.translator.Translator;

import java.util.UUID;

@SideOnly(Side.CLIENT)
public class HudPixelExtended {

    public static UUID UUID;
    public static BoosterManager boosterManager;
    public static StaffManager staffManager;
    private static HudPixelExtended hudPixelExtendedInstance = null;
    private static HudPixelExtendedEventHandler hudPixelExtendedEventHandler = new HudPixelExtendedEventHandler();

    private HudPixelExtended() {
    }

    public static HudPixelExtended getInstance() {
        return hudPixelExtendedInstance == null ? hudPixelExtendedInstance = new HudPixelExtended() : hudPixelExtendedInstance;
    }

    public void setup() {
        MinecraftForge.EVENT_BUS.register(hudPixelExtendedEventHandler);

        UUID = Minecraft.getMinecraft().getSession().getProfile().getId();
        Translator.getINSTANCE().init();
        boosterManager = new BoosterManager();
        staffManager = new StaffManager();
        CooldownDisplayManager.getInstance();
        ArmorHud.INSTANCE.init();
        EffectHud.getINSTANCE().init();

        //nothing to see here :P
        //TODO: remove!
        //SecretFeatures.getINSTANCE().setup();
    }

    public void setupPOST() {

    }

}
