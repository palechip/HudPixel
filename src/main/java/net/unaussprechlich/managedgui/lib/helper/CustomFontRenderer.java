/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import net.unaussprechlich.managedgui.lib.util.ColorRGBA;
import net.unaussprechlich.managedgui.lib.util.RGBA;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import java.awt.*;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;

/**
 * CustomFontRenderer Created by Alexander on 05.03.2017.
 * Description:
 **/
public class CustomFontRenderer extends FontRenderer {

    private static final String random = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000";


    private final UnicodeFont font;
    private final UnicodeFont fontItalic;
    private final UnicodeFont fontBold;
    private final UnicodeFont fontBoldItalic;

    private boolean randomStyle = false;
    private boolean boldStyle = false;
    private boolean strikethroughStyle = false;
    private boolean underlineStyle = false;
    private boolean italicStyle = false;

    private static final int FONT_HEIGHT_FT = 15;

    private static CustomFontRenderer INSTANCE;

    public CustomFontRenderer(Font awtFont) {
        super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().getTextureManager(), false);


        font = new UnicodeFont(awtFont);
        fontBold = new UnicodeFont(awtFont, FONT_HEIGHT_FT , true, false);
        fontItalic = new UnicodeFont(awtFont, FONT_HEIGHT_FT, false, true);
        fontBoldItalic = new UnicodeFont(awtFont, FONT_HEIGHT_FT, false, true);

        font.addAsciiGlyphs();
        fontBold.addAsciiGlyphs();
        fontItalic.addAsciiGlyphs();
        fontBoldItalic.addAsciiGlyphs();

