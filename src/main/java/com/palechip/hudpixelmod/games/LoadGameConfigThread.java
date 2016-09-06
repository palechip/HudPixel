package com.palechip.hudpixelmod.games;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.palechip.hudpixelmod.HudPixelMod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/******************************************************************************
 * HudPixelExtended by unaussprechlich(github.com/unaussprechlich/HudPixelExtended),
 * an unofficial Minecraft Mod for the Hypixel Network.
 * <p>
 * Original version by palechip (github.com/palechip/HudPixel)
 * "Reloaded" version by PixelModders  > Eladkay (github.com/PixelModders/HudPixel)
 * <p>
 * Copyright (c) 2016 unaussprechlich and contributors
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/


public class LoadGameConfigThread extends Thread {

    private final File configurationDirectory;
    private static final String RESOURCE_LOCATION = "HudPixelUpToDateFiles/";
    private static final String CACHEFOLDER = "HudPixelUpToDate";
    private static final String REPOSITORY_URL = "https://raw.githubusercontent.com/palechip/HudPixelUpToDate/master/";

    // a switch used to make developing easier. In distributions, this is allways false.
    private static final boolean ALWAYS_EXTRACT = false;

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
    public LoadGameConfigThread(File configDir) {
        // this class works fully asynchronously.
        super();
        this.setName("HudPixel Up To Date");
        // save the configDir
        this.configurationDirectory = new File(configDir.getAbsolutePath() + File.separator + CACHEFOLDER);
        try {
            // let's start!
            this.start();
        } catch (Exception e) {
            HudPixelMod.instance().logError("The HudPixel Up to Date Thread died!, this may cause severe problems.");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        HudPixelMod logger = HudPixelMod.instance();

        // isHypixelNetwork if local files exist
        for (String file : files) {
            // if the file doesn't exist or the mod is in debugging mode (debugging mode always extracts the latest versions to reflect recent changes)
            if (!(new File(configurationDirectory + File.separator + file).exists()) || ALWAYS_EXTRACT) {
                // make sure the directory exists
                this.configurationDirectory.mkdirs();
                // extract them from the jar
                this.extractFileFromJar(file);
            }
        }

        // load and read local files
        this.readGamesFile(logger);

        // now the config can be read because it needs GameManager and ComponentManager to be working
        HudPixelMod.instance().CONFIG.syncConfig();
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
     *
     * @param firstAttempt used for error detection. The first attempt will automatically retry after failing. (After reextracting the file from jar)
     */
    private JsonObject loadFile(String file, boolean firstAttempt) {
        HudPixelMod logger = HudPixelMod.instance();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.configurationDirectory + File.separator + file));
            return gson.fromJson(reader, JsonObject.class);
        } catch (Exception e) {
            if (firstAttempt) {
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


    /**
     * Loads a games.json file.
     */
    private void readGamesFile(HudPixelMod logger) {
        // GAMES_FILE
        try {
            // load it
            JsonObject games = this.loadFile(GAMES_FILE);

            // get the version and save it
            if (games.has(VERSION_MEMBER)) {
                versions.put(GAMES_FILE, games.get(VERSION_MEMBER).getAsInt());
            } else {
                versions.put(GAMES_FILE, 0);
                logger.logWarn("The " + GAMES_FILE + "is lacking a version flag. Assuming version 0.");
            }
            // pass it's other parts to GameManager
            new GameManager(games.get("games").getAsJsonArray()/*, games.get("components").getAsJsonArray()*/);
        } catch (Exception e) {
            logger.logError("The " + GAMES_FILE + "is corrupt. May things will not work correctly unless it is fixed!");
        }
    }

    /**
     * Download a file form the online repository
     *
     * @param fileName the file name as in files
     * @param version  the version of the file to download
     * @return True if the download was successful
     */
    private boolean downloadFile(String fileName, int version, HudPixelMod logger) {
        try {
            // log the download
            logger.logInfo("Updating \"" + fileName + "\" to version " + version);
            URL downloadURL = new URL(REPOSITORY_URL + fileName.substring(0, fileName.indexOf('.')) + "/v" + version + ".json");
            File destinationFile = new File(this.configurationDirectory + File.separator + fileName);
            // use Apache Commons to download the file
            FileUtils.copyURLToFile(downloadURL, destinationFile, 2000, 4000);
        } catch (IOException e) {
            // log the error
            logger.logError("Failed to download \"" + fileName + "\"! Couldn't get the latest version. This may cause bad behaviour.");
            e.printStackTrace();
            // faiture
            return false;
        }
        // success
        return true;
    }

    /**
     * Downloads to a string. Used for files which aren't saved.
     *
     * @param url the URL from where to download from
     * @return The downloaded string or an empty json object in case of failture.
     */
    private String downloadFileToString(String url, HudPixelMod logger) {
        try {
            return IOUtils.toString(new URL(url));
        } catch (Exception e) {
            logger.logWarn("Faild to download \"" + url + "\"! This is sad but not gamebreaking.");
        }
        return "{}";
    }
}