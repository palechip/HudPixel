// HypixelAPI (c) 2014
// Code based on https://github.com/HypixelDev/PublicAPI/commit/0180d6af7c7cb477978c24ba384452e93f30a7b4
// This is a temporary copyright header which will be replaced when a official header is added.

package net.hypixel.api.http;

import com.google.gson.Gson;
import net.hypixel.api.util.Callback;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPClient {
    private Gson gson = new Gson();

    public <T> void get(String url, Callback<T> callback) {
        try {
            URL urlObj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
            connection.setRequestMethod("GET");
            connection.addRequestProperty("User-Agent", "Mozilla/4.76");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            String result = "";
            while((line = reader.readLine()) != null) {
                result = result+line;
            }
            reader.close();
            T out = gson.fromJson(result, callback.getClazz());
            callback.callback(null, out);
        } catch (Throwable e) {
            callback.callback(e, null);
        }
    }
}
