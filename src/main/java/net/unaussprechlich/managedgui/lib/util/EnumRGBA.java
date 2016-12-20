/*
 * ***************************************************************************
 *
 *         Copyright © 2016 unaussprechlich - ALL RIGHTS RESERVED
 *
 * ***************************************************************************
 */

package net.unaussprechlich.managedgui.lib.util;

public enum EnumRGBA {
    TRANSPARENT   (new ColorRGBA(0, 0  , 0  , 0)),
    RED   (new ColorRGBA(255, 0  , 0  , 255)),
    GREY_T(new ColorRGBA(0  , 0  , 0  , 255 )),
    GREEN (new ColorRGBA(0  , 255, 0  , 255)),
    BLUE  (new ColorRGBA(0  , 0  , 255, 255 ));

    private final ColorRGBA RGBA;

    EnumRGBA(ColorRGBA RGBA){
        this.RGBA = RGBA;
    }

    public ColorRGBA get() {
        return RGBA;
    }

    public float getALPHAf() {return RGBA.getALPHAf();}
    public float getBLUEf()  {return RGBA.getBLUEf();}
    public float getGREENf() {return RGBA.getGREENf();}
    public float getREDf()   {return RGBA.getREDf();}

    public int getALPHA()  {return RGBA.getALPHA();}
    public int getBLUE()   {return RGBA.getBLUE();}
    public int getGREEN()  {return RGBA.getGREEN();}
    public int getRED()    {return RGBA.getRED();}
}
