package com.palechip.hudpixelmod;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class HudPixelUpdateNotifier extends Thread{
    public boolean isOutOfDate = false;
    public String newVersion = "";
    public String downloadLink = "";

    public HudPixelUpdateNotifier() {
        try {
            start();
        } catch (Exception e) {
            // there was an error. Lets assume that there is no update
        }
    }

    @Override
    public void run() {
        ArrayList<String> information = new ArrayList<String>();
        try {
            URL remoteFile = new URL("https://raw.githubusercontent.com/palechip/HudPixel/master/update/update.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(remoteFile.openStream()));
            for(int i = 0; i < 2; i++) {
                information.add(reader.readLine());
            }
        } catch (Exception e) {
            // there was an error. Lets assume that there is no update
        }
        // set the link
        this.downloadLink = information.get(1);
        // extract the versions
        String[] modVersion = HudPixelMod.VERSION.split("[.]");
        String[] latestVersion = information.get(0).split("[.]");

        // compare major versions
        if(Integer.valueOf(modVersion[0]) < Integer.valueOf(latestVersion[0])) {
            this.newVersion = information.get(0);
            this.isOutOfDate = true;
            HudPixelMod.instance().logInfo("Found an available update: " + this.newVersion);
            HudPixelMod.instance().updateFound();
        } else if (Integer.valueOf(modVersion[0]) > Integer.valueOf(latestVersion[0])) {
            // we've got a newer build
            return;
        }
        // compare minor versions
        else if(Integer.valueOf(modVersion[1]) < Integer.valueOf(latestVersion[1])) {
            this.newVersion = information.get(0);
            this.isOutOfDate = true;
            HudPixelMod.instance().logInfo("Found an available update: " + this.newVersion);
            HudPixelMod.instance().updateFound();
        } else if (Integer.valueOf(modVersion[1]) > Integer.valueOf(latestVersion[1])) {
            // we've got a newer build
            return;
        }
        // compare revisions
        else if(Integer.valueOf(modVersion[2]) < Integer.valueOf(latestVersion[2])) {
            this.newVersion = information.get(0);
            this.isOutOfDate = true;
            HudPixelMod.instance().logInfo("Found an available update: " + this.newVersion);
            HudPixelMod.instance().updateFound();
        } else if (Integer.valueOf(modVersion[2]) > Integer.valueOf(latestVersion[2])) {
            // we've got a newer build
            return;
        }
    }

}
