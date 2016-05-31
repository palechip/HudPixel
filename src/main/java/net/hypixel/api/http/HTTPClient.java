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
    private String url;
    private Object callback;

    public <T> void get(String url, Callback<T> callback) {
        this.url = url;
        this.callback = callback;
        new Thread() {
            @Override
            public void run() {
                try {
                    URL urlObj = new URL(HTTPClient.this.url);
                    HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
                    connection.setRequestMethod("GET");
                    connection.addRequestProperty("User-Agent", "Mozilla/5.0");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    String result = "";
                    while((line = reader.readLine()) != null) {
                        result = result+line;
                    }
                    reader.close();
                    T out = gson.fromJson(result, ((Callback<T>)HTTPClient.this.callback).getClazz());
                    ((Callback<T>)HTTPClient.this.callback).callback(null, out);
                } catch(Throwable e) {
                    ((Callback<T>)HTTPClient.this.callback).callback(e, null);
                }
            };
        }.start();
    }
}
