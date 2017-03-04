/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */
package net.unaussprechlich.managedgui.lib.util;


public class ColorRGBA {

    private final int RED;
    private final int GREEN;
    private final int BLUE;
    private final int ALPHA;

    private final float REDf;
    private final float GREENf;
    private final float BLUEf;
    private final float ALPHAf;

    public ColorRGBA(int RED, int GREEN, int BLUE, int ALPHA) {
        this.RED   = RED;
        this.GREEN = GREEN;
        this.BLUE  = BLUE;
        this.ALPHA = ALPHA;

        this.REDf   = ((float)RED)   / 255;
        this.GREENf = ((float)GREEN) / 255;
        this.BLUEf  = ((float)BLUE)  / 255;
        this.ALPHAf = ((float)ALPHA) / 255;
    }

    public ColorRGBA(int RED, int GREEN, int BLUE) {
        int ALPHA = 255;
        this.RED   = RED;
        this.GREEN = GREEN;
        this.BLUE  = BLUE;
        this.ALPHA = ALPHA;

        this.REDf   = ((float)RED)   / 255;
        this.GREENf = ((float)GREEN) / 255;
        this.BLUEf  = ((float)BLUE)  / 255;
        this.ALPHAf = ((float)ALPHA) / 255;
    }

    public ColorRGBA(float RED, float GREEN, float BLUE, float ALPHA) {
        this.RED   = Math.round(RED   * 255f);
        this.GREEN = Math.round(GREEN * 255f);
        this.BLUE  = Math.round(BLUE  * 255f);
        this.ALPHA = Math.round(ALPHA * 255f);

        this.REDf   = RED;
        this.GREENf = GREEN;
        this.BLUEf  = BLUE;
        this.ALPHAf = ALPHA;
    }

    public ColorRGBA(float RED, float GREEN, float BLUE) {
        float ALPHA = 1f;
        this.RED   = Math.round(RED   * 255f);
        this.GREEN = Math.round(GREEN * 255f);
        this.BLUE  = Math.round(BLUE  * 255f);
        this.ALPHA = Math.round(ALPHA * 255f);

        this.REDf   = RED;
        this.GREENf = GREEN;
        this.BLUEf  = BLUE;
        this.ALPHAf = ALPHA;
    }

    public float getALPHAf() {return ALPHAf;}
    public float getBLUEf()  {return BLUEf;}
    public float getGREENf() {return GREENf;}
    public float getREDf()   {return REDf;}

    public int getALPHA()  {return ALPHA;}
    public int getBLUE()   {return BLUE;}
    public int getGREEN()  {return GREEN;}
    public int getRED()    {return RED;}

    public static int interpolateColor(int rgba1, int rgba2, float percent) {
        int r1 = rgba1 & 0xFF, g1 = rgba1 >> 8 & 0xFF, b1 = rgba1 >> 16 & 0xFF, a1 = rgba1 >> 24 & 0xFF;
        int r2 = rgba2 & 0xFF, g2 = rgba2 >> 8 & 0xFF, b2 = rgba2 >> 16 & 0xFF, a2 = rgba2 >> 24 & 0xFF;

        int r = (int) (r1 < r2 ? r1 + (r2 - r1) * percent : r2 + (r1 - r2) * percent);
        int g = (int) (g1 < g2 ? g1 + (g2 - g1) * percent : g2 + (g1 - g2) * percent);
        int b = (int) (b1 < b2 ? b1 + (b2 - b1) * percent : b2 + (b1 - b2) * percent);
        int a = (int) (a1 < a2 ? a1 + (a2 - a1) * percent : a2 + (a1 - a2) * percent);

        return r | g << 8 | b << 16 | a << 24;
    }

    public static int toRGBA(ColorRGBA c) {
        return c.getRED() | c.getGREEN() << 8 | c.getBLUE() << 16 | c.getALPHA() << 24;
    }

    public static ColorRGBA toColor(int rgba) {
        int r = rgba & 0xFF, g = rgba >> 8 & 0xFF, b = rgba >> 16 & 0xFF, a = rgba >> 24 & 0xFF;
        return new ColorRGBA(r, g, b, a);
    }

    public static ColorRGBA getColorFromFormatting(char c){
        ColorRGBA color = RGBA.WHITE_MC.get();
        switch (c){
            case '0': color = RGBA.BLACK_MC.get(); break;
            case '1': color = RGBA.BLUE_DARK_MC.get(); break;
            case '2': color = RGBA.GREEN_DARK_MC.get(); break;
            case '3': color = RGBA.AQUA_DARK_MC.get(); break;
            case '4': color = RGBA.RED_DARK_MC.get(); break;
            case '5': color = RGBA.PURPLE_DARK_MC.get(); break;
            case '6': color = RGBA.GOLD_MC.get(); break;
            case '7': color = RGBA.GRAY_MC.get(); break;
            case '8': color = RGBA.GRAY_DARK_MC.get(); break;
            case '9': color = RGBA.BLUE_MC.get(); break;
            case 'a': color = RGBA.GREEN_MC.get(); break;
            case 'b': color = RGBA.AQUA_MC.get(); break;
            case 'c': color = RGBA.RED_MC.get(); break;
            case 'd': color = RGBA.PURPLE_LIGHT_MC.get(); break;
            case 'e': color = RGBA.YELLOW_MC.get(); break;
            case 'f': color = RGBA.WHITE_MC.get(); break;
        }
        return color;
    }
}
