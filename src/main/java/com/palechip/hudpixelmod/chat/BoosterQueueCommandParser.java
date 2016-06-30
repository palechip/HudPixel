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
package com.palechip.hudpixelmod.chat;

import java.util.ArrayList;

import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.api.interaction.representations.Booster;
import com.palechip.hudpixelmod.games.GameConfiguration;
import com.palechip.hudpixelmod.games.GameManager;
import com.palechip.hudpixelmod.gui.BoosterDisplay;
import com.palechip.hudpixelmod.util.MultiLineCommandParser;

/**
 *Allows to interpret the /booster queue command and use it's information for the BoosterDisplay
 * @author palechip
 */
public class BoosterQueueCommandParser extends MultiLineCommandParser {
    private ArrayList<Booster> activeBoosters;
    private BoosterDisplay display;

    public BoosterQueueCommandParser(BoosterDisplay display) {
        super("                          Network BoosterExtended Queue", "(.* - No Triple Coins Boosters queued)|(.* - Triple Coins from.*)");
        this.display = display;
    }

    @Override
    protected void onCommandReceived(String commandMessage) {
        if(!commandMessage.contains(" - No Triple Coins Boosters queued")) {
            // cut the parts before the relevant name
            String playerName = commandMessage.substring(commandMessage.indexOf("from ") + 5);
            // if there is only one booster queued, the name is already correct
            if(playerName.contains(",")) {
                // otherwise we have to remove the following names
                playerName = playerName.substring(0, playerName.indexOf(","));
            }
            // get the game name
            String gameName = commandMessage.substring(0, commandMessage.indexOf("-") - 1);
            // this allows to generate error messages if we fail to find the game.
            boolean foundGame = false;
            // go through all game configs
            for(GameConfiguration config : GameManager.getGameManager().getConfigurations()) {
                // compare with the official name and the short name
                if(gameName.equals(config.getOfficialName()) || gameName.equals(config.getShortName())) {
                    foundGame = true;
                    // add the booster to the collection currency
                    this.activeBoosters.add(new Booster(playerName, config.getModID()));
                    // save some resources by stopping the loop
                    break;
                }
            }
            // output an error if the game wasn't found
            if(!foundGame) {
                HudPixelMod.instance().logError("Couldn't find the game called \"" + gameName + "\" in the /booster queue output. Report this to palechip!");
            }
        }
    }

    @Override
    protected void onCommandStart() {
        // create or empty the collection array
        if(this.activeBoosters == null) {
            this.activeBoosters = new ArrayList<Booster>();
        } else {
            this.activeBoosters.clear();
        }
    }

    @Override
    protected void onCommandEnd() {
        // inform the BoosterDisplay
        this.display.onBoosterQueueCommandParsed(this.activeBoosters);
    }

}
