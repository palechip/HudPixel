package com.palechip.hudpixelmod.components;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.util.EnumChatFormatting;

public class MegaWallsKillCounter implements IComponent {
    private static final String KILL_DISPLAY = EnumChatFormatting.AQUA + "Kills: " + EnumChatFormatting.RED;
    private static final String FINAL_KILL_DISPLAY = EnumChatFormatting.BLUE + "Final Kills: " + EnumChatFormatting.RED;
    private static final String ASSISTS_DISPLAY = EnumChatFormatting.AQUA +  "" + EnumChatFormatting.ITALIC +"Assists: " + EnumChatFormatting.DARK_GRAY;
    private static final String FINAL_ASSISTS_DISPLAY = EnumChatFormatting.BLUE +  "" + EnumChatFormatting.ITALIC +"Final Assists: " + EnumChatFormatting.DARK_GRAY;
    public static enum KillType {Normal, Final, Assists, Final_Assists};
    
    private KillType trackedType;
    private int kills;

    public MegaWallsKillCounter(KillType type) {
        this.trackedType = type;
    }

    @Override
    public void setupNewGame() {
        this.kills = 0;
    }

    @Override
    public void onGameStart() {
    }

    @Override
    public void onGameEnd() {
    }

    @Override
    public void onTickUpdate() {
    }

    @Override
    public void onChatMessage(String textMessage, String formattedMessage) {
    	if(textMessage.startsWith("+") && textMessage.toLowerCase().contains("coins")) {
    		switch (this.trackedType) {
    		case Normal:
    			if(!textMessage.contains("ASSIST") && !textMessage.contains("FINAL KILL")) {
    				this.kills++;
    			}
    			break;
    		case Final:
    			if(!textMessage.contains("ASSIST") && textMessage.contains("FINAL KILL")) {
    				this.kills++;
    			}
    			break;
    		case Assists:
    			if(textMessage.contains("ASSIST") && !textMessage.contains("FINAL KILL")) {
    				this.kills++;
    			}
    			break;
    		case Final_Assists:
    			if(textMessage.contains("ASSIST") && textMessage.contains("FINAL KILL")) {
    				this.kills++;
    			}
    			break;
    		}
    	}
    }

    @Override
    public String getRenderingString() {
        switch (this.trackedType) {
        case Normal:
            return KILL_DISPLAY + this.kills;
        case Final:
            return FINAL_KILL_DISPLAY + this.kills;
        case Assists:
        	return ASSISTS_DISPLAY + this.kills;
        case Final_Assists:
        	return FINAL_ASSISTS_DISPLAY + this.kills;
        }
        return "";
    }

}
