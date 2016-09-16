package eladkay.modulargui.lib;

import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.config.HudPixelConfig;
import com.palechip.hudpixelmod.extended.configuration.Config;
import de.unaussprechlich.managedgui.lib.util.RenderUtils;
import eladkay.modulargui.lib.base.SimpleModularGuiProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;

/**
    This class is responsible for rendering the elements of the modular GUI.
    @author Eladkay
    @since 1.5
 */
public class Renderer {

    //Replace with some sort of config option
    private boolean isEnabled = true;

    /**
        Event: When the game renders its overlay.
        @author MinecraftForge
     */
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if(!HudPixelMod.isHypixelNetwork() && !HudPixelMod.IS_DEBUGGING) return;
        if(!(Minecraft.getMinecraft().inGameHasFocus)) return;
        ArrayList<ModularGuiRegistry.Element> display = ModularGuiRegistry.allElements; //the elements
        int w = HudPixelConfig.displayXOffset; //width, change this if needed
        int h = HudPixelConfig.displayYOffset; //height, you shouldn't touch this usually
        if (isEnabled) { //if enabled...
            FontRenderer fontRendererObj = FMLClientHandler.instance().getClient().fontRendererObj; //get the font renderer
            for (ModularGuiRegistry.Element element : display) { //for each element...
                if(!element.provider.showElement()) continue; //if you shouldn't show it, skip it.
                String aDisplay;
                if(!element.name.isEmpty() && !(element.provider instanceof SimpleModularGuiProvider))
                    aDisplay = element.name + ": " + element.provider.content(); //get the display text for the element
                else
                    aDisplay = element.provider.content();
                if(element.provider.content() == null) return;
                if (element.provider instanceof SimpleModularGuiProvider || !(element.provider.content().isEmpty() && element.name.isEmpty()) || element.provider.ignoreEmptyCheck()) { //if it's not empty or it's allowed to override this isHypixelNetwork...
                    if(Config.hudBackground)
                        RenderUtils.renderBoxWithColor(w - 2, h -1, fontRendererObj.getStringWidth(aDisplay) + 4, 10, 0, Config.hudRed, Config.hudGreen, Config.hudBlue, Config.hudAlpha );
                    fontRendererObj.drawString(aDisplay, w, h, 0xffffff); //draw it
                    h += 10; //increment height
                }
            }
        }
    }
}
