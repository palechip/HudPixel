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
package com.palechip.hudpixelmod.uptodate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;

import net.minecraftforge.common.config.Configuration;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.games.GameManager;

public class UpToDateThread extends Thread{
    private final File configurationDirectory;
    private static final String RESOURCE_LOCATION = "HudPixelUpToDateFiles/";
    private static final String CACHEFOLDER = "HudPixel-Up-To-Date";
    
    private static final String VERSION_MEMBER = "version";

    private Gson gson = new Gson();

    // a list of all files to make the code a little more compact
    private static ArrayList<String> files;
    
    private HashMap<String, Integer> versions = new HashMap<String, Integer>();

    // file names
    private static final String GAMES_FILE = "games.json";

    static {
        files = new ArrayList<String>();
        files.add(GAMES_FILE);
    }



    /**
     * Loads configuration files and keeps them up to date.
     */
    public UpToDateThread(File configDir) {
        // this class works fully asynchronously.
        super();
        this.setName("HudPixel Up To Date");
        // save the configDir
        this.configurationDirectory = new File(configDir.getAbsolutePath() + File.separator + CACHEFOLDER);
        try{
            // let's start!
            this.start();
        } catch(Exception e) {
            HudPixelMod.instance().logError("The HudPixel Up to Date Thread died!, this may cause severe problems.");
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        HudPixelMod logger = HudPixelMod.instance();
        
        // check if local files exist
        for(String file : files) {
            // if the file doesn't exist
            if(!(new File(configurationDirectory + File.separator + file).exists())) {
                // make sure the directory exists
                this.configurationDirectory.mkdirs();
                // extract them from the jar
                this.extractFileFromJar(file);
            }
        }

        // load and read local files
        this.readGamesFile(logger);

        // download version information
        //TODO
        // download changed files, replace and load them
        //TODO
    }

    /**
     * Extracts a file from the jar and copys it into the cache folder
     */
    private void extractFileFromJar(String file) {
        HudPixelMod logger = HudPixelMod.instance();
        logger.logInfo("Extracting file \"" + file + "\" from the jar!");
        OutputStream output = null;
        InputStream input = null;
        try {
            // the destination
            output = new FileOutputStream(this.configurationDirectory + File.separator + file);
            // the jar content from the mod jar
            input = this.getClass().getClassLoader().getResourceAsStream(RESOURCE_LOCATION + file);
            // let's hope MC doesn't get rid of apache IOUtils...
            IOUtils.copy(input, output);
        } catch (Exception e) {
            HudPixelMod.instance().logError("Failed to extract the file \"" + file + "\" from the jar.");
        } finally {
            try {
                // make sure the streams get closed
                output.close();
                input.close();
            } catch (Exception e) {
                // seems like they weren't open
            }
        }
    }

    /**
     * Loads the file and parses it as Json.
     */
    private JsonObject loadFile(String file) {
        return this.loadFile(file, true);
    }

    /**
     * Loads the file and parses it as Json.
     * @param firstAttempt used for error detection. The first attempt will automatically retry after failing. (After re-extracting the file from jar)
     */
    private JsonObject loadFile(String file, boolean firstAttempt) {
        HudPixelMod logger = HudPixelMod.instance();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.configurationDirectory + File.separator + file));
            return gson.fromJson(reader, JsonObject.class);
        } catch(Exception e) {
            if(firstAttempt) {
                logger.logError("Failed to read the file \"" + file.toString() + "\"! Extracting a new copy from jar.");
                e.printStackTrace();
                this.extractFileFromJar(file);
                return this.loadFile(file, false);
            } else {
                logger.logError("Failed the second time to read the file \"" + file.toString() + "\"! This is really bad. Things might not work correctly");
                return new JsonObject();
            }
        }
    }
    
    private void readGamesFile(HudPixelMod logger) {
        // GAMES_FILE
        try {
            // load it
            JsonObject games = this.loadFile(GAMES_FILE);
            
            // get the version and save it
            if(games.has(VERSION_MEMBER)) {
                versions.put(GAMES_FILE, games.get(VERSION_MEMBER).getAsInt());
            } else {
                versions.put(GAMES_FILE, 0);
                logger.logWarn("The " + GAMES_FILE + "is lacking a version flag. Assuming version 0.");
            }
            // pass it's other parts to GameManager
            new GameManager(games.get("games").getAsJsonArray(), games.get("components").getAsJsonArray());
        } catch (Exception e) {
            logger.logError("The " + GAMES_FILE + "is corrupt. May things will not work correctly unless it is fixed!");
        }
    }
}
