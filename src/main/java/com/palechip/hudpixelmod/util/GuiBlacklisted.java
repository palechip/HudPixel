package com.palechip.hudpixelmod.util;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class GuiBlacklisted extends GuiScreen {
    private final String reason;

    private GuiButton btnAppeal;
    private GuiButton btnQuit;

    public GuiBlacklisted(String reason) {
        this.reason = reason;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button == btnAppeal) try {
            Desktop.getDesktop().browse(new URI("https://hypixel.net/conversations/add?to=unaussprechlich,Eladkay"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        else if (button == btnQuit) {
            BansHandler.init();
            button.enabled = false;
            this.mc.displayGuiScreen(new GuiMainMenu());
        }

    }

    @Override
    public void initGui() {
        this.buttonList.add(this.btnAppeal = new GuiButton(102, (this.width - 175) / 2, this.height / 2 - 25, "Appeal (Will open web browser)"));
        this.buttonList.add(this.btnQuit = new GuiButton(104, (this.width - 175) / 2, this.height / 2, "Quit"));
        if(mc.theWorld != null)
            this.mc.theWorld.sendQuittingDisconnectingPacket();
        this.mc.loadWorld(null);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawBackground(0);
        this.drawCenteredString(this.fontRendererObj, "You were banned from using HudPixel for the following reason:", this.width / 2, this.height / 2 - 75, 16777215);
        this.drawCenteredString(this.fontRendererObj, reason, this.width / 2, this.height / 2 - 50, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
