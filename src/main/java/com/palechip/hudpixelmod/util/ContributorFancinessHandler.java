package com.palechip.hudpixelmod.util;

import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.extended.HudPixelExtendedEventHandler;
import com.palechip.hudpixelmod.extended.util.IEventHandler;
import kotlin.Pair;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.palechip.hudpixelmod.extended.HudPixelExtendedEventHandler.unregisterIEvent;
import static com.palechip.hudpixelmod.extended.util.LoggerHelper.logInfo;
import static com.palechip.hudpixelmod.extended.util.LoggerHelper.logWarn;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static javax.imageio.ImageIO.read;
import static net.minecraft.client.Minecraft.getMinecraft;

public final class ContributorFancinessHandler implements LayerRenderer<EntityPlayer> {
    private volatile static Map<String, Option<ItemStack, LoadImgur>> stacks = new HashMap<>();;
    private volatile static boolean startedLoading = false;

    //https://www.youtube.com/watch?v=41aGCrXM20E
    //@ConfigPropertyBoolean(category = CCategory.HUDPIXEL, id = "displayYourOwn", comment = "Should display your own Contributor Render? (If you don't know what that is, ignore it)", def = true)
    public static boolean displayYourOwn = true;

    public ContributorFancinessHandler() {
        Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();
        RenderPlayer render;
        render = skinMap.get("default");
        render.addLayer(this);
        render = skinMap.get("slim");
        render.addLayer(this);
    }

    public static void init() {
        new ContributorFancinessHandler();
    }

    private static void firstStart() {
        if (!startedLoading) {
            new ThreadContributorListLoader();
            startedLoading = true;
        }
    }

    private static void load(Properties props) {
        stacks = new HashMap<>();
        System.out.println("Fanciness time!");
        for (String key : props.stringPropertyNames()) {
            String value = props.getProperty(key);
            System.out.println("Loading fanciness for " + key + ": " + value);
            try {
                int i = Integer.parseInt(value);
                stacks.put(key.toLowerCase(), new Option<>(new ItemStack(Item.getItemById(i)), null));
            } catch (NumberFormatException e) {
                if (Item.getByNameOrId("minecraft:" + value) != null) {
                    stacks.put(key.toLowerCase(), new Option<>(new ItemStack(Item.getByNameOrId("minecraft:" + value)), null));
                } else if (Block.getBlockFromName("minecraft:" + value) != null) {
                    stacks.put(key.toLowerCase(), new Option<>(new ItemStack(Block.getBlockFromName("minecraft:" + value)), null));
                } else {
                    stacks.put(key.toLowerCase(), new Option<>(null, new LoadImgur(value, (st) -> {
                    })));
                }
            }
        }
    }

    private static void renderStack(EntityPlayer player, Object object) {
        if (object instanceof ItemStack) {
            ItemStack item = (ItemStack) object;
            GlStateManager.pushMatrix();
            translateToHeadLevel(player);
            getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            GlStateManager.rotate(180, 0, 0, 1);
            GlStateManager.translate(0, -0.85, 0);
            GlStateManager.rotate(-90, 0, 1, 0);
            if (item.getItem() instanceof ItemBlock)
                GlStateManager.scale(1, 1, 1);
            else GlStateManager.scale(0.5, 0.5, 0.5);
            getMinecraft().getRenderItem().renderItem(item, ItemCameraTransforms.TransformType.NONE);
            GlStateManager.popMatrix();
        } else if (object instanceof LoadImgur) {
            LoadImgur lmi = (LoadImgur) object;
            GlStateManager.pushMatrix();
            translateToHeadLevel(player);
            GlStateManager.rotate(180, 0, 0, 1);
            GlStateManager.translate(0, -0.85, 0);
            GlStateManager.rotate(-90, 0, 1, 0);
            GlStateManager.rotate(90, 0, 0, 1);
            GlStateManager.scale(1, 1, 1);
            if (lmi.resourceLocation == null) return;
            getMinecraft().renderEngine.bindTexture(lmi.resourceLocation);
            GlStateManager.enableTexture2D();
            double width = 1;
            double height = 1;
            double left = width / 2;
            double bottom = height / 4;

            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer vb = tessellator.getWorldRenderer();

            vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            vb.pos(height - bottom, -left, 0).tex(0, 0).endVertex();
            vb.pos(height - bottom, width - left, 0).tex(1, 0).endVertex();
            vb.pos(-bottom, width - left, 0).tex(1, 1).endVertex();
            vb.pos(-bottom, -left, 0).tex(0, 1).endVertex();
            tessellator.draw();
            GlStateManager.disableTexture2D();
            GlStateManager.popMatrix();
        }
    }

    private static void translateToHeadLevel(EntityPlayer player) {
        GlStateManager.translate(0, -player.getDefaultEyeHeight(), 0);
        if (player.isSneaking())
            GlStateManager.translate(0.25F * MathHelper.sin(player.rotationPitch * (float) Math.PI / 180), 0.25F * MathHelper.cos(player.rotationPitch * (float) Math.PI / 180), 0F);
    }

