package com.palechip.hudpixelmod.detectors;

import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.item.ItemStack;

import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.games.Game;

import cpw.mods.fml.client.FMLClientHandler;

public class GameDetector {
    // null if no game is detected
    private Game currentGame;

    private boolean isGameDetectionStarted = false;
    private boolean isLobbyDetectionStarted = false;

    private boolean isBypassing = false;

    private String bossbarContent = "";

    // this means a lobby where you choose the game and not a pre-game lobby
    private boolean isInLobby = false;
    private static final String COMPASS_NAME = "\u00A7aGame Menu \u00A77(Right Click)";
    private static final String PROFILE_NAME = "\u00A7aYour Profile \u00A77(Right Click)";
    private static final String WITHER_STAR_NAME = "\u00A7aLobby Selector \u00A77(Right Click)";
    private static final String LIMBO_MESSAGE = "You were spawned in Limbo.";
    private static final String MVPPLUS_LAND = "[*] Welcome to Hypixel's MVP+ Land!";
    private static final String HYPIXEL_IP = "mc.hypixel.net";
    private static final String PARTY_DRAG_MESSAGE = "Found a server running";
    private static final String PARTY_WARP_MESSAGE = " summoned you to their server.";

    public void onGuiShow(GuiScreen gui) {
        if(HypixelNetworkDetector.isHypixelNetwork) {
            // prevent exceptions
            if(gui == null) {
                this.isBypassing = false;
                return;
            }

            if(gui instanceof GuiGameOver) {
                this.isBypassing = true;
            }

            // GuiDownloadTerrain is opened when switching between servers, so it has to trigger the detection
            if(!(this.isGameDetectionStarted || this.isLobbyDetectionStarted) && gui instanceof GuiDownloadTerrain) {
                if(!this.isBypassing) {
                    // start either lobby or game detection
                    if(this.currentGame != null) {
                        this.isLobbyDetectionStarted = true;
                    } else {
                        this.isGameDetectionStarted = true;
                        this.isInLobby = false;
                    }
                }
            }
        }
    }

    public void onChatMessage(String textMessage, String formattedMessage) {
        if(this.isGameDetectionStarted) {
            // try detecting the game using the chat tag
            // if the message contains a game tag
            if(textMessage.startsWith("[") && textMessage.contains("]")) {
                // extract the content
                // e.g. [Quake] -> Quake
                String gameTag = textMessage.substring(textMessage.indexOf("[") + 1, textMessage.indexOf("]"));

                // check all games for a matching tag
                for(Game game : Game.getGames()) {
                    if(game.getChatTag() != null && !game.getChatTag().isEmpty() && game.getChatTag().equals(gameTag)) {
                        // we found the game
                        this.currentGame = game;
                        this.isGameDetectionStarted = false;
                        this.currentGame.setupNewGame();
                        break;
                    }
                }
            }
            // check for limbo and MVP+ land
            if(this.isLobbyDetectionStarted || this.isGameDetectionStarted) {
                if(textMessage.equals(LIMBO_MESSAGE) || textMessage.equals(MVPPLUS_LAND)) {
                    this.isInLobby = true;
                    this.isLobbyDetectionStarted = false;
                    this.isGameDetectionStarted = false;
                    if(this.currentGame != null) {
                        // and terminate the game if it wasn't already
                        this.currentGame.endGame();
                        this.currentGame = null;
                    }
                }

            }
            // check for party leaders dragging you directly from a game to another
            if(this.currentGame != null) {
                if(textMessage.contains(PARTY_DRAG_MESSAGE) || textMessage.contains(PARTY_WARP_MESSAGE)) {
                    this.isInLobby = false;
                    this.isGameDetectionStarted = true;
                }
            }
            // we didn't find anything. Retry with the next chat message...
        }
    }

