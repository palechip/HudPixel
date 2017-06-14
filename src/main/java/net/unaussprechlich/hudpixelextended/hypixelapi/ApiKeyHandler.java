/*
 * HudPixelReloaded - License
 * <p>
 * The repository contains parts of Minecraft Forge and its dependencies. These parts have their licenses
 * under forge-docs/. These parts can be downloaded at files.minecraftforge.net.This project contains a
 * unofficial copy of pictures from the official Hypixel website. All copyright is held by the creator!
 * Parts of the code are based upon the Hypixel Public API. These parts are all in src/main/java/net/hypixel/api and
 * subdirectories and have a special copyright header. Unfortunately they are missing a license but they are obviously
 * intended for usage in this kind of application. By default, all rights are reserved.
 * The original version of the HudPixel Mod is made by palechip and published under the MIT license.
 * The majority of code left from palechip's creations is the component implementation.The ported version to
 * Minecraft 1.8.9 and up HudPixel Reloaded is made by PixelModders/Eladkay and also published under the MIT license
 * (to be changed to the new license as detailed below in the next minor update).
 * <p>
 * For the rest of the code and for the build the following license applies:
 * <p>
 * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
 * #  HudPixel by PixelModders, Eladkay & unaussprechlich is licensed under a Creative Commons         #
 * #  Attribution-NonCommercial-ShareAlike 4.0 International License with the following restrictions.  #
 * #  Based on a work at HudPixelExtended & HudPixel.                                                  #
 * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
 * <p>
 * Restrictions:
 * <p>
 * The authors are allowed to change the license at their desire. This license is void for members of PixelModders and
 * to unaussprechlich, except for clause 3. The licensor cannot revoke these freedoms in most cases, as long as you follow
 * the following license terms and the license terms given by the listed above Creative Commons License, however in extreme
 * cases the authors reserve the right to revoke all rights for usage of the codebase.
 * <p>
 * 1. PixelModders, Eladkay & unaussprechlich are the authors of this licensed material. GitHub contributors are NOT
 * considered authors, neither are members of the HudHelper program. GitHub contributers still hold the rights for their
 * code, but only when it is used separately from HudPixel and any license header must indicate that.
 * 2. You shall not claim ownership over this project and repost it in any case, without written permission from at least
 * two of the authors.
 * 3. You shall not make money with the provided material. This project is 100% non commercial and will always stay that
 * way. This clause is the only one remaining, should the rest of the license be revoked. The only exception to this
 * clause is completely cosmetic features. Only the authors may sell cosmetic features for the mod.
 * 4. Every single contibutor owns copyright over his contributed code when separated from HudPixel. When it's part of
 * HudPixel, it is only governed by this license, and any copyright header must indicate that. After the contributed
 * code is merged to the release branch you cannot revoke the given freedoms by this license.
 * 5. If your own project contains a part of the licensed material you have to give the authors full access to all project
 * related files.
 * 6. You shall not act against the will of the authors regarding anything related to the mod or its codebase. The authors
 * reserve the right to take down any infringing project.
 */

package net.unaussprechlich.hudpixelextended.hypixelapi;


import eladkay.hudpixel.HudPixelMod;
import eladkay.hudpixel.util.ChatMessageComposer;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.unaussprechlich.hudpixelextended.HudPixelExtendedEventHandler;
import net.unaussprechlich.hudpixelextended.hypixelapi.callbacks.ApiKeyLoadedCallback;
import net.unaussprechlich.hudpixelextended.util.IEventHandler;
import net.unaussprechlich.hudpixelextended.util.LoggerHelper;

import java.io.*;

@SideOnly(Side.CLIENT)
public class ApiKeyHandler implements IEventHandler {

    private static String API_KEY_STORAGE_PATH;
    private static String API_KEY_STORAGE_FILE;
    private static String API_KEY_REQUEST_MESSAGE_1 = "No API key found. This key is necessary for some cool features.";
    private static String API_KEY_REQUEST_MESSAGE_2_PART1 = "Simply do ";
    private static String API_KEY_REQUEST_MESSAGE_2_PART2 = " for creating a new one.";
    private static String API_KEY_REQUEST_MESSAGE_3 = "You can also add your key manually to config\\hypixel_api_key.txt.";
    private static String API_KEY_REQUEST_MESSAGE_4 = "If you don't want to use the API features, you can disable \"useAPI\" in the config";
    private static String EMPTY_FILE_CONTENT = "Replace this with the api key or do /api on Hypixel Network. This File gets reset when a key doesn't work.";
    private static String API_KEY_PATTERN = "[a-f0-9]{8}[-]([a-f0-9]{4}[-]){3}[a-f0-9]{12}";

    static {
        try {
            // in the config folder
            API_KEY_STORAGE_PATH = FMLClientHandler.instance().getClient().mcDataDir.getCanonicalPath() + File.separatorChar + "config" + File.separatorChar;
            // a file called hypixel_api_key.txt
            API_KEY_STORAGE_FILE = API_KEY_STORAGE_PATH + "hypixel_api_key.txt";
        } catch (IOException e) {
            HudPixelMod.Companion.instance().logError("Critical error when finding the api key file: ");
            e.printStackTrace();
        }
    }

