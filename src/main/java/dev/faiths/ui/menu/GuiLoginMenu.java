package dev.faiths.ui.menu;

import com.github.stachelbeere1248.zombiesutils.ZombiesUtils;
import com.google.common.reflect.ClassPath;
import dev.faiths.Faiths;
import dev.faiths.command.impl.ModuleCommand;
import dev.faiths.component.SmoothCameraComponent;
import dev.faiths.config.ConfigManager;
import dev.faiths.module.CheatModule;
import dev.faiths.module.ModuleManager;
import dev.faiths.ui.altmanager.CustomGuiTextField;
import dev.faiths.ui.notifiction.NotificationType;
import dev.faiths.utils.render.RenderUtils;
import ltd.guimc.silencefix.SFIRCListener;
import ltd.guimc.silencefix.SilenceFixIRC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomPanorama;
import net.optifine.CustomPanoramaProperties;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;
import tech.skidonion.obfuscator.annotations.NativeObfuscation;
import tech.skidonion.obfuscator.inline.Wrapper;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

import static dev.faiths.Faiths.IS_BETA;
import static dev.faiths.Faiths.VERSION;

@NativeObfuscation
public class GuiLoginMenu extends GuiScreen implements GuiYesNoCallback {

    /**
     * Timer used to rotate the panorama, increases every tick.
     */
    private int panoramaTimer;

    float hWidth = 960;
    /**
     * Texture allocated for the current viewport of the main menu's panorama
     * background.
     */
    private DynamicTexture viewportTexture;

    /**
     * An array of all the paths to the panorama pictures.
     */
    private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[] {
            new ResourceLocation("textures/gui/title/background/panorama_0.png"),
            new ResourceLocation("textures/gui/title/background/panorama_1.png"),
            new ResourceLocation("textures/gui/title/background/panorama_2.png"),
            new ResourceLocation("textures/gui/title/background/panorama_3.png"),
            new ResourceLocation("textures/gui/title/background/panorama_4.png"),
            new ResourceLocation("textures/gui/title/background/panorama_5.png") };
    private ResourceLocation backgroundTexture;

    private GuiButton loginButton;
    private GuiButton regbutton;
    private String message;

    /**
     * Called from the main game loop to update the screen.
     */

    private CustomGuiTextField textField = new CustomGuiTextField(1, Minecraft.getMinecraft().fontRendererObj,
            this.width / 2 - 100, 0, 200, 20, "Password");
    private CustomGuiTextField textField1 = new CustomGuiTextField(2, Minecraft.getMinecraft().fontRendererObj,
            this.width / 2 - 100, 0, 200, 20, "Username");

