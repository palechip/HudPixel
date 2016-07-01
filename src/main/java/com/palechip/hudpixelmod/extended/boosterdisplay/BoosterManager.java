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

public class BoosterManager extends FancyListManager implements BoosterResponseCallback{

//######################################################################################################################

    private static final int REQUEST_COOLDOWN = 60 * 1000 * 5; // = 5min

    /**
     * Enter a  new gamemode with booster here, the system will add the booster then!
     * Also please upload the gameicon to the resource folder and link it in util.GameIconLoader
     * Also please add the new gamemode with the right ID and right name (put there the name it says
     * when tipping somebody in this gamemode) to the GameType enum class.
     **/
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

//######################################################################################################################

    private long lastRequest;

    /**
     * sets the settings for the fancyListManager and also generates all boosters
     * in the gamesWithBooster array.
     */
    public BoosterManager(){
        super(5); //this sets how many boosters are displayed at once you can change that
        for(GameType g : gamesWithBooster){
            this.fancyListObjects.add(new BoosterExtended(g));
        }
    }

    /**
     * Well you can do some stuff here befor rendering the display
     * You still have to call the renderDisplay() method ... otherwise there
     * will be nothing shown.
     */
    @Override
    public void onRender(){
        if(Minecraft.getMinecraft().currentScreen instanceof GuiChat)
            this.renderDisplay();
    }

    /**
     * do some things while the gametick ... you should also send the tip
     * to each FancyListObject
     */
    @Override
    public void onClientTick(){
        requestBoosters(false);
        for(FancyListObject b : fancyListObjects){
            b.onClientTick();
        }
    }

    /**
     * Filters out the tipped message and notifies the BoosterExtended that is had been tipped.
     * @param e The chatEvent
     */
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

    /**
     * This requests the boosters via the api interaction
     * @param forceRequest Set this to true if you want to force the request
     */
    void requestBoosters(Boolean forceRequest){
        if(HudPixelConfig.useAPI && HudPixelConfig.displayNetworkBoosters) {
            // check if enough time has past
            if(System.currentTimeMillis() > lastRequest + REQUEST_COOLDOWN  || forceRequest) {
                // save the time of the request
                lastRequest = System.currentTimeMillis();
                // tell the queue that we need boosters
                Queue.getInstance().getBoosters(HudPixelExtended.boosterManager);
            }
        }
    }

    /**
     * This method gets called when there is a booster response
     * Sorry for this messy if-for but somehow it works :P
     * @param boosters the boosters parsed by the callback
     */
    public void onBoosterResponse(ArrayList<Booster> boosters) {
        // we aren't loading anymore
        if(boosters != null) {
            for(Booster b : boosters){
                GameType gameType = GameType.getTypeByID(b.getGameID());
                Boolean found = false;
                if(b.getRemainingTime() < b.getTotalLength()) {
                    for(FancyListObject fco : fancyListObjects){
                        BoosterExtended be = (BoosterExtended) fco;
                        if(be.getGameType() == gameType ){
                            if(be.getBooster() != null  && !(be.getBooster().equals(b))){
                                be.setCurrentBooster(b);
                                LoggerHelper.logInfo("[BoosterDisplay]: stored booster with ID " + b.getGameID()
                                        +" and owner " + b.getOwner() + " in the boosterdisplay!");
                            }
                            found = true; break;
                        }
                    }
                    if(!found) LoggerHelper.logWarn("[BoosterDisplay]: No display found for booster with ID " + b.getGameID()
                                +" and owner " + b.getOwner() + "!");
                }
            }
        } else LoggerHelper.logWarn("[BoosterDisplay]: The buuster response was NULL!");
    }
}