    @Override
    public void doRenderLayer(@Nonnull EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        String name = player.getName();
        float yaw = player.prevRotationYawHead + (player.rotationYawHead - player.prevRotationYawHead) * partialTicks;
        float yawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks;
        float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;
        GlStateManager.pushMatrix();
        GlStateManager.rotate(yawOffset, 0, -1, 0);
        GlStateManager.rotate(yaw - 270, 0, 1, 0);
        GlStateManager.rotate(pitch, 0, 0, 1);
        firstStart();
        if (!displayYourOwn) {
            stacks.remove(Minecraft.getMinecraft().thePlayer.getName());
        }
        if (stacks.keySet().stream().map((s) -> {
            if (s == null) return null;
            else return s.toLowerCase();
        }).collect(Collectors.toList()).contains(name.toLowerCase()))
            renderStack(player, stacks.get(name.toLowerCase()).invoke());
        GlStateManager.popMatrix();
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    private static class ThreadContributorListLoader extends Thread {
        public ThreadContributorListLoader() {
            setName("Contributor Fanciness Thread");
            setDaemon(true);
            start();
        }

        @Override
        public void run() {
            try {
                URL url = new URL("https://raw.githubusercontent.com/Eladkay/static/master/HudPixelSupporters");
                Properties props = new Properties();
                try (InputStreamReader reader = new InputStreamReader(url.openStream())) {
                    props.load(reader);
                    load(props);
                }
            } catch (IOException e) {
                HudPixelMod.instance().getLOGGER().info("Could not load contributors list. Either you're offline or github is down. Nothing to worry about, carry on~");
            }
        }
    }

    public static class LoadImgur implements IEventHandler {

        public ResourceLocation resourceLocation;
        public String origin;
        private BufferedImage image;
        private boolean imageLoaded = false;
        private boolean imageSetup = false;
        private String thing;
        private IImgurCallback callback;

        public LoadImgur(String thing, IImgurCallback callback) {
            HudPixelExtendedEventHandler.registerIEvent(this);
            this.callback = callback;
            this.thing = thing;
            loadFromURL();
        }

        private void setupImage() {
            imageSetup = true;
            if (image == null) {
                callback.onImgurCallback(null);
                unregisterIEvent(this);
                return;
            }
            DynamicTexture texture = new DynamicTexture(image);
            resourceLocation = getMinecraft().getTextureManager().getDynamicTextureLocation(thing, texture);
            logInfo("[LoadContributors]: Loaded info for " + thing + " @ " + "http://i.imgur.com/" + thing + ".png");
            callback.onImgurCallback(this.resourceLocation);
            unregisterIEvent(this);
        }

        private void loadFromURL() {

            new Thread() {
                @Override
                public void run() {

                    final ExecutorService service;

                    service = newSingleThreadExecutor();
                    Pair<BufferedImage, String> pair = null;
                    Future<Pair<BufferedImage, String>> future;
                    boolean failed;
                    try {
                        future = service.submit(new CallURL());
                        pair = future.get();
                    } catch (final InterruptedException ex) {
                        logWarn("[LoadContributors]: Something went wrong while loading the contributor info for" + thing);
                        ex.printStackTrace();
                    } catch (final ExecutionException ex) {
                        logWarn("[LoadContributors]: Something went wrong while loading the contributor info for" + thing);
                        ex.printStackTrace();
                        try {
                            image = read(new URL("http://i.imgur.com/" + thing + ".png"));
                            imageLoaded = true;
                        } catch (MalformedURLException e) {
                            failed = true;
                            logWarn("[LoadContributors]: Couldn't load contributor info for " + thing + " @ " + "http://i.imgur.com/" + thing + ".png");
                        } catch (IOException e) {
                            failed = true;
                            logWarn("[LoadContributors]: Couldn't read contributor info for " + thing + " @ " + "http://i.imgur.com/" + thing + ".png");
                        }
                    }
                    image = pair.component1();
                    origin = pair.component2();
                    logInfo("[LoadContributors]: Info loaded for " + thing);


                    imageLoaded = true;

                    service.shutdownNow();
                }
            }.start();
        }

        @Override
        public void onClientTick() {
            if (imageLoaded && !imageSetup) setupImage();
        }

        @Override
        public void onChatReceived(ClientChatReceivedEvent e) {

        }

        @Override
        public void onRender() {

        }

        @Override
        public void handleMouseInput(int i, int mX, int mY) {

        }

        @Override
        public void onMouseClick(int mX, int mY) {

        }

        /**
         * Helper class to get the image via url request and filereader
         */
        class CallURL implements Callable<Pair<BufferedImage, String>> {

            public Pair<BufferedImage, String> call() throws Exception {
                return new Pair<>(read(new URL("http://i.imgur.com/" + thing + ".png")), "http://i.imgur.com/" + thing + ".png");
            }
        }

    }
}