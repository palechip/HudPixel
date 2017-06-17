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

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.net.URL;

/**
 * A immutable object which allows can represent a simple chat message.
 * Can create {@link net.minecraft.util.text.ITextComponent} for a given message with a given style.
 *
 * Use with {@link ChatMsg}.
 *
 * Offers constructors with a similar syntax to {@link ChatMsg}.
 * Allows to easily modify the formatting.
 * Allows to add click and hover events.
 *
 * @author palechip
 */
public class ChatMsgPart {
    // Settings for the auto hover events.
    private static final String DEFAULT_LINK_HOVER_MSG = "Click to visit: ";
    private static final TextFormatting DEFAULT_LINK_HOVER_COLOR = TextFormatting.AQUA;
    private static final String DEFAULT_FILE_HOVER_MSG = "Click to open: ";
    private static final TextFormatting DEFAULT_FILE_HOVER_COLOR = TextFormatting.AQUA;
    private static final String DEFAULT_CMD_HOVER_MSG = "Send: ";
    private static final TextFormatting DEFAULT_CMD_HOVER_COLOR = TextFormatting.WHITE;

    protected final String text;
    protected final Style style;

    /**
     * The internal constructor which only optionally copies the style.
     * @param text The text for the message.
     * @param style The style for the message. May contain click and hover events.
     * @param noCopy Determine if the style must be copied. (if it comes from outside)
     */
    protected ChatMsgPart(String text, Style style, boolean noCopy) {
        if(noCopy) {
            this.style = style;
        } else {
            this.style = style.createDeepCopy();
        }

        this.text = text;
    }

    /**
     * Adds a formatting modifier to the message. Don't use reset.
     * @param formatting The formatting modifier.
     * @return A new object with the added formatting.
     */
    public ChatMsgPart addFormatting(TextFormatting formatting) {
        // Copy the own style.
        Style updated = this.style.createDeepCopy();

        // Apply the update.
        updated = ChatMsgPart.addFormattingToStyle(updated, formatting);

        // Create the new object.
        return new ChatMsgPart(this.text, updated, true);
    }

