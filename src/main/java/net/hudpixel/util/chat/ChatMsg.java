/*
 * Part of HudPixel Reloaded. (https://github.com/HudPixel/HudPixel)
 *
 * License see: license.md
 *
 * Additionally the author allows the usage under the MIT License.
 *
 * MIT License
 *
 * Copyright (c) 2017 palechip
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */


package net.hudpixel.util.chat;


import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * Compose pretty formatted chat messages with ease.
 *
 * ChatMsg is immutable and support templating like String.format().
 * It will also cache messages if no formatting is used.
 * The class is pretty lenient and won't usually throw exceptions. Make sure the result looks like you imagined.
 *
 * Usage:
 * For simple messages simply chain formatting and text in the constructor.
 * Don't forget to call send afterwards. Will by default add the [HudPixel] tag in front of the message.
 * Using java.net.URL, you can make the next element be a link.
 *
 * For fancier messages you can use ChatMsgPart. It supports click and hover events.
 *
 * Examples:
 *
 * Create a new line.
 * new ChatMsg().send(false); // No [HudPixel] tag.
 *
 * Simple formatted message.
 * new ChatMsg(TextFormatting.RED, "Hello ", TextFormatting.RED, TextFormatting.BOLD, "World!").send();
 *
 * Link to hypixel.
 * new ChatMsg(TextFormatting.BLUE, new URL("https://www.hypixel.net"), "Hypixel Website").send();
 *
 * Fancy hover message.
 * new ChatMsg(new ChatMsgPart(TextFormatting.YELLOW, "Dare to hover?").hover(TextFormatting.RED, "SURPRISE!")).send();
 *
 * As a template. (Note: Kotlin may need \$ for escaping)
 * ChatMsg template = new ChatMsg(TextFormatting.GRAY, "Hello %1$s");
 * template.sendFormat("palechip");
 *
 * @author palechip
 */
public class ChatMsg {
    protected final List<ChatMsgPart> parts;

    // If no string modification is applied when sending, the result will be cached.
    private ITextComponent cache = null;

    // The prefix
    protected final static ChatMsg HUDPIXEL_PREFIX = new ChatMsg("[", TextFormatting.GOLD, "Hud", TextFormatting.YELLOW, "Pixel", "] ");

    /**
     * Create a complex chat message providing a sequence of the following objects:
     * String, TextFormatting, java.net.URL, ChatMsgPart and ChatMsg.
     *
     * The constructions has to obey the following rules:
     * 1. TextFormatting modifiers are applied to the next String, ChatMsgPart.
     * 2. URLs make the next String, ChatMsgPart link to the specified link.
     * 3. The String, ChatMsgPart or ChatMsg are considered in order of their occurrence.
     * 4. All String components may contains parts to formatted using String.format() when sending.
     *      However links and hover messages will not be formatted.
     * 5. No formatting or urls in front of ChatMsg.
     *
     * Don't forget to actually send the message! Check the class documentation for usage examples and more.
     *
     * ChatMsg is immutable.
     * @param args The sequence of objects as specified.
     */
    public ChatMsg(Object... args) {

        // Use LinkedList due to sequential access nature.
        this.parts = new LinkedList<>();

        // Save all formattings to apply.
        LinkedList<TextFormatting> formattingBuffer = new LinkedList<>();
        URL urlBuffer = null;

        // Go through all parameters in order.
        for(Object parameter : args) {
            if(parameter instanceof TextFormatting) {
                // Add to buffer.
                formattingBuffer.add((TextFormatting)parameter);
            } else if(parameter instanceof URL) {
                // Add to buffer.
                urlBuffer = (URL)parameter;
            } else if(parameter instanceof ChatMsg) {
                // Add all parts. Works because ChatMsgParts are immutable. No copy necessary.
                this.parts.addAll(((ChatMsg)parameter).parts);
            } else if(parameter instanceof ChatMsgPart) {
                ChatMsgPart result = (ChatMsgPart)parameter;

                // Apply all formattings.
                for(TextFormatting formatting : formattingBuffer) {
                    result = result.addFormatting(formatting);
                }

                // Apply url if available.
                if(urlBuffer != null) {
                    result = result.link(urlBuffer);
                }

                // Clear all buffers.
                formattingBuffer.clear();
                urlBuffer = null;

                // Add the part.
                this.parts.add(result);
            } else if(parameter instanceof String) {
                ChatMsgPart result;

                // Use special constructor to create less objects.
                if(formattingBuffer.size() == 1) {
                    result = new ChatMsgPart(formattingBuffer.getFirst(), (String)parameter);
                } else if(!formattingBuffer.isEmpty()) {
                    // There are 2 or more formattings. Use the first 2 for the constructor.
                    result = new ChatMsgPart(formattingBuffer.pop(), formattingBuffer.pop(), (String) parameter);

                    // Apply the remaining formattings.
                    for(TextFormatting formatting : formattingBuffer) {
                        result = result.addFormatting(formatting);
                    }
                } else {
                    // No formatting.
                    result = new ChatMsgPart((String) parameter);
                }

                // Apply url if available.
                if(urlBuffer != null) {
                    result = result.link(urlBuffer);
                }

                // Clear all buffers.
                formattingBuffer.clear();
                urlBuffer = null;

                // Add the part.
                this.parts.add(result);


            } else {
                throw new RuntimeException("Invalid chat message construction. Constructor doesn't support: " + parameter.toString());
            }
        }
    }




