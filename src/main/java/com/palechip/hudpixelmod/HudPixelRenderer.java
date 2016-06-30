/*******************************************************************************
 * HudPixel Reloaded (github.com/palechip/HudPixel), an unofficial Minecraft Mod for the Hypixel Network
 *
 * Copyright (c) 2014-2015 palechip (twitter.com/palechip) and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package com.palechip.hudpixelmod;

import com.palechip.hudpixelmod.config.HudPixelConfig;
import com.palechip.hudpixelmod.detectors.HypixelNetworkDetector;
import com.palechip.hudpixelmod.extended.configuration.Config;
import com.palechip.hudpixelmod.extended.newcomponents.FpsComponent;
import com.palechip.hudpixelmod.extended.newcomponents.PingComponent;
import com.palechip.hudpixelmod.games.Game;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.util.ArrayList;
import java.util.Random;

/**
 * Handles the display on the screen when no gui is displayed.
 */
public class HudPixelRenderer {
    public static final int RENDERING_HEIGHT_OFFSET = 10;
    public static final int CHAT_BOX_CORRECTION = 14;
    public boolean isHUDShown = true;
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
    
    public HudPixelRenderer() {
        this.nothingToDisplay = new ArrayList<String>(0);
        // initialize rendering vars
        this.loadRenderingProperties();
    }
    
    /**
     * Loads and processes all values stored in the DISPLAY_CATEGORY in the config
     */
    public void loadRenderingProperties() {
        this.renderOnTheRight = HudPixelConfig.displayMode != null && HudPixelConfig.displayMode.toLowerCase().contains("right");
        this.renderOnTheBottom = HudPixelConfig.displayMode != null && HudPixelConfig.displayMode.toLowerCase().contains("bottom");
        Minecraft mc = FMLClientHandler.instance().getClient();
        ScaledResolution res = new ScaledResolution(mc);
        
        // begin on the right side
        this.startWidthRight = (res.getScaledWidth() + HudPixelConfig.displayXOffset) - 1;
        this.startWidth = HudPixelConfig.displayXOffset + 1;
        // begin on the bottom
        this.startHeightBottom = (res.getScaledHeight() + HudPixelConfig.displayYOffset) - 1;
        this.startHeight = HudPixelConfig.displayYOffset + 1;
        
        this.defaultRenderingStrings = new ArrayList<String>();
        if(HudPixelConfig.displayVersion) {
            this.defaultRenderingStrings.add("HudPixel RL " + EnumChatFormatting.GOLD + HudPixelMod.instance());
        }


    }
    
    /**
     * This doesn't render the game. It just does calculations which shouldn't be made during the rendering tick.
     */
    public void onClientTick() {
        // update the resolution for rendering on the right & for rendering on the bottom
        Minecraft mc = FMLClientHandler.instance().getClient();
        ScaledResolution res = new ScaledResolution(mc);
        this.startWidthRight = (res.getScaledWidth() + HudPixelConfig.displayXOffset) - 1;
        this.startHeightBottom = (res.getScaledHeight() + HudPixelConfig.displayYOffset) - 1;

        updateDefaultRenderStrings();
        
        // check the result timer
        if(this.results != null) {
            if((System.currentTimeMillis() - this.resultStartTime) >= this.resultRenderTime) {
                this.results = null;
            }
        }
    }
    private static String pickColor() {
        Random r = new Random();
        String[] a = {
                "" + EnumChatFormatting.RED + EnumChatFormatting.BOLD ,
                "" + EnumChatFormatting.GOLD + EnumChatFormatting.BOLD ,
                "" + EnumChatFormatting.YELLOW + EnumChatFormatting.BOLD,
                "" + EnumChatFormatting.GREEN + EnumChatFormatting.BOLD,
                "" + EnumChatFormatting.DARK_AQUA + EnumChatFormatting.BOLD,
                "" + EnumChatFormatting.BLUE + EnumChatFormatting.BOLD,
                "" + EnumChatFormatting.LIGHT_PURPLE + EnumChatFormatting.BOLD,
        };
        return a[r.nextInt(a.length)];
    }
    private void updateDefaultRenderStrings(){
        ArrayList<String> bufferStrings = new ArrayList<String>();
        if(HudPixelConfig.displayVersion)   bufferStrings.add("HudPixelReloaded " + EnumChatFormatting.GOLD + HudPixelMod.DEFAULT_VERSION);
        if(Config.isPingShown)bufferStrings.add(EnumChatFormatting.GOLD +  PingComponent.getStaticRenderingString());
        if(Config.isFpsShown)bufferStrings.add(EnumChatFormatting.GOLD + FpsComponent.getFps());

        this.defaultRenderingStrings = bufferStrings;
    }
    
