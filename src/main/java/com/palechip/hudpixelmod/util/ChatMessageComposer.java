package com.palechip.hudpixelmod.util;

import java.util.ArrayList;

import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import com.palechip.hudpixelmod.HudPixelMod;

import net.minecraftforge.fml.client.FMLClientHandler;

/**
 * A mighty chat message manager. His nickname is Skype. ;)
 * @author palechip
 *
 */
public class ChatMessageComposer {
    private static IChatComponent CHAT_PREFIX;
    private IChatComponent chatComponent;
    private ArrayList<ChatMessageComposer> appendedMessages;
    
    /**
     * Creates a new ChatMessageComposer.
     * @param text Text of the chat message.
     */
    public ChatMessageComposer(String text) {
        this.chatComponent = new ChatComponentText(text);
    }
    
    /**
     * Creates a new ChatMessageComposer.
     * @param text Text of the chat message.
     * @param color Color of the chat message.
     */
    public ChatMessageComposer(String text, EnumChatFormatting color) {
        this(text);
        this.addFormatting(color);
    }
    
    /**
     * Adds a formatting to the text message. The ChatMessageComposer used is modified.
     * @return The ChatMessageComposer instance in order to make code more compact.
     */
    public ChatMessageComposer addFormatting(EnumChatFormatting formatting) {
        ChatStyle style = this.chatComponent.getChatStyle();
        switch (formatting) {
        case ITALIC:
            style.setItalic(true);
            break;
        case BOLD:
            style.setBold(true);
            break;
        case UNDERLINE:
            style.setUnderlined(true);
            break;
        case OBFUSCATED:
            style.setObfuscated(true);
            break;
        case STRIKETHROUGH:
            style.setStrikethrough(true);
            break;
        default:
            style.setColor(formatting);
            break;
        }
        this.chatComponent.setChatStyle(style);
        
        return this;
    }
    
    /**
     * Append a message to the an existing message. This is used to achieve multiple colors in one line. 
     * @param message message to append
     * @return The ChatMessageComposer instance in order to make code more compact.
     */
    public ChatMessageComposer appendMessage(ChatMessageComposer message) {
        // Make sure appendedMessages gets created
        if(this.appendedMessages == null) {
            this.appendedMessages = new ArrayList<ChatMessageComposer>();
        }
        // Add the message
        this.appendedMessages.add(message);
        // And add messages which were added to the message
        if(message.appendedMessages != null) {
            this.appendedMessages.addAll(message.appendedMessages);
        }
        
        return this;
    }
    
    /**
     * Makes the chat message clickable.
     * @param action Action performed by clicking
     * @param execute URL or command to execute
     * @param description Shown message when hovering over the clickable chat.
     * @return The ChatMessageComposer instance in order to make code more compact.
     */
    public ChatMessageComposer makeClickable(ClickEvent.Action action, String execute, ChatMessageComposer description) {
        ChatStyle style = this.chatComponent.getChatStyle();
        
        style.setChatClickEvent(new ClickEvent(action, execute));
        style.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, description.assembleMessage(false)));
        
        this.chatComponent.setChatStyle(style);
        
        return this;
    }
    
    /**
     * Makes the chat message link to a given url.
     * @param url The linked URL. MAKE SURE IT STARTS WITH HTTP:// or HTTPS://!
     * @return The ChatMessageComposer instance in order to make code more compact.
     */
    public ChatMessageComposer makeLink(String url) {
        // Compose a generic description
        ChatMessageComposer description = new ChatMessageComposer("Click to visit ", EnumChatFormatting.GRAY);
        description.appendMessage(new ChatMessageComposer(url, EnumChatFormatting.AQUA).addFormatting(EnumChatFormatting.UNDERLINE));
        // and make it clickable
        this.makeClickable(Action.OPEN_URL, url, description);
        
        return this;
    }
    
    /**
     * Creates a tooltip like hover text.
     * @param text the message shown then hovering
     * @return The ChatMessageComposer instance in order to make code more compact.
     */
    public ChatMessageComposer makeHover(ChatMessageComposer text) {
        ChatStyle style = this.chatComponent.getChatStyle();
        
        style.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, text.assembleMessage(false)));
        
        this.chatComponent.setChatStyle(style);
        
        return this;
    }
    
    /**
     * Send this message to the player. (with [HudPixel] prefix)
     */
    public void send() {
        this.send(true);
    }
    
    /**
     * Send this message to the player.
     * @param prefix Send the [HudPixel] prefix?
     */
    public void send(boolean prefix) {
        try {
            // send the assebled message to the player
            FMLClientHandler.instance().getClientPlayerEntity().addChatMessage(this.assembleMessage(prefix));
        } catch(Exception e) {
            HudPixelMod.instance().logError("Failed to send chat message");
            e.printStackTrace();
        }
    }
    
    /**
     * Builds an IChatComponent including all appended messages.
     * @param prefix should [HudPixel] be added as chat prefix?
     * @return the IChatComponent containing all appended messages
     */
    protected IChatComponent assembleMessage(boolean prefix) {
        IChatComponent result;
        if(prefix) {
            // Copy the prefix
            result = CHAT_PREFIX.createCopy();
        } else if(this.appendedMessages == null) {
            // Nothing to append
            return this.chatComponent;
        } else {
            // this step is important so that the appended messages don't inherit the style
            result = new ChatComponentText("");
        }
        
        // add the main message
        result.appendSibling(this.chatComponent);
        // and add all appended messages
        if(this.appendedMessages != null) {
            for (ChatMessageComposer m : this.appendedMessages) {
                result.appendSibling(m.chatComponent);
            }
        }
        
        return result;
    }
    
    // Builds the chat prefix
    static {
        IChatComponent name1 = new ChatComponentText("Hud");
        IChatComponent name2 = new ChatComponentText("Pixel");
        name1.getChatStyle().setColor(EnumChatFormatting.GOLD);
        name2.getChatStyle().setColor(EnumChatFormatting.YELLOW);
        
        CHAT_PREFIX = new ChatComponentText("[").appendSibling(name1).appendSibling(name2).appendSibling(new ChatComponentText("] "));
    }
}
