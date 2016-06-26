// HypixelAPI (c) 2014
// Code based on https://github.com/HypixelDev/PublicAPI/commit/0180d6af7c7cb477978c24ba384452e93f30a7b4
// This is a temporary copyright header which will be replaced when a official header is added.

package net.hypixel.api.reply;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@SuppressWarnings("unused")
public class SessionReply extends AbstractReply {
    private JsonElement session;

    /**
     * @return The session, or null if one wasn't found
     */
    public JsonObject getSession() {
        if(session.isJsonNull()) {
            return null;
        } else {
            return session.getAsJsonObject();
        }
    }

    @Override
    public String toString() {
        return "SessionReply{" +
                "session=" + session +
                ",super=" + super.toString() + "}";
    }
}