package com.palechip.hudpixelmod.games;

import java.util.ArrayList;

/**
 * Holds necessary properties for a game to be created and detected. Likes to get filled from JSON.
 * @author palechip
 *
 */
public class GameConfiguration {

    public static final GameConfiguration NULL_GAME = new GameConfiguration();

    // different names
    private String officialName;
    private String shortName;
    private String databaseName;

    // ID's
    private int databaseID;
    private int modID;

    // detection properties
    private String chatTag;
    private String bossbarName;
    private String scoreboardName;
    private String scoreboardMap;

    private String startMessage;
    private String endMessage;

    // config values
    private String configCategory;
    private String configPrefix;

    // components
    private ArrayList<String> components;

    // getters for all fields
    public String getOfficialName() {
        return officialName;
    }

    public String getShortName() {
        return shortName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public int getDatabaseID() {
        return databaseID;
    }

    public int getModID() {
        return modID;
    }

    public String getChatTag() {
        return chatTag;
    }

    public String getBossbarName() {
        return bossbarName;
    }

    public String getScoreboardName() {
        return scoreboardName;
    }

    public String getScoreboardMap() {
        return scoreboardMap;
    }

    public String getStartMessage() {
        return startMessage;
    }

    public String getEndMessage() {
        return endMessage;
    }

    public String getConfigCategory() {
        return configCategory;
    }

    public String getConfigPrefix() {
        return configPrefix;
    }

    public ArrayList<String> getComponents() {
        return components;
    }

    private GameConfiguration() {
        this.officialName = "NULL GAME";
        this.shortName = "NULL";
        this.databaseName = "";
        this.databaseID = 0;
        this.modID = 0;
        this.chatTag = "";
        this.bossbarName = "";
        this.scoreboardName = "";
        this.startMessage = "";
        this.endMessage = "";
        this.configCategory = "";
        this.configPrefix = "";
        this.components = new ArrayList<String>();
    }

    @Override
    /**
     * Used to verify that the Json parsing created a correct GameConfiguration. 
     */
    public String toString() {
        return "GameConfiguration [officialName=" + officialName
                + ", shortName=" + shortName + ", databaseName=" + databaseName
                + ", databaseID=" + databaseID + ", modID=" + modID
                + ", chatTag=" + chatTag + ", bossbarName=" + bossbarName
                + ", scoreboardName=" + scoreboardName + ", scoreboardMap="
                + scoreboardMap + ", startMessage=" + startMessage
                + ", endMessage=" + endMessage + ", configCategory="
                + configCategory + ", configPrefix=" + configPrefix
                + ", components=" + components + "]";
    }
}