    public void updateScreen() {
        ++this.panoramaTimer;
        textField.updateCursorCounter();
        textField1.updateCursorCounter();
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in
     * single-player
     */
    public boolean doesGuiPauseGame() {
        return false;
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the
     * equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key),
     * keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        textField.textboxKeyTyped(typedChar, keyCode);
        textField1.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when
     * the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui() {
        this.message = "Idle...";
        this.viewportTexture = new DynamicTexture(256, 256);
        this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background",
                this.viewportTexture);
        int j = this.height / 4 + 48;
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        hWidth = scaledResolution.getScaledWidth() / 2;
        this.buttonList.add(loginButton = new GuiButton(0, this.width / 2 - 100, j + 72 + 12, "Login"));
        this.buttonList.add(regbutton = new GuiButton(1, this.width / 2 - 100, j + 72, "Register"));
        Keyboard.enableRepeatEvents(true);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for
     * buttons)
     */
    @NativeObfuscation(virtualize = NativeObfuscation.VirtualMachine.TIGER_WHITE)
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            message = "Authenticating...";
            new Thread(this::login).start();
        }
        if (button.id == 1) {
            try {
                Desktop.getDesktop().browse(new URI("https://skidonion.tech/#/register"));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @NativeObfuscation
    private void login() {
        loginButton.enabled = false;

        int result = Wrapper.login(textField1.getText(), textField.getText(), false);
        if (result == 0) {
            Class clazz = null;
            try {
                clazz = Class.forName("java.lang.ProcessEnvironment");
            } catch (ClassNotFoundException ignored) {
            }
            Field field = null;
            try {
                field = clazz.getDeclaredField("theUnmodifiableEnvironment");
            } catch (NoSuchFieldException ignored) {
            }
            try {
                field.setAccessible(true);
            } catch (Throwable e) {
                message = EnumChatFormatting.RED + "You are using a unsupported JVM.";
                return;
            }
            // -1808631973 // stable
            // 1263677895
            // -300222605
            // -1521957196

            // 2066960 // beta
            // 859346722
            // 1568828169
            // 1857748011
            boolean flag = false;
            if (IS_BETA) {
                Optional<String> cloudConstant = Wrapper.getCloudConstant(2066960, 0);
                if (cloudConstant.isPresent()
                        && (Integer.parseInt(cloudConstant.get()) ^ 1568828169) == 859346722) {
                    message = EnumChatFormatting.GREEN + "Done!";
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {

                    }
                    flag = true;
                    loginButton.enabled = true;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {

                    }
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {

                    }
                    loginButton.enabled = true;
                    message = EnumChatFormatting.RED + "Error!";
                    Faiths.notificationManager.pop("Authentication",
                            "Failed login #1-1" + (cloudConstant.isPresent()?"t":"f"),
                            NotificationType.ERROR);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {

                    }
                }

            } else {
                Optional<String> cloudConstant = Wrapper.getCloudConstant(-1808631973, 0);
                if (cloudConstant.isPresent()
                        && (Integer.parseInt(cloudConstant.get()) ^ -300222605) == 1263677895) {
                    message = EnumChatFormatting.GREEN + "Done!";
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {

                    }
                    flag = true;
                    loginButton.enabled = true;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {

                    }
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {

                    }
                    loginButton.enabled = true;
                    message = EnumChatFormatting.RED + "Error!";
                    Faiths.notificationManager.pop("Authentication",
                            "Failed login #1-2",
                            NotificationType.ERROR);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {

                    }
                }
            }

            if (flag) {
                mc.addScheduledTask(this::done);

                Faiths.moduleManager = new ModuleManager();
                Faiths.configManager = new ConfigManager();

                try {
                    final ClassLoader loader = Thread.currentThread().getContextClassLoader();
                    for (final ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClasses()) {
                        if (info.getPackageName().startsWith("dev.faiths") || info.getPackageName().startsWith("dev.jnic")) {
                            final Class<?> clazzs = info.load();
                            if (CheatModule.class.isAssignableFrom(clazzs) && clazzs != CheatModule.class) {
                                try {
                                    Faiths.moduleManager.modules.add((CheatModule) clazzs.newInstance());
                                } catch (Exception ignored) {}
                            }
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                Faiths.moduleManager.modules.sort(Comparator.comparing(CheatModule::getName));
                Faiths.moduleManager.modules.forEach(Faiths.INSTANCE.getEventManager()::registerEvent);
                Faiths.moduleManager.modules.forEach(module -> {
                    if (!module.getValues().isEmpty())
                        Faiths.commandManager.registerCommand(new ModuleCommand(module, module.getValues()));
                });
                Faiths.moduleManager.copiedModules = new ArrayList<>(Faiths.moduleManager.modules);
                Faiths.configManager.loadConfigs();
                Runtime.getRuntime().addShutdownHook(new Thread(Faiths.configManager::saveConfigs));
                Faiths.INSTANCE.getEventManager().registerEvent(Faiths.INSTANCE.getRotationManager());
                Faiths.INSTANCE.getEventManager().registerEvent(new SmoothCameraComponent());
                new ZombiesUtils().init();

                /*if (!IRC.isRunning()) {
                    try {
                        IRC.run(SHA256(String.valueOf(Wrapper.getUsername())));
                    } catch (NoSuchAlgorithmException e) {
                        Faiths.notificationManager.pop("IRC", "An error occurred.", NotificationType.WARNING);
                    }
                }*/

                try {
                    SilenceFixIRC.init();
                    SilenceFixIRC.Instance.connect();
                    Faiths.INSTANCE.getEventManager().registerEvent(new SFIRCListener());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Faiths.isInitializing = false;
            }

        } else {
            switch (result) {
                case 4:
                    loginButton.enabled = true;
                    message = EnumChatFormatting.RED + "Error!";
                    Faiths.notificationManager.pop("Authentication",
                            "The username does not exist or wrong password", NotificationType.ERROR);
                    break;
                case 5:
                    loginButton.enabled = true;
                    message = EnumChatFormatting.RED + "Error!";
                    Faiths.notificationManager.pop("Authentication",
                            "Your account was banned by admin", NotificationType.ERROR);
                    break;
                case 6:
                    loginButton.enabled = true;
                    message = EnumChatFormatting.RED + "Error!";
                    Faiths.notificationManager.pop("Authentication",
                            "this software was stopped servicing, please contact the software admin",
                            NotificationType.ERROR);
                    break;
                case 7:
                    loginButton.enabled = true;
                    message = EnumChatFormatting.RED + "Error!";
                    Faiths.notificationManager.pop("Authentication",
                            "Admin temporarily banned your account, please contact the software admin",
                            NotificationType.ERROR);
                    break;
                case 8:
                    loginButton.enabled = true;
                    message = EnumChatFormatting.RED + "Error!";
                    Faiths.notificationManager.pop("Authentication",
                            "You don't have any subscription of this software", NotificationType.ERROR);
                    break;
                case 9:
                    loginButton.enabled = true;
                    message = EnumChatFormatting.RED + "Error!";
                    Faiths.notificationManager.pop("Authentication",
                            "The software is under maintaining", NotificationType.ERROR);
                    break;
                default:
                    loginButton.enabled = true;
                    message = EnumChatFormatting.RED + "Error!";
                    Faiths.notificationManager.pop("Authentication",
                            String.format("Failed login #0-%s", (int) result), NotificationType.ERROR);
            }
        }
        loginButton.enabled = true;
    }

    @NativeObfuscation
    private void done() {
        Faiths.notificationManager.pop("Authentication",
                "Successfully login as " + Wrapper.getUsername().get(), NotificationType.SUCCESS);
        Faiths.verified = true;
        Display.setTitle("Faiths " + VERSION + "/" + Wrapper.getUsername().get() + (IS_BETA ? "(Beta)" : "(Stable)"));
        this.mc.displayGuiScreen(new GuiMainMenu());
    }

    /**
     * Draws the main menu panorama
     */
    private void drawPanorama(int p_73970_1_, int p_73970_2_, float p_73970_3_) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        int i = 8;
        int j = 64;
        CustomPanoramaProperties custompanoramaproperties = CustomPanorama.getCustomPanoramaProperties();

        if (custompanoramaproperties != null) {
            j = custompanoramaproperties.getBlur1();
        }

        for (int k = 0; k < j; ++k) {
            GlStateManager.pushMatrix();
            float f = ((float) (k % i) / (float) i - 0.5F) / 64.0F;
            float f1 = ((float) (k / i) / (float) i - 0.5F) / 64.0F;
            float f2 = 0.0F;
            GlStateManager.translate(f, f1, f2);
            GlStateManager.rotate(MathHelper.sin(((float) this.panoramaTimer + p_73970_3_) / 400.0F) * 25.0F + 20.0F,
                    1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-((float) this.panoramaTimer + p_73970_3_) * 0.1F, 0.0F, 1.0F, 0.0F);

            for (int l = 0; l < 6; ++l) {
                GlStateManager.pushMatrix();

                if (l == 1) {
                    GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (l == 2) {
                    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                }

                if (l == 3) {
                    GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (l == 4) {
                    GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                }

                if (l == 5) {
                    GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
                }

                ResourceLocation[] aresourcelocation = titlePanoramaPaths;

                if (custompanoramaproperties != null) {
                    aresourcelocation = custompanoramaproperties.getPanoramaLocations();
                }

                this.mc.getTextureManager().bindTexture(aresourcelocation[l]);
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                int i1 = 255 / (k + 1);
                float f3 = 0.0F;
                worldrenderer.pos(-1.0D, -1.0D, 1.0D).tex(0.0D, 0.0D).color(255, 255, 255, i1).endVertex();
                worldrenderer.pos(1.0D, -1.0D, 1.0D).tex(1.0D, 0.0D).color(255, 255, 255, i1).endVertex();
                worldrenderer.pos(1.0D, 1.0D, 1.0D).tex(1.0D, 1.0D).color(255, 255, 255, i1).endVertex();
                worldrenderer.pos(-1.0D, 1.0D, 1.0D).tex(0.0D, 1.0D).color(255, 255, 255, i1).endVertex();
                tessellator.draw();
                GlStateManager.popMatrix();
            }

            GlStateManager.popMatrix();
            GlStateManager.colorMask(true, true, true, false);
        }

        worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }

    /**
     * Rotate and blurs the skybox view in the main menu
     */
    private void rotateAndBlurSkybox(float p_73968_1_) {
        this.mc.getTextureManager().bindTexture(this.backgroundTexture);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.colorMask(true, true, true, false);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        GlStateManager.disableAlpha();
        int i = 3;
        int j = 3;
        CustomPanoramaProperties custompanoramaproperties = CustomPanorama.getCustomPanoramaProperties();

        if (custompanoramaproperties != null) {
            j = custompanoramaproperties.getBlur2();
        }

        for (int k = 0; k < j; ++k) {
            float f = 1.0F / (float) (k + 1);
            int l = this.width;
            int i1 = this.height;
            float f1 = (float) (k - i / 2) / 256.0F;
            worldrenderer.pos((double) l, (double) i1, (double) this.zLevel).tex((double) (0.0F + f1), 1.0D)
                    .color(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos((double) l, 0.0D, (double) this.zLevel).tex((double) (1.0F + f1), 1.0D)
                    .color(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos(0.0D, 0.0D, (double) this.zLevel).tex((double) (1.0F + f1), 0.0D)
                    .color(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos(0.0D, (double) i1, (double) this.zLevel).tex((double) (0.0F + f1), 0.0D)
                    .color(1.0F, 1.0F, 1.0F, f).endVertex();
        }

        tessellator.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask(true, true, true, true);
    }

    /**
     * Renders the skybox in the main menu
     */
    private void renderSkybox(int p_73971_1_, int p_73971_2_, float p_73971_3_) {
        this.mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport(0, 0, 256, 256);
        this.drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        int i = 3;
        CustomPanoramaProperties custompanoramaproperties = CustomPanorama.getCustomPanoramaProperties();

        if (custompanoramaproperties != null) {
            i = custompanoramaproperties.getBlur3();
        }

        for (int j = 0; j < i; ++j) {
            this.rotateAndBlurSkybox(p_73971_3_);
            this.rotateAndBlurSkybox(p_73971_3_);
        }

        this.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        float f2 = this.width > this.height ? 120.0F / (float) this.width : 120.0F / (float) this.height;
        float f = (float) this.height * f2 / 256.0F;
        float f1 = (float) this.width * f2 / 256.0F;
        int k = this.width;
        int l = this.height;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(0.0D, (double) l, (double) this.zLevel).tex((double) (0.5F - f), (double) (0.5F + f1))
                .color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos((double) k, (double) l, (double) this.zLevel).tex((double) (0.5F - f), (double) (0.5F - f1))
                .color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos((double) k, 0.0D, (double) this.zLevel).tex((double) (0.5F + f), (double) (0.5F - f1))
                .color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos(0.0D, 0.0D, (double) this.zLevel).tex((double) (0.5F + f), (double) (0.5F + f1))
                .color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        tessellator.draw();
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY,
     * renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.disableAlpha();
        this.renderSkybox(mouseX, mouseY, partialTicks);
        GlStateManager.enableAlpha();

        RenderUtils.drawWindow(this.width / 2f - 120, this.height / 4f + 12, 240, 48 * 4);
        int b = this.height / 4 + 48;
        RenderUtils.drawImage(new ResourceLocation("client/icon/logo.png"),
                GuiLoginMenu.this.width / 2f - 120 + 240 / 2 - 120, GuiLoginMenu.this.height / 4f + 16, 40, 40,
                Color.WHITE);

        drawCenteredString(mc.fontRendererObj, EnumChatFormatting.GRAY + "Login with Username and Password",
                this.width / 2, b + 72 + 12 - 4 - 50, -1);

        drawCenteredString(mc.fontRendererObj, message,
                this.width / 2, b + 72 + 12 - 4 - 100, -1);

        loginButton.yPosition = b + 72 +    12 + 24;
        regbutton.yPosition = b + 72 + 12 + 50;
        textField.xPosition = this.width / 2 - 100;
        textField.setPassword(true);
        textField.yPosition = b + 72 + 12 - 2;
        textField.drawTextBox();

        textField1.xPosition = this.width / 2 - 100;
        textField1.yPosition = b + 50;
        textField1.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);

    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        textField.mouseClicked(mouseX, mouseY, mouseButton);
        textField1.mouseClicked(mouseX, mouseY, mouseButton);
    }

}
