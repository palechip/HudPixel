package com.palechip.hudpixelmod.components;

import com.palechip.hudpixelmod.stats.BlitzStatsDisplayer;
import com.palechip.hudpixelmod.util.ChatMessageComposer;
import com.palechip.hudpixelmod.util.GameType;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;

public class AfterStatsComponent implements IComponent {
    private GameType type;

    public AfterStatsComponent(int modid) {
        this.type = GameType.getTypeByID(modid);
    }

    @Override
    public void setupNewGame() {}

    @Override
    public void onGameStart() {}

    @Override
    public void onGameEnd() {}

    @Override
    public void onTickUpdate() {}

    @Override
    public void onChatMessage(String textMessage, String formattedMessage) {
        String username = FMLClientHandler.instance().getClient().getSession().getUsername();
        switch (this.type) {
        case BLITZ:
             if(textMessage.contains(username + " was killed")) {
                if (textMessage.contains(" by ")) {
                    String killer;
                    String messageStartingWithKiller = textMessage.substring(textMessage.indexOf("by ") + 3);
                    if(messageStartingWithKiller.contains(" ")) {
                        killer = messageStartingWithKiller.substring(0, messageStartingWithKiller.indexOf(' ')).replace("!", "");
                    } else  {
                        killer = messageStartingWithKiller.replace("!", "");
                    }
                    new BlitzStatsDisplayer(killer).display();
                } else {
                    new ChatMessageComposer("There is nobody to get the stats. :(", EnumChatFormatting.GREEN).send();
                }
            }
            break;
        default:
            // not implemented for all other games
            break;
        }
    }

    @Override
    public String getRenderingString() {
        // this will never render anything
        return "";
    }

}