    /**
     * Make the message link to a url. MAKE SURE IT STARTS WITH HTTP:// or HTTPS://!
     * @param url A click on the message will open this url.
     * @param noAutoHover Disable the automatic hover message which will indicate that is a link. Use to create a custom one.
     * @return A new object which links to the url.
     */
    public ChatMsgPart link(String url, boolean noAutoHover) {
        // Copy the own style.
        Style updated = this.style.createDeepCopy();

        // Create a click event.
        updated.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));

        if(!noAutoHover) {
            // Create a fitting hover event.
            updated.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new ChatMsgPart(DEFAULT_LINK_HOVER_COLOR, DEFAULT_LINK_HOVER_MSG + url).assemble()));
        }

        // Create the new object.
        return new ChatMsgPart(this.text, updated, true);
    }

    /**
     * Make the message open a file.
     * @param file A click on the message will open this file.
     * @param noAutoHover Disable the automatic hover message which will indicate that is opens a link.
     * @return A new object which opens the file.
     */
    public ChatMsgPart file(String file, boolean noAutoHover) {
        // Copy the own style.
        Style updated = this.style.createDeepCopy();

        // Create a click event.
        updated.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file));

        if(!noAutoHover) {
            // Create a fitting hover event.
            updated.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new ChatMsgPart(DEFAULT_FILE_HOVER_COLOR, DEFAULT_FILE_HOVER_MSG + file).assemble()));
        }

        // Create the new object.
        return new ChatMsgPart(this.text, updated, true);
    }

    /**
     * Make the message run a command when clicked.
     * @param command The command to run.
     * @param noAutoHover Disable the automatic hover message which will indicate that the message will run a command.
     * @return A new object which runs the command.
     */
    public ChatMsgPart cmdRun(String command, boolean noAutoHover) {
        // Copy the own style.
        Style updated = this.style.createDeepCopy();

        // Create a click event.
        updated.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));

        if(!noAutoHover) {
            // Create a fitting hover event.
            updated.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new ChatMsgPart(DEFAULT_CMD_HOVER_COLOR, DEFAULT_CMD_HOVER_MSG + command).assemble()));
        }

        // Create the new object.
        return new ChatMsgPart(this.text, updated, true);
    }

    /**
     * Suggest a command. i.e. copy it into the chat window. There is no auto hover message.
     * @param command The command to suggest.
     * @return A new object which suggests the command.
     */
    public ChatMsgPart cmdSuggest(String command) {
        // Copy the own style.
        Style updated = this.style.createDeepCopy();

        // Create a click event.
        updated.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));

        // Create the new object.
        return new ChatMsgPart(this.text, updated, true);
    }

    /**
     * Make a message be shown when this message is hovered over in chat.
     * @param hoverText The message to be shown.
     * @return A new object which has the hover event.
     */
    public ChatMsgPart hover(ChatMsgPart hoverText) {
        // Copy the own style.
        Style updated = this.style.createDeepCopy();

        // Create a hover event.
        updated.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText.assemble()));

        // Create the new object.
        return new ChatMsgPart(this.text, updated, true);
    }

    /**
     * Make a message be shown when this message is hovered over in chat.
     * @param hoverText The message to be shown.
     * @return A new object which has the hover event.
     */
    public ChatMsgPart hover(ChatMsg hoverText) {
        // Copy of the method above but since the two parameters don't share an interface, this is necessary.
        // Copy the own style.
        Style updated = this.style.createDeepCopy();

        // Create a hover event. Uses assembleNoCache because ChatMsg must enforce its immutability.
        updated.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText.assembleNoCache()));

        // Create the new object.
        return new ChatMsgPart(this.text, updated, true);
    }



    /**
     *  Create a {@link ITextComponent} with this message. Potentially using the formatting.
     * @param args Arguments which will be passed to String.format().
     * @return Returns a {@link ITextComponent} which contains the message with formatting.
     */
    public ITextComponent assemble(Object... args) {

        // Format the text if necessary.
        String text;
        if(args == null || args.length == 0) {
            text = this.text;
        } else {
            text = String.format(this.text, args);
        }

        // Create a StringComponent.
        ITextComponent result = new TextComponentString(text);

        // The Style contains click and hover events if present.
        // Note that the copy is necessary to ensure the immutability of this object.
        result.setStyle(this.style.createDeepCopy());

        return result;
    }

    //
    // Utility methods.
    //

    /**
     * Calls the correct method to add a formatting code to a style. Modifies the style!
     * @param style The style to modify.
     * @param formatting The formatting to add. May be color or fancy formatting.
     */
    protected static Style addFormattingToStyle(Style style, TextFormatting formatting) {
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
            case RESET:
                // Messages created with this system don't have transitive formatting. Therefore no reset necessary.
                throw new RuntimeException("Invalid chat formatting created. Don't use reset!");
            default:
                style.setColor(formatting);
                break;
        }

        return  style;
    }

    //
    // Alternative constructors.
    //

    /**
     * Create an unformatted chat message.
     * @param text The message.
     */
    public ChatMsgPart(String text) {
        // Use no formatting.
        this(text, new Style(), true);
    }

    /**
     * Create a formatted message with a single {@link TextFormatting} modifier. Don't use reset.
     * @param format1 A formatting modifier applied to the message.
     * @param text The message.
     */
    public ChatMsgPart(TextFormatting format1, String text) {
        // Create the style with given format.
        this(text, addFormattingToStyle(new Style(), format1), true);
    }

    /**
     * Create a formatted message with a two {@link TextFormatting} modifier. Don't use reset.
     * If you need more modifiers, you can chain addFormatting() calls. (Make sure it won't be too busy for the eye!)
     * @param format1 A formatting modifier applied to the message.
     * @param format2 Another formatting modifier applied to the message.
     * @param text The message.
     */
    public ChatMsgPart(TextFormatting format1, TextFormatting format2, String text) {
        // Create the style with given formats.
        this(text, addFormattingToStyle(addFormattingToStyle(new Style(), format1), format2), true);
    }

    /**
     * Create a formatted message with a given style.
     *
     * NOTE: With this method you can undermine the immutability of the class because you could add a mutable
     * hover event. Use with care!
     *
     * @param style The style for the message. May contain click and hover events. Will be copied.
     * @param text The message.
     */
    public ChatMsgPart(Style style, String text) {
        // Style comes from outside. Needs to be copied to ensure immutability.
        this(text, style, false);
    }

    //
    // Alternative link methods.
    //

    /**
     * Make the message link to a url. Will create a hover event to indicate that it is a link.
     * MAKE SURE IT STARTS WITH HTTP:// or HTTPS://!
     * @param url A click on the message will open this url.
     * @return A new object which links to the url.
     */
    public ChatMsgPart link(String url) {
        return this.link(url, false);
    }

    /**
     * Make the message link to a url. Will create a hover event to indicate that it is a link.
     * @param url A click on the message will open this url.
     * @return A new object which links to the url.
     */
    public ChatMsgPart link(URL url) {
        return this.link(url, false);
    }

    /**
     * Make the message link to a url.
     * @param url A click on the message will open this url.
     * @param noAutoHover Disable the automatic hover message which will indicate that is a link. Use to create a custom one.
     * @return A new object which links to the url.
     */
    public ChatMsgPart link(URL url, boolean noAutoHover) {
        return this.link(url.toString(), noAutoHover);
    }

    //
    // Alternative file method.
    //

    /**
     * Make the message open a file. Will create a hover event to indicate the action.
     * @param file A click on the message will open this file.
     * @return A new object which opens the file.
     */
    public ChatMsgPart file(String file) {
        return this.file(file, false);
    }

    //
    // Alternative cmdRun method.
    //

    /**
     * Make the message run a command when clicked. Will create a hover event to indicate the action.
     * @param command The command to run.
     * @return A new object which runs the command.
     */
    public ChatMsgPart cmdRun(String command) {
        return this.cmdRun(command, false);
    }

    //
    // Alternative hover methods.
    //

    /**
     * Make a message be shown when this message is hovered over in chat. Will be unformatted.
     * @param text The message.
     * @return A new object which has the hover event.
     */
    public ChatMsgPart hover(String text) {
        // Create a new ChatMsgPart using the parameters.
        return this.hover(new ChatMsgPart(text));
    }

    /**
     * Make a message be shown when this message is hovered over in chat. Will use the given formatting modifier.
     * @param text The message.
     * @param format1 A formatting modifier applied to the message.
     * @return A new object which has the hover event.
     */
    public ChatMsgPart hover(TextFormatting format1, String text) {
        // Create a new ChatMsgPart using the parameters.
        return this.hover(new ChatMsgPart(format1, text));
    }

    /**
     * Make a message be shown when this message is hovered over in chat. Will use the given formatting modifiers.
     * @param text The message.
     * @param format1 A formatting modifier applied to the message.
     * @param format2 Another formatting modifier applied to the message.
     * @return A new object which has the hover event.
     */
    public ChatMsgPart hover(TextFormatting format1, TextFormatting format2, String text) {
        // Create a new ChatMsgPart using the parameters.
        return this.hover(new ChatMsgPart(format1, text));
    }
}
