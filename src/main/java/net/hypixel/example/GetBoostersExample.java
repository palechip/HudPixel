package net.hypixel.example;

import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.BoostersReply;
import net.hypixel.api.request.Request;
import net.hypixel.api.request.RequestBuilder;
import net.hypixel.api.request.RequestType;
import net.hypixel.api.util.Callback;

import java.util.UUID;

public class GetBoostersExample {
    public static void main(String[] args) {
        HypixelAPI.getInstance().setApiKey(UUID.fromString("fd08635f-5848-4bd7-a9f6-771f5d93b1de"));

        Request request = RequestBuilder.newBuilder(RequestType.BOOSTERS).createRequest();
        System.out.println(request.getURL(HypixelAPI.getInstance()));
        HypixelAPI.getInstance().getAsync(request, new Callback<BoostersReply>(BoostersReply.class) {
            @Override
            public void callback(Throwable failCause, BoostersReply result) {
                if (failCause != null) {
                    failCause.printStackTrace();
                } else {
                    System.out.println(result);
                }
                HypixelAPI.getInstance().finish();
                System.exit(0);
            }
        });
        ExampleUtil.await(); // This is required because the API is asynchronous, so without this the program will exit.
    }
}