    /**
     * Sends this message to the sender. Will apply #String.format() on the message.
     * @param sender The recipient of the message. Usually this will be the player.
     * @param usePrefix Set to true if the [HudPixel] prefix should be used.
     * @param formatArgs The arguments which will be passed to #String.format().
     */
    public void sendFormat(ICommandSender sender, boolean usePrefix, Object... formatArgs) {
        // Assemble using cache (if possible) and send.
        sender.sendMessage(this.asssemble(usePrefix, true, formatArgs));
    }

    /**
     * Join all the parts to a {@link ITextComponent}.
     * @param addPrefix Should the [HudPixel] prefix be added.
     * @param useCache Disable the cache if {@link ITextComponent} is to be passed outside. Won't load and write to cache.
     * @param args All formatting parameters.
     * @return Returns the assembled ITextComponent.
     */
    protected ITextComponent asssemble(boolean addPrefix, boolean useCache, Object... args) {
        // Check if a cache entry is usable.
        if((args == null || args.length == 0) && useCache && this.cache != null) {
            return this.cache;
        }

        ITextComponent result;

        if(addPrefix) {
            // Obtain a new Prefix component.
            result = HUDPIXEL_PREFIX.assembleNoCache();
        } else {
            // Use an empty message to avoid formatting inheritance.
            result = new TextComponentString("");
        }

        // Chain all the parts. Pass the formatting parameters to be applied.
        for(ChatMsgPart part : this.parts) {
            result.appendSibling(part.assemble(args));
        }

        // Update the cache.
        if((args == null || args.length == 0) && useCache) {
            this.cache = result;
        }

        return result;
    }

    /**
     *  Only use if absolutely necessary. Normally the send() or sendFormat() methods should be enough.
     *
     * Assebmbles the {@link ITextComponent}. However this won't use the cache to ensure immutability.
     * Doesn't add the [HudPixel] prefix.
     * @return A ITextComponent with the formatted and assembled message.
     */
    public ITextComponent assembleNoCache(Object... args) {
        return this.asssemble(false, false, args);
    }

    //
    // Options for the send and sendFormat methods.
    //

    /**
     * Sends this message to the sender.
     * @param sender The recipient of the message. Usually this will be the player.
     * @param usePrefix Set to true if the [HudPixel] prefix should be used.
     */
    public void send(ICommandSender sender, boolean usePrefix) {
        // Just don't format.
        this.sendFormat(sender, usePrefix, null);
    }

    /**
     * Sends this message to the player. Will use the [HudPixel] prefix.
     */
    public void send() {
        // Use main send.
        this.send(true);
    }

    /**
     * Sends this message to the player.
     * @param usePrefix Set to true if the [HudPixel] prefix should be used.
     */
    public void send(boolean usePrefix) {
        // Get the player.
        ICommandSender sender = FMLClientHandler.instance().getClientPlayerEntity();
        // Use main send.
        this.send(sender, usePrefix);
    }

    /**
     * Send this message to the given sender. Will use the [HudPixel] prefix.
     * @param sender The recipient of the message. Usually this will be the player.
     */
    public void send(ICommandSender sender) {
        // Use main send.
        this.send(sender, true);
    }

    /**
     * Sends this message to the player. Will use the [HudPixel] prefix. Will apply #String.format() on the message.
     * @param formatArgs The arguments which will be passed to #String.format().
     */
    public void sendFormat(Object... formatArgs) {
        // Use main sendFormat
        this.sendFormat(true, formatArgs);
    }

    /**
     * Sends this message to the player. Will apply #String.format() on the message.
     * @param usePrefix Set to true if the [HudPixel] prefix should be used.
     * @param formatArgs The arguments which will be passed to #String.format().
     */
    public void sendFormat( boolean usePrefix, Object... formatArgs) {
        // Get the player.
        ICommandSender sender = FMLClientHandler.instance().getClientPlayerEntity();
        // Use main sendFormat.
        this.sendFormat(sender, usePrefix, formatArgs);
    }

    /**
     * Sends this message to the sender. Will use the [HudPixel] prefix. Will apply #String.format() on the message.
     * @param sender The recipient of the message. Usually this will be the player.
     * @param formatArgs The arguments which will be passed to #String.format().
     */
    public void sendFormat(ICommandSender sender, Object... formatArgs) {
        // Use main sendFormat.
        this.sendFormat(sender, true, formatArgs);
    }
}
