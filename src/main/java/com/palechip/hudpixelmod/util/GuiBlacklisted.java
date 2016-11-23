package com.palechip.hudpixelmod.util;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class GuiBlacklisted extends GuiScreen {
    private final String reason;

    private GuiButton btnAppeal;

    public GuiBlacklisted(String reason) {
        this.reason = reason;
        this.btnAppeal = new GuiButton(102, this.width / 2, this.height / 2 - 25, "Appeal");
        this.buttonList.add(btnAppeal);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button == btnAppeal) try {
            Desktop.getDesktop().browse(new URI("https://hypixel.net/conversations/add?to=unaussprechlich,Eladkay"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawBackground(0);
        this.drawCenteredString(this.fontRendererObj, "You were banned from using HudPixel for the following reason:", this.width / 2, this.height / 2 - 75, 16777215);
        this.drawCenteredString(this.fontRendererObj, reason, this.width / 2, this.height / 2 - 50, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
