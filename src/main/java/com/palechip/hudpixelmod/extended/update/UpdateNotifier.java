package com.palechip.hudpixelmod.extended.update;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
public class UpdateNotifier implements McColorHelper{

    private static String SEPARATION_MESSAGE = "\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC" +
            "\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC" +
            "\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC";


    public UpdateNotifier(){
        getHttpRequest();

    }

    private void getHttpRequest(){

        try {

            URL u = new URL("https://raw.githubusercontent.com/unaussprechlich/HudPixelExtended/1.8.9-unaussprechlich-experimental/checkforversion/version.json");
            HttpsURLConnection con = (HttpsURLConnection) u.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String data = br.readLine();
            System.out.println(data);
            printUpdateMessage(jsonParser(data));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private JsonObject jsonParser(String data){
        JsonParser jsonParser = new JsonParser();
        return jsonParser.parse(data).getAsJsonObject();
    }

    private String removeQuotes(String s){
        return s.replace("\"", "");
    }

    private void printUpdateMessage(JsonObject jsonObject){

        printMessage(GOLD + SEPARATION_MESSAGE);

        printMessage("");

        printMessage(GREEN + "A new version of " + GOLD + "Hud" + EnumChatFormatting.YELLOW + "Pixel" + GREEN + " has been published!");
        new ChatMessageComposer("V" + removeQuotes(jsonObject.get("Version").toString()), WHITE).appendMessage
                (new ChatMessageComposer(" click here to download now", EnumChatFormatting.GRAY)
                        .makeLink(removeQuotes(jsonObject.get("DownloadLink").toString()))).send();

        printMessage("");

        printMessage(GOLD  + "----------------- " + GREEN + "Changelog for "
                    + WHITE + "V" + removeQuotes(jsonObject.get("Version").toString())
                    + GOLD + " -----------------");

        JsonObject jsonObject2 = jsonObject.get("UpdateMessages").getAsJsonObject();
        for(int i = 1; true; i++){
            JsonElement buffE = jsonObject2.get("" + i);
            if(buffE != null){
                printChangelogLine(removeQuotes(buffE.toString()));
            } else {
                break;
            }
        }

        printMessage(GOLD + "-----------------------------------------------------");
        printMessage("");
        printMessage(GOLD + SEPARATION_MESSAGE);

        System.out.println(jsonObject.get("Version"));
    }

    private void printChangelogLine(String s){

        if (s.startsWith("[+]")){
            printMessage(GREEN + "[+]"
                    + GRAY + s.substring(3)
            );
        } else if (s.startsWith("[-]")){
            printMessage(D_RED + "[-]"
                    + GRAY + s.substring(3)
            );
        } else {
            printMessage(BLUE + s.substring(0, s.indexOf("]") + 1)
                    + GRAY + s.substring(s.indexOf("]") + 1));
        }
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
