package com.palechip.hudpixelmod;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.util.EnumChatFormatting;

import com.palechip.hudpixelmod.detectors.HypixelNetworkDetector;
import com.palechip.hudpixelmod.gui.BoosterDisplay;

import net.minecraftforge.fml.client.FMLClientHandler;

/**
 * Handles the display on the screen when no gui is displayed.
 */
public class HudPixelRenderer {
    public static final int RENDERING_HEIGHT_OFFSET = 10;
    public static final int CHAT_BOX_CORRECTION = 14;
    
    // Rendering vars
    private boolean renderOnTheRight;
    private boolean renderOnTheBottom;
    private int startWidth;
    private int startHeight;
    private int startWidthRight;
    private int startHeightBottom;
    // this will be rendered when there is nothing else to render
    private ArrayList<String> defaultRenderingStrings;
    private ArrayList<String> nothingToDisplay;

    // vars for displaying the results after a game
    private ArrayList<String> results;
    private long resultRenderTime;
    private long resultStartTime;
    
    public boolean isHUDShown = true;
    
    public BoosterDisplay boosterDisplay;
    
    public HudPixelRenderer(HudPixelUpdateNotifier updater) {
        this.boosterDisplay = new BoosterDisplay();
        this.nothingToDisplay = new ArrayList<String>(0);
        // initialize rendering vars
        this.loadRenderingProperties(updater);
    }
    
    /**
     * Loads and processes all values stored in the DISPLAY_CATEGORY in the config
     */
    public void loadRenderingProperties(HudPixelUpdateNotifier updater) {
        this.renderOnTheRight = HudPixelConfig.displayMode != null ? HudPixelConfig.displayMode.toLowerCase().contains("right") : false;
        this.renderOnTheBottom = HudPixelConfig.displayMode != null ? HudPixelConfig.displayMode.toLowerCase().contains("bottom") : false;
        Minecraft mc = FMLClientHandler.instance().getClient();
        ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        
        // begin on the right side
        this.startWidthRight = (res.getScaledWidth() + HudPixelConfig.displayXOffset) - 1;
        this.startWidth = HudPixelConfig.displayXOffset + 1;
        // begin on the bottom
        this.startHeightBottom = (res.getScaledHeight() + HudPixelConfig.displayYOffset) - 1;
        this.startHeight = HudPixelConfig.displayYOffset + 1;
        
        this.defaultRenderingStrings = new ArrayList<String>();
        if(HudPixelConfig.displayVersion) {
            this.defaultRenderingStrings.add("HudPixel RL " + EnumChatFormatting.GOLD + HudPixelMod.VERSION);
        }
        if(updater.isOutOfDate) {
            this.defaultRenderingStrings.add(EnumChatFormatting.RED + "UPDATE: " + updater.newVersion);
            this.defaultRenderingStrings.add(EnumChatFormatting.YELLOW + updater.downloadLink);
        }
        
    }
    
    /**
     * This doesn't render the game. It just does calculations which shouldn't be made during the rendering tick.
     */
    public void onClientTick() {
        // update the resolution for rendering on the right & for rendering on the bottom
        Minecraft mc = FMLClientHandler.instance().getClient();
        ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        this.startWidthRight = (res.getScaledWidth() + HudPixelConfig.displayXOffset) - 1;
        this.startHeightBottom = (res.getScaledHeight() + HudPixelConfig.displayYOffset) - 1;
        
        // check the result timer
        if(this.results != null) {
            if((System.currentTimeMillis() - this.resultStartTime) >= this.resultRenderTime) {
                this.results = null;
            }
        }
    }
    
    /**
     *  Called with the last set of rendering strings so they can be displayed longer.
     */
    public void displayResults(ArrayList<String> results) {
        this.results = results;
        this.resultStartTime = System.currentTimeMillis();
        this.resultRenderTime = HudPixelConfig.displayShowResultTime >= 0 ? HudPixelConfig.displayShowResultTime * 1000 : Integer.MAX_VALUE; // transform to milliseconds
    }
    
