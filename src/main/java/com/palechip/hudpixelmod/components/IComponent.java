package com.palechip.hudpixelmod.components;

public interface IComponent {
    /**
     * This is called when the mod has detected that the player joined a game of this type.
     * It should reset the rendered strings to the default ones.
     */
    public void setupNewGame();
    
    /**
     * Called when the game starts.
     */
    public void onGameStart();
    
    /**
     * Called when the game ends.
     */
    public void onGameEnd();
    
    /**
     * If the game is running, it'll receive ticks to update the rendered strings.
     */
    public void onTickUpdate();
    
    /**
     * If the game is running, it'll receive the chat messages which the client receives.
     */
    public void onChatMessage(String textMessage, String formattedMessage);
    
    /**
     * Gets the string the component wants to render.
     * @return
     */
    public String getRenderingString();
}
