package com.palechip.hudpixelmod.detectors;

import com.palechip.hudpixelmod.games.Game;
import com.palechip.hudpixelmod.games.GameManager;
import com.palechip.hudpixelmod.modulargui.IHudPixelModularGuiProviderBase;
import com.palechip.hudpixelmod.modulargui.ModularGuiHelper;
import com.palechip.hudpixelmod.util.GameType;
import com.palechip.hudpixelmod.util.ScoreboardReader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.regex.Pattern;

public class GameDetector {
    public static final Pattern LOBBY_MATCHER = Pattern.compile("\\w*lobby\\d+");
    public static final char COLOR_CHAR = '\u00A7';
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf(COLOR_CHAR) + "[0-9A-FK-OR]");
    private static GameType currentGameType = GameType.UNKNOWN;
    private static Game currentGame;

    public static boolean doesGameTypeMatchWithCurrent(GameType type) {
        switch (type) {
            case UNKNOWN:
                return currentGameType == GameType.UNKNOWN;
            case ALL_GAMES:
                return true;

            case QUAKECRAFT:
                return currentGameType == GameType.QUAKECRAFT;

            case THE_WALLS:
                return currentGameType == GameType.THE_WALLS;

            case PAINTBALL:
                return currentGameType == GameType.PAINTBALL;

            case BLITZ:
                return currentGameType == GameType.BLITZ;

            case TNT_GAMES:
            case BOW_SPLEEF:
            case TNT_RUN:
            case TNT_WIZARDS:
            case TNT_TAG:
            case ANY_TNT:
                return currentGameType == GameType.ANY_TNT;

            case VAMPIREZ:
                return currentGameType == GameType.VAMPIREZ;

            case MEGA_WALLS:
                return currentGameType == GameType.MEGA_WALLS;

            case ARENA:
                return currentGameType == GameType.ARENA;

            case UHC:
                return currentGameType == GameType.UHC;

            case COPS_AND_CRIMS:
                return currentGameType == GameType.COPS_AND_CRIMS;

            case WARLORDS:
                return currentGameType == GameType.WARLORDS;

            case ARCADE_GAMES:
            case BLOCKING_DEAD:
            case BOUNTY_HUNTERS:
            case BUILD_BATTLE:
            case CREEPER_ATTACK:
            case DRAGON_WARS:
            case ENDER_SPLEEF:
            case FARM_HUNT:
            case GALAXY_WARS:
            case PARTY_GAMES_1:
            case PARTY_GAMES_2:
            case TRHOW_OUT:
            case TURBO_KART_RACERS:
            case ANY_ARCADE:
            case FOOTBALL:
                return currentGameType == GameType.ANY_ARCADE;

            case SPEED_UHC:
                return currentGameType == GameType.SPEED_UHC;

            case CRAZY_WALLS:
                return currentGameType == GameType.CRAZY_WALLS;

            case SMASH_HEROES:
            case SMASH_HEROES_WOSPACE:
                return currentGameType == GameType.SMASH_HEROES || currentGameType == GameType.SMASH_HEROES_WOSPACE;

            case SKYWARS:
                return currentGameType == GameType.SKYWARS;

            default:
                return false;
        }
    }

    public static boolean isLobby() {
        return isLobby;
    }

    public static GameType getCurrentGameType() {
        return currentGameType;
    }

    public static Game getCurrentGame() {
        return currentGame = currentGameType == GameType.UNKNOWN ? Game.NO_GAME : GameManager.getGameManager().createGame(currentGameType);
    }

    int cooldown = 0;
    private boolean schedule = false;
    static {
        MinecraftForge.EVENT_BUS.register(new GameDetector());
    }
    @SubscribeEvent
    public void onServerChange(EntityJoinWorldEvent event) {
        if(!(event.entity instanceof EntityPlayerSP)) return;
        EntityPlayerSP player = (EntityPlayerSP) event.entity;
        player.sendChatMessage("/whereami");
        cooldown = 5;

    }

    int scheduleWhereami = -1;
    @SubscribeEvent
    public void onLogin(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        scheduleWhereami = 10;
    }

    public static String stripColor(String input) {
        if (input == null)
            return null;
        return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }


    public void update(String s) {
        s = s.toLowerCase();
        switch (s) {
            case "hypixel":
                currentGameType = GameType.UNKNOWN; //main lobby
                break;
            case "hypixel.net":
                ScoreboardReader.resetCache();
                schedule = true;
                break;
            case "":
            case " ":
                schedule = true;
                break;
            case " smash heroes":
            case "smash heroes":
                currentGameType = GameType.SMASH_HEROES;
            default:
                schedule = false;

                GameType game = currentGameType;
                for (GameType type : GameType.values())
                    if (s.toLowerCase().replace(" ", "").contains(type.scoreboardName.toLowerCase().replace(" ", ""))) {
                        currentGameType = type;
                        isLobby = false;
                        ModularGuiHelper.providers.forEach(IHudPixelModularGuiProviderBase::setupNewGame);
                        ModularGuiHelper.providers.forEach(IHudPixelModularGuiProviderBase::onGameStart);
                    }
                if(game != currentGameType && Minecraft.getMinecraft().thePlayer != null) {
                    //success!
                    //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Changed server! Game is now " + currentGameType));
                } else {
                    currentGameType = GameType.UNKNOWN;
                    schedule = true;
                }

                cooldown = -1;
                break;
        }
    }

    @SubscribeEvent
    public void tickly(TickEvent.ClientTickEvent event) {
        String title = ScoreboardReader.getScoreboardTitle();
        title = stripColor(title).toLowerCase();
        cooldown--;
        scheduleWhereami--;
        if(schedule || cooldown == 0) update(title);
        if(scheduleWhereami == 0 && Minecraft.getMinecraft().thePlayer != null) {
            scheduleWhereami = -1;
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/whereami");
        }
    }

    private static boolean isLobby = false;
    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        if(message.toLowerCase().contains("currently on server".toLowerCase())) {
            if(LOBBY_MATCHER.asPredicate().test(message)) { //lobby
                isLobby = true;
                ModularGuiHelper.providers.forEach(IHudPixelModularGuiProviderBase::onGameEnd);
            }
            event.setCanceled(true);
        }
    }
}