    /**
     * This renders the entire display
     */
    public void onRenderTick() {
        Minecraft mc = FMLClientHandler.instance().getClient();
        if(HypixelNetworkDetector.isHypixelNetwork && !mc.gameSettings.showDebugInfo && (mc.inGameHasFocus || mc.currentScreen instanceof GuiChat) && this.isHUDShown) {
            FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRendererObj;
            int width;
            int height;
            ArrayList<String> renderStrings;
            boolean isBoosterDisplay = false;
            boolean isTipAllButton = false;
            
            // normal game display
            if(HudPixelMod.instance().gameDetector.getCurrentGame() != null) {
                renderStrings = HudPixelMod.instance().gameDetector.getCurrentGame().getRenderStrings();
            }
            // booster display
            else if(mc.currentScreen instanceof GuiChat && HudPixelMod.instance().gameDetector.isInLobby() && HudPixelConfig.useAPI && HudPixelConfig.displayNetworkBoosters) {
                renderStrings = this.boosterDisplay.getRenderingStrings();
                isBoosterDisplay = true;
            }
            // results after a game
            else if(this.results != null) {
                renderStrings = this.results;
            }
            // tip all button with nothing else to display
            else if(HudPixelConfig.displayTipAllButton && mc.currentScreen instanceof GuiChat && HudPixelMod.instance().gameDetector.isInLobby()) {
                renderStrings = this.nothingToDisplay;
            } else {
                // default display
                if(!this.defaultRenderingStrings.isEmpty()) {
                    renderStrings = this.defaultRenderingStrings;
                } else {
                    return;
                }
            }
            
            // should display the tip all button
            if(HudPixelConfig.displayTipAllButton && mc.currentScreen instanceof GuiChat && HudPixelMod.instance().gameDetector.isInLobby()) {
                isTipAllButton = true;
            }

            // get the right width
            if(this.renderOnTheRight || isBoosterDisplay || isTipAllButton) {
                int maxWidth = 0;
                for(String s : renderStrings) {
                    if(s != null) {
                        int stringWidth = mc.fontRendererObj.getStringWidth(s);
                        if(stringWidth > maxWidth) {
                            maxWidth = stringWidth;
                        }
                    }
                }
                width = this.startWidthRight - maxWidth;
            } else {
                width = this.startWidth;
            }

            // and the right height
            if(this.renderOnTheBottom || isBoosterDisplay || isTipAllButton) {
                height = this.startHeightBottom - renderStrings.size() * RENDERING_HEIGHT_OFFSET;
                if(isBoosterDisplay || isTipAllButton) {
                    height -= CHAT_BOX_CORRECTION;
                    if(isTipAllButton) {
                        height -= boosterDisplay.TIP_ALL_BUTTON_HEIGHT;
                    }
                }
            } else {
                height = this.startHeight;
            }

            // render a box for the booster display
            if((isBoosterDisplay && isTipAllButton) || (isTipAllButton && !renderStrings.isEmpty())) {
                this.boosterDisplay.render(width - 2, height - 2, this.startWidthRight, this.startHeightBottom - (CHAT_BOX_CORRECTION + boosterDisplay.TIP_ALL_BUTTON_HEIGHT), width - 2, this.startHeightBottom - (CHAT_BOX_CORRECTION + boosterDisplay.TIP_ALL_BUTTON_HEIGHT), (this.startWidthRight - width) + 4);
            } else if(isBoosterDisplay) {
                this.boosterDisplay.render(width - 2, height - 2, this.startWidthRight, this.startHeightBottom - CHAT_BOX_CORRECTION, width - 2, this.startHeightBottom - CHAT_BOX_CORRECTION, (this.startWidthRight - width) + 4);
            } else if(isTipAllButton) {
                this.boosterDisplay.render(0, 0, 0, 0, width - 152, this.startHeightBottom - (CHAT_BOX_CORRECTION + boosterDisplay.TIP_ALL_BUTTON_HEIGHT), 150);
            }

            // render the display
            for(int i = 0; i < renderStrings.size(); i++) {
                // skip the string if it's empty
                if(renderStrings.get(i) != null && !renderStrings.get(i).isEmpty()) {
                    fontRenderer.drawString(renderStrings.get(i), width, height, 0xffffff);
                    height += RENDERING_HEIGHT_OFFSET;
                }
            }
        }
    }
}
