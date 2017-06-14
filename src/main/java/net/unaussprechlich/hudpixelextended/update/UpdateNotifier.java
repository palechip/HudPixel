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

package net.unaussprechlich.hudpixelextended.update;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eladkay.hudpixel.util.ChatMessageComposer;
import eladkay.hudpixel.util.UpdateHandlerKt;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.unaussprechlich.hudpixelextended.util.McColorHelper;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import static eladkay.hudpixel.HudPixelMod.DEFAULT_VERSION;
import static java.lang.System.out;
import static net.minecraft.client.Minecraft.getMinecraft;
import static net.unaussprechlich.hudpixelextended.util.LoggerHelper.logError;
import static net.unaussprechlich.hudpixelextended.util.LoggerHelper.logWarn;

@SideOnly(Side.CLIENT)
public class UpdateNotifier implements McColorHelper {

    public static String SEPARATION_MESSAGE = "\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC" +
            "\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC" +
            "\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC";
    static boolean done = false;
    //######################################################################################################################
    private static String KEY_VERSION = "Version";
    private static String KEY_UPDATEMESSAGE = "UpdateMessage";
    private static String KEY_DOWNLOADLINK = "DownloadLink";
    //######################################################################################################################
    private static String LINK_TO_UPDATEFILE = "https://raw.githubusercontent.com/unaussprechlich/HudPixelExtended/1.8.9-release/checkforversion/Version.json";

    //pauses the thread to
    public UpdateNotifier(boolean wait) {
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getHttpRequest();
            }
        }.start();
    }

    private void getHttpRequest() {

        try {

            URL u = new URL(LINK_TO_UPDATEFILE);
            HttpsURLConnection con = (HttpsURLConnection) u.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

            StringBuilder sBuilder = new StringBuilder();
            String buff = "";
            while ((buff = br.readLine()) != null) {
                sBuilder.append(buff);
            }
            String data = sBuilder.toString();
            out.println(data);
            JsonObject jsonObject = jsonParser(data);
            if (!getStringFromJson(KEY_VERSION, jsonObject).equalsIgnoreCase(DEFAULT_VERSION) && DEFAULT_VERSION.contains("dev")) {
                printUpdateMessage(jsonObject);
                UpdateHandlerKt.receiveUpdate(getStringFromJson(KEY_VERSION, jsonObject));
            }
        } catch (MalformedURLException e) {
            logError("[UpdateNotifier]: Something went wrong while loading the URL for the update file");
            e.printStackTrace();
        } catch (IOException e) {
            logError("[UpdateNotifier]: Something went wrong while reading the update file!");
            e.printStackTrace();
        }

    }

    private String getStringFromJson(String key, JsonObject jsonObject) {
        try {
            if (jsonObject.get(key) != null) {
                return removeQuotes(jsonObject.get(key).toString());
            } else {
                logWarn("[UpdateNotifier]: Key '" + key + "' not Found in Json!");
                return "";
            }
        } catch (Exception e) {
            logError("[UpdateNotifier]: Something went wrong while extracting Key '" + key + "' from Json!");
            e.printStackTrace();
            return D_RED + "Something went wrong while extracting Key '" + key + "' from Json!";
        }

    }

    private JsonObject jsonParser(String data) {
        JsonParser jsonParser = new JsonParser();
        return jsonParser.parse(data).getAsJsonObject();
    }

    private String removeQuotes(String s) {
        return s.replace("\"", "");
    }

    /**
     * Yeah, best code-style EU .... it's messy but it's just for generating a update message,
     * so no need for being nice code :P
     *
     * @param jsonObject updateJson Array
     */
    private void printUpdateMessage(JsonObject jsonObject) {
        if (done) return;
        done = true;
        printMessage(GOLD + SEPARATION_MESSAGE);
        printMessage("");

        //GOING TO PRINT THE DOWLOADLINK
        printMessage(GOLD + "------" + GREEN + " A new version of " + WHITE + "[" + GOLD + "Hud"
                + TextFormatting.YELLOW + "Pixel" + WHITE + "]" + GREEN + " has been published" + GOLD + " ------");
        new ChatMessageComposer("v" + getStringFromJson(KEY_VERSION, jsonObject), TextFormatting.YELLOW).appendMessage
                (new ChatMessageComposer(" > click here to download the newest version < ", TextFormatting.ITALIC)
                        .makeLink(getStringFromJson(KEY_DOWNLOADLINK, jsonObject))).send();

        printMessage("");

        //GOING TO PRINT THE UPDATE NOE OR DIE TEXT
        printMessage(GRAY + "You are currently running v" +DEFAULT_VERSION + "! This version will now no longer be supported " +
                "by the HudPixelTeam! Make sure to update to the newest version befor sending any bug-reports or feature requests!" +
                " HudPixel Reloaded v3 is still in development state, so expect bugs and new features at any time!");

        printMessage("");

        //GOING TO PRINT THE CURRENT BUGREPORTMESSAGE
        printMessage(GOLD + "------" + GREEN + " You can enter a bugreport directly on GitHub " + GOLD + "------");
        new ChatMessageComposer(" press this link to report a bug on GitHub", RED)
                .makeLink("https://github.com/unaussprechlich/HudPixelExtended/issues").send();

        //GOING TO PRINT THE CHANGELOG
        printMessage("");
        printMessage(GOLD + "----------------- " + GREEN + "Changelog for "
                + TextFormatting.YELLOW + "v" + getStringFromJson(KEY_VERSION, jsonObject)
                + GOLD + " -----------------");
        JsonObject jsonObject2 = jsonObject.get("UpdateMessages").getAsJsonObject();
        for (int i = 1; true; i++) {
            String buff;
            if ((!(buff = getStringFromJson(i + "", jsonObject2)).equals(""))) {
                printChangelogStyle(buff);
            } else {
                break;
            }
        }

        printMessage(GOLD + SEPARATION_MESSAGE);
    }

    /**
     * colors the brackets in the right color
     *
     * @param s text to print
     */
    private void printChangelogStyle(String s) {
        if (s.startsWith("[+]")) {
            printMessage(GREEN + "[+]"
                    + printChangelogText(s.substring(3))
            );
        } else if (s.startsWith("[-]")) {
            printMessage(D_RED + "[-]"
                    + printChangelogText(s.substring(3))
            );
        } else {
            printMessage(TextFormatting.YELLOW + s.substring(0, s.indexOf("]") + 1)
                    + printChangelogText(s.substring(s.indexOf("]") + 1)));
        }
    }

    /**
     * makes everything between ' those quotes white
     *
     * @param s text to process
     * @return the processed text
     */
    private String printChangelogText(String s) {
        String[] singleWords = s.split(" ");
        StringBuilder sBuilder = new StringBuilder();
        for (String i : singleWords) {
            if (i.startsWith("'") && i.endsWith("'")) {
                i = i.replace("'", "");
                sBuilder.append(WHITE + " ").append(i);
            } else {
                sBuilder.append(GRAY + " ").append(i);
            }
        }
        return sBuilder.toString();
    }

    /**
     * prints the message to the clientchat
     *
     * @param message the message
     **/
    private void printMessage(String message) {
        new ChatMessageComposer(message).send();
    }

}