    public void onClientTick() {
        // check if the bossbar updated
        if(BossStatus.bossName != null && !this.bossbarContent.equals(BossStatus.bossName)) {
            this.bossbarContent = BossStatus.bossName;
            this.onBossbarChange();
        }

        // lobby detection is also done when game detection is active
        // because players can change from lobby to lobby
        if((this.isLobbyDetectionStarted || this.isGameDetectionStarted) && FMLClientHandler.instance().getClientPlayerEntity() != null && FMLClientHandler.instance().getClientPlayerEntity().inventory != null) {
            // detect lobbies
            // the mod assumes that the player is in a lobby when he has the lobby compass, the lobby clock or the lobby selection star
            ItemStack[] inventory = FMLClientHandler.instance().getClientPlayerEntity().inventory.mainInventory;

            // limbo and MVP+ land count as lobby as well
            if((inventory[0] != null && inventory[0].getDisplayName().equals(COMPASS_NAME)) || (inventory[1] != null &&inventory[1].getDisplayName().equals(PROFILE_NAME)) ||  (inventory[8] != null && inventory[8].getDisplayName().equals(WITHER_STAR_NAME))) {
                // increase the chance of me noticing when a name was changed
                if(HudPixelMod.IS_DEBUGGING) {
                    if(inventory[0] != null && !inventory[0].getDisplayName().equals(COMPASS_NAME)) {
                        HudPixelMod.instance().logDebug("THE LOBBY DETECTION ITEM NAME FOR THE COMPASS MIGHT HAVE CHANGED!!!");
                        HudPixelMod.instance().logDebug("Actual Name: \"" + inventory[0].getDisplayName() + "\" Saved Name: \"" + COMPASS_NAME + "\"");
                    }
                    if(inventory[1] != null && !inventory[1].getDisplayName().equals(PROFILE_NAME)) {
                        HudPixelMod.instance().logDebug("THE LOBBY DETECTION ITEM NAME FOR THE PROFILE MIGHT HAVE CHANGED!!!");
                        HudPixelMod.instance().logDebug("Actual Name: \"" + inventory[1].getDisplayName() + "\" Saved Name: \"" + PROFILE_NAME + "\"");
                    }
                    if(inventory[8] != null && !inventory[8].getDisplayName().equals(WITHER_STAR_NAME)) {
                        HudPixelMod.instance().logDebug("THE LOBBY DETECTION ITEM NAME FOR THE WITHER STAR MIGHT HAVE CHANGED!!!");
                        HudPixelMod.instance().logDebug("Actual Name: \"" + inventory[8].getDisplayName() + "\" Saved Name: \"" + WITHER_STAR_NAME + "\"");
                    }
                }

                this.isInLobby = true;
                this.isLobbyDetectionStarted = false;
                this.isGameDetectionStarted = false;
                if(this.currentGame != null) {
                    // and terminate the game if it wasn't already
                    this.currentGame.endGame();
                    this.currentGame = null;
                }
            }
        }
    }

    private void onBossbarChange() {
        if(this.isGameDetectionStarted) {
            // if there is a boss bar
            if(BossStatus.bossName != null) {
                // check all games for a matching name
                for(Game game : Game.getGames()) {
                    // please note the use of contains() and not equals()
                    // in a pre-game lobby there will always be the IP in the bar. (because youtube)
                    if(game.getBossbarName() != null && !game.getBossbarName().isEmpty() && BossStatus.bossName.contains(game.getBossbarName()) && BossStatus.bossName.toLowerCase().contains(this.HYPIXEL_IP)) {
                        // we found the game
                        this.currentGame = game;
                        this.isGameDetectionStarted = false;
                        this.currentGame.setupNewGame();
                        break;
                    }
                }
            }
        }
    }

    public boolean isInLobby() {
        return this.isInLobby;
    }

    public boolean isGameDetectionStarted() {
        return this.isGameDetectionStarted;
    }

    public Game getCurrentGame() {
        return this.currentGame;
    }
}
