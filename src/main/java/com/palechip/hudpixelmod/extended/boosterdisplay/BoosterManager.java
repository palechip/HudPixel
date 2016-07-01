package com.palechip.hudpixelmod.extended.boosterdisplay;

import com.palechip.hudpixelmod.api.interaction.Queue;
import com.palechip.hudpixelmod.api.interaction.callbacks.BoosterResponseCallback;
import com.palechip.hudpixelmod.api.interaction.representations.Booster;
import com.palechip.hudpixelmod.config.HudPixelConfig;
import com.palechip.hudpixelmod.extended.HudPixelExtended;
import com.palechip.hudpixelmod.extended.util.LoggerHelper;
import com.palechip.hudpixelmod.extended.util.gui.FancyListManager;
import com.palechip.hudpixelmod.extended.util.gui.FancyListObject;
import com.palechip.hudpixelmod.util.GameType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.util.ArrayList;

/******************************************************************************
 * HudPixelExtended by unaussprechlich(github.com/unaussprechlich/HudPixelExtended),
 * an unofficial Minecraft Mod for the Hypixel Network.
 * <p>
 * Original version by palechip (github.com/palechip/HudPixel)
 * "Reloaded" version by PixelModders -> Eladkay (github.com/PixelModders/HudPixel)
 * <p>
 * Copyright (c) 2016 unaussprechlich and contributors
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
public class BoosterManager extends FancyListManager implements BoosterResponseCallback{

    private long lastRequest;
    private static final int REQUEST_COOLDOWN = 60000; // = 30s

    private final static GameType[] gamesWithBooster = new GameType[]{
            GameType.SPEED_UHC,
            GameType.SMASH_HEROES,
            GameType.CRAZY_WALLS,
            GameType.SKYWARS,
            GameType.TURBO_KART_RACERS,
            GameType.WARLORDS,
            GameType.MEGA_WALLS,
            GameType.UHC,
            GameType.BLITZ,
            GameType.COPS_AND_CRIMS,
            GameType.THE_WALLS,
            GameType.ARCADE_GAMES,
            GameType.ARENA,
            GameType.PAINTBALL,
            GameType.TNT_GAMES,
            GameType.VAMPIREZ
    };

    public BoosterManager(){
        super(5);
        for(GameType g : gamesWithBooster){
            this.fancyListObjects.add(new BoosterExtended(g));
        }
    }

    public void requestBoosters(){
        if(HudPixelConfig.useAPI && HudPixelConfig.displayNetworkBoosters) {
            // check if enough time has past
            if(System.currentTimeMillis() > lastRequest + REQUEST_COOLDOWN) {
                // save the time of the request
                lastRequest = System.currentTimeMillis();
                // tell the queue that we need boosters
                Queue.getInstance().getBoosters(HudPixelExtended.boosterManager);
            }
        }
    }

    @Override
    public void onRender(){
        if(Minecraft.getMinecraft().currentScreen instanceof GuiChat)
            this.renderDisplay();
    }

    @Override
    public void onClientTick(){
        requestBoosters();
        for(FancyListObject b : fancyListObjects){
            b.onClientTick();
        }
    }

    @Override
    public void onChatReceived(ClientChatReceivedEvent e) {
        String chat = e.message.getUnformattedText();
        if(!chat.contains("You tipped ") || chat.contains(":")) return;

        String[] split = chat.split(" ");
        String player = split[2];
        String gamemode = split[4];

        for(int i = 5; i < split.length; i++)
            gamemode+=(" " + split[i]);

        GameType gameType = GameType.getTypeByName(gamemode);

        for(FancyListObject f : fancyListObjects){
            BoosterExtended b = (BoosterExtended) f;
            if(b.getGameType() == gameType)
                b.setGameModeTipped(player);
        }
    }

    public void onBoosterResponse(ArrayList<Booster> boosters) {

        LoggerHelper.logInfo("[BoosterDisplay]: Got a booster response!");

        // we aren't loading anymore
        if(boosters != null) {
            for(Booster b : boosters){
                GameType gameType = GameType.getTypeByID(b.getGameID());
                Boolean found = false;
                if(b.getRemainingTime() < b.getTotalLength()) {
                    for(FancyListObject fco : fancyListObjects){
                        BoosterExtended be = (BoosterExtended) fco;
                        if(be.getGameType() == gameType ){
                            if(be.getBooster() != null  && (be.getBooster().getOwner().equalsIgnoreCase(b.getOwner()))){
                                found = true;
                                break;
                            }
                            be.setCurrentBooster(b);
                            LoggerHelper.logInfo("[BoosterDisplay]: stored booster with ID " + b.getGameID()
                                    +" and owner " + b.getOwner() + " in the boosterdisplay!");
                            found = true;
                            break;
                        }
                    }
                    if(!found){
                        LoggerHelper.logWarn("[BoosterDisplay]: No display found for booster with ID " + b.getGameID()
                                +" and owner " + b.getOwner() + "!");
                    }
                }
            }
        } else {
            LoggerHelper.logWarn("[BoosterDisplay]: No response to requested Boosters!");
        }
    }
}