        font.getEffects().add(new ColorEffect(Color.WHITE));
        fontBold.getEffects().add(new ColorEffect(Color.WHITE));
        fontItalic.getEffects().add(new ColorEffect(Color.WHITE));
        fontBoldItalic.getEffects().add(new ColorEffect(Color.WHITE));
        try {
            font.loadGlyphs();
            fontBold.loadGlyphs();
            fontItalic.loadGlyphs();
            fontBoldItalic.loadGlyphs();
        } catch(SlickException exception) {
            throw new RuntimeException(exception);
        }

        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
        FONT_HEIGHT = font.getHeight(alphabet) / 2;
    }

    public static CustomFontRenderer getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new CustomFontRenderer(new Font("Trebuchet MS", Font.PLAIN, FONT_HEIGHT_FT));
        return INSTANCE;
    }



    public void drawFormatted(String s, int x, int y){
        ColorRGBA     color = RGBA.WHITE_MC.get();
        int           posX  = 0;
        StringBuilder sB    = new StringBuilder();

        randomStyle = false;
        boldStyle = false;
        italicStyle = false;
        strikethroughStyle = false;
        underlineStyle = false;

        for(int i = 0; i < s.length(); i++){
            char c0 = s.charAt(i);
            if (c0 == '§' && i + 1 < s.length()) {
                char c1 = s.toLowerCase(Locale.ENGLISH).charAt(i + 1);
                int i1 = "0123456789abcdefklmnor".indexOf(c1);
                //DRAW IT!
                if(i1 != -1){
                    i++;
                    renderString(sB.toString(), x + posX, y, color);
                    posX += getStringWidth(sB.toString());
                    sB = new StringBuilder();

                    if (i1 < 16) color = ColorRGBA.Companion.getColorFromFormatting(c1);
                    else if (i1 == 16) this.randomStyle = true;
                    else if (i1 == 17) this.boldStyle = true;
                    else if (i1 == 18) this.strikethroughStyle = true;
                    else if (i1 == 19) this.underlineStyle = true;
                    else if (i1 == 20) this.italicStyle = true;
                    else if (i1 == 21) {
                        this.randomStyle = false;
                        this.boldStyle = false;
                        this.strikethroughStyle = false;
                        this.underlineStyle = false;
                        this.italicStyle = false;
                        color = RGBA.WHITE_MC.get();
                    }

                } else sB.append(c0);


            } else {
                int j = random.indexOf(c0);
                if (this.randomStyle && j != -1) {
                    int k = getCharWidth(c0);
                    char cR;
                    while (true) {
                        j = this.fontRandom.nextInt(random.length());
                        cR = random.charAt(j);
                        if (k == getCharWidth(cR)) break;
                    }
                    c0 = cR;
                }
                sB.append(c0);
            }
            if(i == s.length() - 1) renderString(sB.toString(), x + posX, y, color);
        }
    }


    public void renderString(String string, int x, int y, ColorRGBA color) {
        if(string == null || string.equalsIgnoreCase("")) return;

        glPushMatrix();
        glScaled(0.5, 0.5, 0.5);

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 0, 1);

        x *= 2;
        y *= 2;

        if(italicStyle && boldStyle)
            fontBoldItalic.drawString(x, y, string, new org.newdawn.slick.Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()));
        else if(italicStyle)
            fontItalic.drawString(x, y, string, new org.newdawn.slick.Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()));
        else if(boldStyle)
            fontBold.drawString(x, y, string, new org.newdawn.slick.Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()));
        else
            font.drawString(x, y, string, new org.newdawn.slick.Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()));

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();

        glPopMatrix();
    }

    @Override
    public java.util.List<String> listFormattedStringToWidth(String str, int wrapWidth) {
        return Arrays.asList(wrapFormattedStringToWidth(str, wrapWidth).split("\n"));
    }

    /**
     * Inserts newline and formatting into a string to wrap it within the specified width.
     */
    private String wrapFormattedStringToWidth(String str, int wrapWidth) {
        int i = this.sizeStringToWidth(str, wrapWidth);
        if (str.length() <= i) return str;
        else {
            String s = str.substring(0, i);
            char c0 = str.charAt(i);
            boolean flag = c0 == 32 || c0 == 10;
            String s1 = getFormatFromString(s) + str.substring(i + (flag ? 1 : 0));
            return s + "\n" + wrapFormattedStringToWidth(s1, wrapWidth);
        }
    }

    /**
     * Determines how many characters from the string will fit into the specified width.
     */
    private int sizeStringToWidth(String str, int wrapWidth) {
        int i = str.length();
        int j = 0;
        int k = 0;
        int l = -1;

        for (boolean flag = false; k < i; ++k) {
            char c0 = str.charAt(k);

            switch (c0) {
                case '\n':
                    --k;
                    break;
                case ' ':
                    l = k;
                default:
                    j += getCharWidth(c0);
                    if (flag) ++j;
                    break;
                case '\u00a7':
                    if (k < i - 1) {
                        ++k;
                        char c1 = str.charAt(k);
                        if (c1 != 108 && c1 != 76)
                            if (c1 == 114 || c1 == 82 || isFormatColor(c1))
                                flag = false;
                            else flag = true;
                    }
            }

            if (c0 == 10) {
                ++k;
                l = k;
                break;
            }

            if (j > wrapWidth) {
                break;
            }
        }
        return k != i && l != -1 && l < k ? l : k;
    }

    /**
     * Checks if the char code is a hexadecimal character, used to set colour.
     */
    private static boolean isFormatColor(char colorChar) {
        return colorChar >= 48 && colorChar <= 57 || colorChar >= 97 && colorChar <= 102 || colorChar >= 65 && colorChar <= 70;
    }

    /**
     * Trims a string to a specified width
     */
    @Override
    public String trimStringToWidth(String text, int width) {

        StringBuilder stringbuilder = new StringBuilder();
        int i = 0;
        int j = 0;
        int k = 1;
        boolean flag = false;
        boolean flag1 = false;

        for (int l = j; l >= 0 && l < text.length() && i < width; l += k) {

            char c0 = text.charAt(l);
            int i1 = font.getWidth(c0 +  "");

            if (flag) {
                flag = false;
                if (c0 != 108 && c0 != 76)
                    flag1 = !(c0 == 114 || c0 == 82);
            }
            else if (i1 < 0) flag = true;
            else {
                i += i1;
                if (flag1) ++i;
            }
            if (i > width) break;

            stringbuilder.append(c0);
        }

        return stringbuilder.toString();
    }


    @Override
    public int getCharWidth(char c) {
        return getStringWidth(Character.toString(c));
    }

    @Override
    public int getStringWidth(String string) {
        if(Objects.equals(string, "")) return 0;
        return font.getWidth(string) / 2;
    }
}
