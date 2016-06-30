package com.palechip.hudpixelmod.extended.update;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.palechip.hudpixelmod.HudPixelProperties;
import com.palechip.hudpixelmod.extended.util.LoggerHelper;
import com.palechip.hudpixelmod.extended.util.McColorHelper;
import com.palechip.hudpixelmod.util.ChatMessageComposer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/******************************************************************************
 * HudPixelExtended by unaussprechlich(github.com/unaussprechlich/HudPixelExtended),
 * an unofficial Minecraft Mod for the Hypixel Network.
 * <p>
 * Original version by palechip (github.com/palechip/HudPixel)
 * "Reloaded" version by PixelModders -> Eladkay (github.com/PixelModders/HudPixel)
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
public class UpdateNotifier implements McColorHelper {

//######################################################################################################################
    private static String KEY_VERSION = "Version";
    private static String KEY_UPDATEMESSAGE = "UpdateMessage";
    private static String KEY_DOWNLOADLINK = "DownloadLink";
    private static String LINK_TO_UPDATEFILE = "https://raw.githubusercontent.com/unaussprechlich/HudPixelExtended/1.8.9-unaussprechlich-experimental/checkforversion/Version.json";
    private static String SEPARATION_MESSAGE = "\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC" +
            "\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC" +
            "\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC";
//######################################################################################################################

    //pauses the thread to
    public UpdateNotifier(boolean wait){
        new Thread(){
            @Override
            public void run(){
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getHttpRequest();
            }
        }.start();

    }

    private void getHttpRequest(){

        try {

            URL u = new URL(LINK_TO_UPDATEFILE);
            HttpsURLConnection con = (HttpsURLConnection) u.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

            StringBuilder sBuilder = new StringBuilder();
            String buff = "";
            while(( buff = br.readLine()) != null){
                sBuilder.append(buff);
            }
            String data = sBuilder.toString();
            System.out.println(data);
            JsonObject jsonObject= jsonParser(data);
            if(!getStringFromJson(KEY_VERSION, jsonObject).equalsIgnoreCase(HudPixelProperties.VERSION))
                printUpdateMessage(jsonObject);
        } catch (MalformedURLException e) {
            LoggerHelper.logError("[UpdateNotifier]: Something went wrong while loading the URL for the update file");
            e.printStackTrace();
        } catch (IOException e) {
            LoggerHelper.logError("[UpdateNotifier]: Something went wrong while reading the update file!");
            e.printStackTrace();
        }

    }

    private String getStringFromJson(String key, JsonObject jsonObject){
        try{
            if(jsonObject.get(key) != null){
                return removeQuotes(jsonObject.get(key).toString());
            } else {
                LoggerHelper.logWarn("[UpdateNotifier]: Key '" + key + "' not Found in Json!");
                return "";
            }
        } catch (Exception e){
            LoggerHelper.logError("[UpdateNotifier]: Something went wrong while extracting Key '" + key + "' from Json!");
            e.printStackTrace();
            return D_RED +"Something went wrong while extracting Key '" + key + "' from Json!";
        }

    }

    private JsonObject jsonParser(String data){
        JsonParser jsonParser = new JsonParser();
        return jsonParser.parse(data).getAsJsonObject();
    }

    private String removeQuotes(String s){
        return s.replace("\"", "");
    }


    /**
     * Yeah, best code-style EU .... it's messy but it's just for generating a update message,
     * so no need for being nice code :P
     * @param jsonObject updateJson Array
     */
    private void printUpdateMessage(JsonObject jsonObject){

        printMessage(GOLD + SEPARATION_MESSAGE);

        printMessage("");

        //GOING TO PRINT THE DOWLOADLINK
        printMessage(GOLD + "------" + GREEN +" A new version of " + WHITE + "[" + GOLD + "Hud"
                + EnumChatFormatting.YELLOW + "Pixel" + WHITE + "]" + GREEN + " has been published" +  GOLD + " ------");
        new ChatMessageComposer("v" + getStringFromJson(KEY_VERSION, jsonObject), EnumChatFormatting.YELLOW).appendMessage
                (new ChatMessageComposer(" > click here to download the newest version < ", EnumChatFormatting.ITALIC)
                        .makeLink(getStringFromJson(KEY_DOWNLOADLINK, jsonObject))).send();

        printMessage("");

        //GOING TO PRINT THE UPDATE NOE OR DIE TEXT
        printMessage(GRAY + "Your currently running v" + HudPixelProperties.VERSION + "! This version will now no longer be supported " +
                "by the HudPixelTeam! So make sure to update to the newest version befor sending any bug-reports or feature requests!" +
                " HudPixel Reloaded v3 is still in development state, so expect bugs and new features at any time!");

        printMessage("");

        //GOING TO PRINT THE CURRENT BUGREPORTMESSAGE
        printMessage(GOLD + "------" + GREEN + " You can enter a bugreports directly on GitHub " + GOLD + "------");
        new ChatMessageComposer(" press this link to report a bug on GitHub", RED)
                        .makeLink("https://github.com/HudPixel/HudPixelExtended/issues").send();

        //GOING TO PRINT THE CHANGELOG
        printMessage("");
        printMessage(GOLD  + "----------------- " + GREEN + "Changelog for "
                    + EnumChatFormatting.YELLOW + "v" + getStringFromJson(KEY_VERSION, jsonObject)
                    + GOLD + " -----------------");
        JsonObject jsonObject2 = jsonObject.get("UpdateMessages").getAsJsonObject();
        for(int i = 1; true; i++){
            String buff;
            if((!(buff = getStringFromJson(i + "", jsonObject2)).equals(""))){
                printChangelogStyle(buff);
            } else {
                break;
            }
        }

        printMessage(GOLD + SEPARATION_MESSAGE);
    }

    /**
     * colors the brackets in the right color
     * @param s text to print
     */
    private void printChangelogStyle(String s){
        if (s.startsWith("[+]")){
            printMessage(GREEN + "[+]"
                    + printChangelogText(s.substring(3))
            );
        } else if (s.startsWith("[-]")){
            printMessage(D_RED + "[-]"
                    + printChangelogText(s.substring(3))
            );
        } else {
            printMessage(EnumChatFormatting.YELLOW + s.substring(0, s.indexOf("]") + 1)
                    + printChangelogText(s.substring(s.indexOf("]") + 1)));
        }
    }

    /**
     * makes everything between ' those quotes white
     * @param s text to process
     * @return the processed text
     */
    private String printChangelogText(String s){
        String[] singleWords = s.split(" ");
        StringBuilder sBuilder = new StringBuilder();
        for(String i : singleWords){
            if(i.startsWith("'") && i.endsWith("'")){
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
     * @param message the message
     **/
    private void printMessage(String message) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(
                new ChatComponentText(message));
    }

}
