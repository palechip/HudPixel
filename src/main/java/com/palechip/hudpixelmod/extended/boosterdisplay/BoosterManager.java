package com.palechip.hudpixelmod.extended.boosterdisplay;

import com.palechip.hudpixelmod.extended.util.gui.FancyListManager;
import com.palechip.hudpixelmod.extended.util.gui.FancyListObject;
import com.palechip.hudpixelmod.util.GameType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

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
public class BoosterManager extends FancyListManager {

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

    @Override
    public void onRender(){
        if(Minecraft.getMinecraft().currentScreen instanceof GuiChat)
            this.renderDisplay();
    }

    @Override
    public void onClientTick(){
        for(FancyListObject b : fancyListObjects){
            b.onClientTick();
        }
    }

    @Override
    public void onChatReceived(ClientChatReceivedEvent e) {
        String chat = e.message.getUnformattedText();
        if(!chat.contains("You tipped ") || chat.contains(":")) return;

        System.out.println("TIPPED!");

        String gamemode = "";
        String[] split = chat.split(" ");
        gamemode = split[4];
        for(int i = 5; i < split.length; i++)
            gamemode+=(" " + split[i]);

        System.out.println(gamemode);

        GameType gameType = GameType.getTypeByName(gamemode);

        for(FancyListObject f : fancyListObjects){
            BoosterExtended b = (BoosterExtended) f;
            if(b.getGameType() == gameType)
                b.setGameModeTipped();
        }
    }

}
