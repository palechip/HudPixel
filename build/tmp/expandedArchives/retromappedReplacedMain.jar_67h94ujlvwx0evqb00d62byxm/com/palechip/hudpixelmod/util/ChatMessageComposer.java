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

import net.minecraft.event.ClickEvent.Action;
/**
 * A mighty chat message manager. His nickname is Skype. ;)
 * @author palechip
 *
 */
public class ChatMessageComposer {
    private static IChatComponent CHAT_PREFIX;
    public static String SEPARATION_MESSAGE = "\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC";
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
        ChatStyle style = this.chatComponent.func_150256_b();
        switch (formatting) {
        case ITALIC:
            style.func_150217_b(true);
            break;
        case BOLD:
            style.func_150227_a(true);
            break;
        case UNDERLINE:
            style.func_150228_d(true);
            break;
        case OBFUSCATED:
            style.func_150237_e(true);
            break;
        case STRIKETHROUGH:
            style.func_150225_c(true);
            break;
        default:
            style.func_150238_a(formatting);
            break;
        }
        this.chatComponent.func_150255_a(style);
        
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
        ChatStyle style = this.chatComponent.func_150256_b();
        
        style.func_150241_a(new ClickEvent(action, execute));
        style.func_150209_a(new HoverEvent(HoverEvent.Action.SHOW_TEXT, description.assembleMessage(false)));
        
        this.chatComponent.func_150255_a(style);
        
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
        ChatStyle style = this.chatComponent.func_150256_b();
        
        style.func_150209_a(new HoverEvent(HoverEvent.Action.SHOW_TEXT, text.assembleMessage(false)));
        
        this.chatComponent.func_150255_a(style);
        
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
            FMLClientHandler.instance().getClientPlayerEntity().func_145747_a(this.assembleMessage(prefix));
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
            result = CHAT_PREFIX.func_150259_f();
        } else if(this.appendedMessages == null) {
            // Nothing to append
            return this.chatComponent;
        } else {
            // this step is important so that the appended messages don't inherit the style
            result = new ChatComponentText("");
        }
        
        // add the main message
        result.func_150257_a(this.chatComponent);
        // and add all appended messages
        if(this.appendedMessages != null) {
            for (ChatMessageComposer m : this.appendedMessages) {
                result.func_150257_a(m.chatComponent);
            }
        }
        
        return result;
    }
    
    // Builds the chat prefix
    static {
        IChatComponent name1 = new ChatComponentText("Hud");
        IChatComponent name2 = new ChatComponentText("Pixel");
        name1.func_150256_b().func_150238_a(EnumChatFormatting.GOLD);
        name2.func_150256_b().func_150238_a(EnumChatFormatting.YELLOW);
        
        CHAT_PREFIX = new ChatComponentText("[").func_150257_a(name1).func_150257_a(name2).func_150257_a(new ChatComponentText("] "));
    }
    
    /**
     * Prints a Hypixel style separaton message using the provided color.
     */
    public static void printSeparationMessage(EnumChatFormatting color) {
        new ChatMessageComposer(SEPARATION_MESSAGE, color).addFormatting(EnumChatFormatting.BOLD).send(false);
    }
}