    /**
     *  Called with the last set of rendering strings so they can be displayed longer.
     */
    public void displayResults(ArrayList<String> results) {
        if(Config.isPingShown) results.remove(0);
        if(Config.isFpsShown) results.remove(0);
        this.results = new ArrayList<String>(results);
        this.resultStartTime = System.currentTimeMillis();
        this.resultRenderTime = HudPixelConfig.displayShowResultTime >= 0 ? HudPixelConfig.displayShowResultTime * 1000 : Integer.MAX_VALUE; // transform to milliseconds
    }
    
    private ArrayList<String> getRightRenderstring(){
        ArrayList<String> renderStrings = null;
        boolean isBoosterDisplay = false;
        boolean isTipAllButton = false;
        // normal game display
        if(!HudPixelMod.instance().gameDetector.getCurrentGame().equals(Game.NO_GAME)) {
            renderStrings = HudPixelMod.instance().gameDetector.getCurrentGame().getRenderStrings();
        }
        // booster display
        else if(Minecraft.getMinecraft().currentScreen instanceof GuiIngameMenu && HudPixelMod.instance().gameDetector.isInLobby() && HudPixelConfig.useAPI && HudPixelConfig.displayNetworkBoosters) {
            isBoosterDisplay = true;
        }
        // results after a game
        else if(this.results != null) {
            renderStrings = new ArrayList<String>(defaultRenderingStrings);
            renderStrings.addAll(results);
        }
        // tip all button with nothing else to display
        else if(HudPixelConfig.displayQuickLoadButton && Minecraft.getMinecraft().currentScreen instanceof GuiIngameMenu && HudPixelMod.instance().gameDetector.isInLobby()) {
            renderStrings = this.nothingToDisplay;
        } else {
            // default display
            if(!this.defaultRenderingStrings.isEmpty()) {
                renderStrings = this.defaultRenderingStrings;
            } else {
                return renderStrings;
            }
        }
        return renderStrings;
    }
    
    /**
     * This renders the entire display
     * TODO: this is shit .... maybe i should rewrite the hudpixel rendersystem
     */
    public void onRenderTick() {
        if(true) return;
        Minecraft mc = FMLClientHandler.instance().getClient();
        if(HypixelNetworkDetector.isHypixelNetwork && !mc.gameSettings.showDebugInfo && (mc.inGameHasFocus || mc.currentScreen instanceof GuiIngameMenu) && this.isHUDShown) {
            FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRendererObj;
            int width;
            int height;
            ArrayList<String> renderStrings;
            boolean isBoosterDisplay = false;
            boolean isTipAllButton = false;
            
            // normal game display
            if(!HudPixelMod.instance().gameDetector.getCurrentGame().equals(Game.NO_GAME) && !(mc.currentScreen instanceof GuiIngameMenu)) {
                renderStrings = HudPixelMod.instance().gameDetector.getCurrentGame().getRenderStrings();
            }

            // booster display
            else if(mc.currentScreen instanceof GuiIngameMenu && HudPixelMod.instance().gameDetector.isInLobby() && HudPixelConfig.useAPI && HudPixelConfig.displayNetworkBoosters) {
                isBoosterDisplay = true;
            }

            // results after a game
            else if(this.results != null && !(mc.currentScreen instanceof GuiIngameMenu ) ){
                renderStrings = new ArrayList<String>(defaultRenderingStrings);
                renderStrings.addAll(results);
            }

            // tip all button with nothing else to display
            else if(HudPixelConfig.displayQuickLoadButton && mc.currentScreen instanceof GuiIngameMenu && HudPixelMod.instance().gameDetector.isInLobby()) {
                renderStrings = this.nothingToDisplay;

            } else {
                // default display
                if(!this.defaultRenderingStrings.isEmpty() && !(mc.currentScreen instanceof GuiIngameMenu)) {
                    renderStrings = this.defaultRenderingStrings;
                } else {
                    return;
                }
            }
        }
    }
}
