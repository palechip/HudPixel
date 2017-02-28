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
}
