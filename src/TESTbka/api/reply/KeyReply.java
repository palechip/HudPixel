// HypixelAPI (c) 2014
// Code based on https://github.com/HypixelDev/PublicAPI/commit/0180d6af7c7cb477978c24ba384452e93f30a7b4
// This is a temporary copyright header which will be replaced when a official header is added.

package net.hypixel.api.reply;

import java.util.UUID;

@SuppressWarnings("unused")
public class KeyReply extends AbstractReply {
    private Key record;

    public Key getRecord() {
        return record;
    }

    public class Key {
        private String key;
        private String owner;
        private int queriesInPastMin;

        public UUID getKey() {
            return UUID.fromString(key);
        }

        public String getOwner() {
            return owner;
        }

        public int getQueriesInPastMin() {
            return queriesInPastMin;
        }

        @Override
        public String toString() {
            return "Key{" +
                    "key='" + key + '\'' +
                    ", owner='" + owner + '\'' +
                    ", queriesInPastMin=" + queriesInPastMin +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "KeyReply{" +
                "record=" + record +
                ",super=" + super.toString()+"}";
    }
}
