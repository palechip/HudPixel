package com.palechip.hudpixelmod.detectors.newsystem;

import com.palechip.hudpixelmod.games.Game;
import com.palechip.hudpixelmod.games.GameManager;
import com.palechip.hudpixelmod.util.GameType;
import com.palechip.hudpixelmod.util.ScoreboardReader;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.regex.Pattern;

public class GameDetector {
    public static final Pattern LOBBY_MATCHER = Pattern.compile("\\w*lobby\\d+");
    public static final char COLOR_CHAR = '\u00A7';
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf(COLOR_CHAR) + "[0-9A-FK-OR]");
    public static Game currentGame = Game.NO_GAME;
    private boolean schedule = false;
    static {
        MinecraftForge.EVENT_BUS.register(new GameDetector());
    }
    @SubscribeEvent
    public void onServerChange(EntityJoinWorldEvent event) {
        if(!(event.entity instanceof EntityPlayerSP)) return;
        EntityPlayerSP player = (EntityPlayerSP) event.entity;
       // player.sendChatMessage("/whereami");
        String title = ScoreboardReader.getScoreboardTitle();
        title = stripColor(title).toLowerCase();
        update(title);

    }

    public static String stripColor(String input) {
        if (input == null)
            return null;
        return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }


    public void update(String s) {
        switch (s) {
            case "hypixel":
               // currentGame = Game.NO_GAME; //main lobby
                break;
            case "hypixel.net":
                schedule = true;
                break;
            default:
                schedule = false;
                for (GameType type : GameType.values())
                    if (type.getName().toLowerCase().equals(s.toLowerCase()))
                        currentGame = GameManager.getGameManager().createGame(type);
                System.out.println("~~~~" + currentGame);
                break;
        }
    }

    @SubscribeEvent
    public void tickly(TickEvent.ClientTickEvent event) {
        String title = ScoreboardReader.getScoreboardTitle();
        title = stripColor(title).toLowerCase();
        if(schedule) update(title);
    }

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) {
        /*String message = event.message.getUnformattedText();
        if(message.toLowerCase().contains("currently on server".toLowerCase())) {
            if(LOBBY_MATCHER.asPredicate().test(message))  //lobby
                currentGame = Game.NO_GAME;
            event.setCanceled(true);
        }*/
    }
}
