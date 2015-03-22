package com.palechip.hudpixelmod.chat;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

import com.palechip.hudpixelmod.HudPixelConfig;
import com.palechip.hudpixelmod.util.ChatMessageComposer;

import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class LobbyCommandAutoCompleter {
    public static final String LOBBY_CONFIRMATION_MESSAGE = "Are you sure? Type /lobby again if you really want to quit.";

    public LobbyCommandAutoCompleter() {
    	
    }
    
    public void onChatReceived(ClientChatReceivedEvent event) {
    	    String message = event.message.getUnformattedText();
        // get the message asking for /lobby as confirmation
        if(HudPixelConfig.autoCompleteSecondLobbyCmd && message.equals(LOBBY_CONFIRMATION_MESSAGE)) {
             Minecraft.getMinecraft().thePlayer.sendChatMessage("/lobby");
             new ChatMessageComposer("Automatically sending lobby for confirmation!", EnumChatFormatting.GREEN).send();
             //delete the original message.
            event.setCanceled(true);
            
            return;
        }
    }
}