    // this gets set to true when the loading fails but is finished
    private static boolean loadingFailed = false;
    private static String apiKey;
    private static ApiKeyLoadedCallback callback;

    private static ApiKeyHandler INSTANCE;

    public static ApiKeyHandler getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new ApiKeyHandler();
        return INSTANCE;
    }

    private ApiKeyHandler() {
        HudPixelExtendedEventHandler.registerIEvent(this);
    }

    public static String getApiKey() {
        return apiKey;
    }

    public void loadKey(ApiKeyLoadedCallback callback) {
        ApiKeyHandler.callback = callback;
        if(apiKey != null && !loadingFailed)
            callback.ApiKeyLoaded(false, apiKey);
        else
            new Thread(ApiKeyHandler.getINSTANCE()::loadAPIKey).start();
    }

    public static boolean isLoadingFailed() {
        return loadingFailed;
    }

    @Override
    public void onChatReceived(ClientChatReceivedEvent e) throws Throwable {
        String message = e.getMessage().getUnformattedText();
        if (!message.startsWith("Your new API key is ")) return;

        // extract the key
        apiKey = message.substring(message.indexOf("is ") + 3);

        // let the callback know
        callback.ApiKeyLoaded(false, apiKey);

        // and save it
        new Thread(ApiKeyHandler.getINSTANCE()::saveAPIKey).start();

        // tell the user
        new ChatMessageComposer("API key successfully detected and saved. The API is ready for usage.", TextFormatting.GREEN).send();

    }

    /**
     * Asks the user to do /api
     */
    public static void requestApiKey() {
        new ChatMessageComposer(API_KEY_REQUEST_MESSAGE_1).send();
        new ChatMessageComposer(API_KEY_REQUEST_MESSAGE_2_PART1).appendMessage(new ChatMessageComposer("/api new", TextFormatting.RED).makeClickable(Action.RUN_COMMAND, "/api new", new ChatMessageComposer("Runs ", TextFormatting.GRAY).appendMessage(new ChatMessageComposer("/api new", TextFormatting.RED)))).appendMessage(new ChatMessageComposer(API_KEY_REQUEST_MESSAGE_2_PART2)).send();
        new ChatMessageComposer(API_KEY_REQUEST_MESSAGE_3).send();
        new ChatMessageComposer(API_KEY_REQUEST_MESSAGE_4).send();
    }

    /**
     * Gets called from a separate thread for loading the api key
     */
    private void loadAPIKey() {
        try {
            LoggerHelper.logInfo("[API][Key] loading key!");
            // make sure the path exists
            File path = new File(API_KEY_STORAGE_PATH);
            File file = new File(API_KEY_STORAGE_FILE);
            if (!path.exists()) {
                path.mkdirs();
            }

            // isHypixelNetwork if the file exists
            if (!file.exists()) {
                // there is no file so there can't be an api key
                // create it
                file.createNewFile();
                this.resetApiFile(file);
                loadingFailed = true;
                callback.ApiKeyLoaded(true, null);
                requestApiKey();
                return;
            }
            // read the key
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String key = reader.readLine();
            reader.close();
            // make sure the content can be a valid key
            if (key == null || key.equals(EMPTY_FILE_CONTENT) || !this.isCorrectKeyFormat(key.replace(" ", ""))) {
                this.resetApiFile(file);
                loadingFailed = true;
                callback.ApiKeyLoaded(true, null);
                requestApiKey();
                return;
            }
            apiKey = key.replace(" ", "");
            loadingFailed = false;
            callback.ApiKeyLoaded(false, apiKey);
        } catch (Exception e) {
            HudPixelMod.Companion.instance().logError("Critical error when reading the api key file: ");
            e.printStackTrace();
        }
    }

    /**
     * Gets called from a separate thread for loading the api key
     */
    private void saveAPIKey() {
        try {
            // we don't isHypixelNetwork whether the file exists since we already do it on startup
            // there is no way the user deletes the file after startup unintentionally
            PrintWriter writer = new PrintWriter(new File(API_KEY_STORAGE_FILE));
            writer.write(apiKey);
            writer.close();
        } catch (Exception e) {
            HudPixelMod.Companion.instance().logError("Critical error when storing the api key file: ");
            e.printStackTrace();
        }
    }

    /**
     * Empties the api key file and adds a message how to use it.
     */
    private void resetApiFile(File file) throws FileNotFoundException {
        // empty the file
        PrintWriter writer = new PrintWriter(file);
        // fill the file with the empty content
        writer.write(EMPTY_FILE_CONTENT);
        writer.flush();
        writer.close();
    }

    /**
     * Verifies that the key has the correct pattern.
     */
    private boolean isCorrectKeyFormat(String key) {
        return key.matches(API_KEY_PATTERN);
    }
}
