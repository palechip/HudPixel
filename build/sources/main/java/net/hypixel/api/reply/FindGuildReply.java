// HypixelAPI (c) 2014
// Code based on https://github.com/HypixelDev/PublicAPI/commit/0180d6af7c7cb477978c24ba384452e93f30a7b4
// This is a temporary copyright header which will be replaced when a official header is added.

package net.hypixel.api.reply;

@SuppressWarnings("unused")
public class FindGuildReply extends AbstractReply {
    private String guild;

    /**
     * @return The ID of the guild that was found, or null if there was no guild by that name
     */
    public String getGuild() {
        return guild;
    }

    @Override
    public String toString() {
        return "FindGuildReply{" +
                "guild=" + guild +
                ",super=" + super.toString() + "}";
    }
}
