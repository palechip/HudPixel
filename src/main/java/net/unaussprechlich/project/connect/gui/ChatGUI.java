package net.unaussprechlich.project.connect.gui;

import com.palechip.hudpixelmod.ChatDetector;
import com.palechip.hudpixelmod.HudPixelMod;
import kotlin.Unit;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.unaussprechlich.hypixel.helper.HypixelRank;
import net.unaussprechlich.managedgui.lib.databases.Player.PlayerModel;
import net.unaussprechlich.managedgui.lib.event.EnumDefaultEvents;
import net.unaussprechlich.managedgui.lib.event.util.Event;
import net.unaussprechlich.managedgui.lib.gui.GUI;
import net.unaussprechlich.managedgui.lib.handler.MouseHandler;
import net.unaussprechlich.managedgui.lib.templates.defaults.container.*;
import net.unaussprechlich.managedgui.lib.templates.tabs.containers.TabContainer;
import net.unaussprechlich.managedgui.lib.templates.tabs.containers.TabListElementContainer;
import net.unaussprechlich.managedgui.lib.templates.tabs.containers.TabManager;
import net.unaussprechlich.managedgui.lib.util.ColorRGBA;
import net.unaussprechlich.managedgui.lib.util.DisplayUtil;
import net.unaussprechlich.managedgui.lib.util.RGBA;
import net.unaussprechlich.managedgui.lib.util.RenderUtils;
import org.lwjgl.opengl.GL11;

import java.util.UUID;

/**
 * ChatGUI Created by Alexander on 24.02.2017.
 * Description:
 **/
public class ChatGUI extends GUI{

    private final TabManager tabManager = new TabManager();

    private static final int WIDTH  = 500;
    private static final int HEIGHT = 200;

    private static DefScrollableContainer scrollALL = new DefScrollableContainer(RGBA.P1B1_DEF.get(), WIDTH , HEIGHT - 17, new IScrollSpacerRenderer() {
        @Override
        public void render(int xStart, int yStart, int width) {
            RenderUtils.rect_fade_horizontal_s1_d1(xStart + 25, yStart, width - 42, 2, RGBA.P1B1_DEF.get(), new ColorRGBA(30, 30, 30, 255));
        }

        @Override
        public int getSpacerHeight() {
            return 2;
        }
    });


