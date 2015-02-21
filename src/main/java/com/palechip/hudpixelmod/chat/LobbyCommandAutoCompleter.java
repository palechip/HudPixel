package com.palechip.hudpixelmod.chat;

import net.minecraft.util.EnumChatFormatting;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.util.ChatMessageComposer;

import net.minecraftforge.fml.client.FMLClientHandler;

public class LobbyCommandAutoCompleter {
    public static final String LOBBY_CONFIRMATION_MESSAGE = "Are you sure? Type /lobby again if you really want to quit.";

    public LobbyCommandAutoCompleter() {
    }
    
    public void onChatMessage(String textMessage, String formattedMessage) {
        // get the message asking for /lobby as confirmation
        if(textMessage.equals(LOBBY_CONFIRMATION_MESSAGE) && HudPixelConfig.autoCompleteSecondLobbyCmd) {
            FMLClientHandler.instance().getClient().thePlayer.sendChatMessage("/lobby");
            new ChatMessageComposer("Automatically sending /lobby for confirmation!", EnumChatFormatting.GREEN).send();
        }
    }
    
}
