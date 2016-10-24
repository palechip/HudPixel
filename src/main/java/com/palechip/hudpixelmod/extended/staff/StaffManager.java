/***********************************************************************************************************************
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
 **********************************************************************************************************************/
package com.palechip.hudpixelmod.extended.staff;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.palechip.hudpixelmod.GameDetector;
import com.palechip.hudpixelmod.extended.HudPixelExtendedEventHandler;
import com.palechip.hudpixelmod.extended.fancychat.FancyChat;
import com.palechip.hudpixelmod.extended.util.IEventHandler;
import com.palechip.hudpixelmod.extended.util.LoggerHelper;
import com.palechip.hudpixelmod.extended.util.McColorHelper;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class StaffManager implements IEventHandler, McColorHelper {

    private static ArrayList<String> adminList = new ArrayList<String>();
    private static ArrayList<String> helperList = new ArrayList<String>();
    private static String LINK_TO_STAFFJSON = "http://hudpixel.unaussprechlich.net/HudPixel/files/staffjson.php";

    public StaffManager() {
        HudPixelExtendedEventHandler.registerIEvent(this);
        //loades the staff
        getHttpRequest();
    }

    /**
     * changes the tag above the player, when they join and are part of the hudpixel team
     * @param e
     */
    public static void onPlayerName(PlayerEvent.NameFormat e) {
        if (!GameDetector.isLobby()) return;
        if (adminList.contains(e.username)) {
            e.displayname = hudAdminTag() + e.displayname;
        } else if (helperList.contains(e.username)) {
            e.displayname = hudHelperTag() + e.displayname;
        }
    }

    private static String hudHelperTag() {
        return GOLD + "[Hud" + YELLOW + "Helper" + GOLD + "]" + YELLOW + " ";
    }

    private static String hudAdminTag() {
        return GOLD + "[Hud" + RED + "Admin" + GOLD + "]" + RED + " ";
    }

    /**
     * Does a http request to my server and get's the current staff members
     */
    private void getHttpRequest() {
        try {
            URL u = new URL(LINK_TO_STAFFJSON);
            HttpURLConnection con = (HttpURLConnection) u.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

            StringBuilder sBuilder = new StringBuilder();
            String buff = "";
            while ((buff = br.readLine()) != null) {
                sBuilder.append(buff);
            }
            String data = sBuilder.toString();
            JsonObject jsonObject = jsonParser(data);
            adminList = getArrayFromJsonEntry(getStringFromJson("admins", jsonObject));
            helperList = getArrayFromJsonEntry(getStringFromJson("helper", jsonObject));
        } catch (MalformedURLException e) {
            LoggerHelper.logError("[StaffLoader]: Something went wrong while loading the URL for the staff file");
            e.printStackTrace();
        } catch (IOException e) {
            LoggerHelper.logError("[StaffLoader]: Something went wrong while reading the update file!");
            e.printStackTrace();
        }
    }

    /**
     * Get's the data from the website and generates a JsonObjrect
     * @param data website data
     * @return the data as JsonObject
     */
    private JsonObject jsonParser(String data) {
        JsonParser jsonParser = new JsonParser();
        return jsonParser.parse(data).getAsJsonObject();
    }

    /**
     * get's all usernames i put into the category
     * @param s JsonEntry
     * @return usernames in a ArrayList
     */
    private ArrayList<String> getArrayFromJsonEntry(String s) {
        s = removeChars(s, "\"");
        s = removeChars(s, "[");
        return new ArrayList<String>(Arrays.asList(s.split(",")));
    }

    /**
     * Get's one json entry via key and returns everything inside as a string
     * @param key the key
     * @param jsonObject the JsonObject
     * @return entry
     */
    private String getStringFromJson(String key, JsonObject jsonObject) {
        try {
            if (jsonObject.get(key) != null) {
                return jsonObject.get(key).toString();
            } else {
                LoggerHelper.logWarn("[StaffLoader]: Key '" + key + "' not Found in Json!");
                return "";
            }
        } catch (Exception e) {
            LoggerHelper.logError("[UpdateNotifier]: Something went wrong while extracting Key '" + key + "' from Json!");
            e.printStackTrace();
            return D_RED + "Something went wrong while extracting Key '" + key + "' from Json!";
        }
    }

    /**
     * removes a char-type from a string
     * @param s the string
     * @param r this char will get replaced by noting
     * @return s without r
     */
    private String removeChars(String s, String r) {
        return s.replace(r, "");
    }


    /**
     * buts the admin/helper tag infront of a message a admin/helper has written
     * @param e chat event
     * @throws Throwable
     */
    @Override
    public void onChatReceived(ClientChatReceivedEvent e) throws Throwable {
        if (e.message.getUnformattedText().contains("http")) return;
        for (String s : adminList) {
            if (e.message.getUnformattedText().contains(s + ":") || e.message.getUnformattedText().startsWith(s + ":")) {
                e.message = new ChatComponentText(e.message.getFormattedText().replaceFirst(s, hudAdminTag() + s));
                FancyChat.getInstance().addMessage(e.message.getFormattedText());
                return;
            }
        }

        for (String s : helperList) {
            if (e.message.getUnformattedText().contains(s + ":") || e.message.getUnformattedText().startsWith(s + ":")) {
                e.message = new ChatComponentText(e.message.getFormattedText().replaceFirst(s, hudHelperTag() + s));
                FancyChat.getInstance().addMessage(e.message.getFormattedText());
                return;
            }
        }
    }

    @Override
    public void onClientTick() {
    }

    @Override
    public void onRender() {
    }

    @Override
    public void handleMouseInput(int i, int mX, int mY) {
    }

    @Override
    public void onMouseClick(int mX, int mY) {
    }
}