    public ChatGUI(){

        registerChild(tabManager);


        DefChatMessageContainer test1 = new DefChatMessageContainer(
                new PlayerModel("unausspreclich", UUID.randomUUID(), HypixelRank.ADMIN.get(), new ResourceLocation(HudPixelMod.MODID,"textures/skins/SteveHead.png")),
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.",
                new DefPictureContainer(),
                WIDTH
        );

        DefChatMessageContainer test2 = new DefChatMessageContainer(
                new PlayerModel("hst", UUID.randomUUID(), HypixelRank.YT.get(), new ResourceLocation(HudPixelMod.MODID,"textures/skins/SteveHead.png")),
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.",
                new DefPictureContainer(),
                WIDTH
        );

        DefChatMessageContainer test3 = new DefChatMessageContainer(
                new PlayerModel("EpicNewb", UUID.randomUUID(), HypixelRank.DEFAULT.get(), new ResourceLocation(HudPixelMod.MODID,"textures/skins/SteveHead.png")),
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.",
                new DefPictureContainer(),
                WIDTH
        );

        DefChatMessageContainer test4 = new DefChatMessageContainer(
                new PlayerModel("ciu22", UUID.randomUUID(), HypixelRank.MOD.get(), new ResourceLocation(HudPixelMod.MODID,"textures/skins/SteveHead.png")),
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.",
                new DefPictureContainer(),
                WIDTH
        );

        DefChatMessageContainer test5 = new DefChatMessageContainer(
                new PlayerModel("ausspreclich", UUID.randomUUID(), HypixelRank.MVP_PLUS.get(), new ResourceLocation(HudPixelMod.MODID,"textures/skins/SteveHead.png")),
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.",
                new DefPictureContainer(),
                WIDTH
        );



        test1.addMessage("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.");
        test1.addMessage("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.");
        test2.addMessage("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.");
        test2.addMessage("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.");
        test3.addMessage("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.");
        test3.addMessage("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.");
        test4.addMessage("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.");
        test4.addMessage("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.");
        test5.addMessage("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.");
        test5.addMessage("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.");

        scrollALL.registerScrollElement(new DefCenteredLoadingContainer(WIDTH, 50));
        scrollALL.registerScrollElement(test1);
        scrollALL.registerScrollElement(test2);
        scrollALL.registerScrollElement(test3);
        scrollALL.registerScrollElement(test4);
        scrollALL.registerScrollElement(test5);


        tabManager.registerTab(new TabContainer(new TabListElementContainer("ALL", RGBA.WHITE.get(), tabManager), scrollALL, tabManager));
        tabManager.registerTab(new TabContainer(new TabListElementContainer("Party", RGBA.BLUE.get(), tabManager), new DefBackgroundContainerFrame(WIDTH, HEIGHT, RGBA.BLUE.get()), tabManager));
        tabManager.registerTab(new TabContainer(new TabListElementContainer("Guild", RGBA.GREEN.get(), tabManager), new DefBackgroundContainerFrame(WIDTH, HEIGHT, RGBA.GREEN.get()), tabManager));
        tabManager.registerTab(new TabContainer(new TabListElementContainer("Private", RGBA.RED.get(), tabManager), new DefBackgroundContainerFrame(WIDTH, HEIGHT, RGBA.RED.get()), tabManager));

        updatePosition();

        ChatDetector.INSTANCE.registerEventHandler(ChatDetector.PrivateMessage.INSTANCE, (chatDetector, eventInfo) -> {
                   addChatMessage(eventInfo.getData().get("name"), eventInfo.getData().get("message"));
                   return Unit.INSTANCE;
            }
        );

    }

    public static void addChatMessage(String name, String message){
        scrollALL.registerScrollElement(
                new DefChatMessageContainer(
                        new PlayerModel(name, UUID.randomUUID(), HypixelRank.MVP_PLUS.get(), new ResourceLocation(HudPixelMod.MODID,"textures/skins/SteveHead.png")),
                        message,
                        new DefPictureContainer(),
                        WIDTH
                )
        );
    }

    private void updatePosition(){
        tabManager.setXOffset(5);
        tabManager.setYOffset(DisplayUtil.getScaledMcHeight() - HEIGHT - 17 - 30);
    }


    @Override
    public boolean doClientTick() {
        //TODO OFFSET RENDERING IS BROKEN! MAYBE IN CONTAINER_FRAME
        return true;
    }

    @Override
    public boolean doRender(int xStart, int yStart) {
        GL11.glColor3f(1f, 1f, 1f);
        //GuiInventory.drawEntityOnScreen(1000, 500, 100, MouseHandler.getmX(), MouseHandler.getmY(), Minecraft.getMinecraft().thePlayer);


        return true;
    }

    @Override
    public boolean doChatMessage(ClientChatReceivedEvent e) {
        return true;
    }

    @Override
    public boolean doMouseMove(int mX, int mY) {
        return true;
    }

    @Override
    public boolean doScroll(int i) {
        return true;
    }

    @Override
    public boolean doClick(MouseHandler.ClickType clickType) {
        return true;
    }

    @Override
    public <T extends Event> boolean doEventBus(T event) {
        if(event.getID() == EnumDefaultEvents.SCREEN_RESIZE.get()){
            updatePosition();
        }
        return true;
    }

    @Override
    public boolean doOpenGUI(GuiOpenEvent e) {
        return true;
    }

    @Override
    public boolean doResize() {
        return true;
    }
}
