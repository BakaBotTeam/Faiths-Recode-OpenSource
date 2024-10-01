package dev.faiths;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import dev.faiths.command.CommandManager;
import dev.faiths.config.ConfigManager;
import dev.faiths.event.EventManager;
import dev.faiths.hackerdetector.HackerDetector;
import dev.faiths.module.ModuleManager;
import dev.faiths.ui.font.FontManager;
import dev.faiths.ui.notifiction.NotificationManager;
import dev.faiths.utils.SlotSpoofManager;
import dev.faiths.utils.player.RotationManager;
import dev.faiths.utils.tasks.TaskManager;
import net.minecraft.util.ResourceLocation;
import net.vialoadingbase.ViaLoadingBase;
import net.viamcp.ViaMCP;
import org.apache.commons.lang3.RandomUtils;
import tech.skidonion.obfuscator.annotations.NativeObfuscation;

import java.net.InetSocketAddress;

public class Faiths {
    public static Faiths INSTANCE;
    public static final String NAME = "Faiths";
    public static String VERSION = "240828";
    public static boolean IS_BETA = false;
    public static final ResourceLocation cape = new ResourceLocation("client/cape.png");
    @NativeObfuscation.Inline
    public static boolean verified = false; // this is a temporary boolean
    private final EventManager eventManager;
    @NativeObfuscation.Inline
    public static HackerDetector hackerDetector;
    private final RotationManager rotationManager;
    public final SlotSpoofManager slotSpoofManager;
    @NativeObfuscation.Inline
    public static ModuleManager moduleManager;
    @NativeObfuscation.Inline
    public static ConfigManager configManager;
    @NativeObfuscation.Inline
    public static CommandManager commandManager;
    @NativeObfuscation.Inline
    public static NotificationManager notificationManager;
    public TaskManager taskManager;
    public static boolean isInitializing = true;
    public static int delta;
    public static long lastFrame;
    public final int astolfo;

    public Faiths() {
        INSTANCE = this;
       // Wrapper._debug_addDefaultCloudConstant("Beta", "1857748011");
      //  Wrapper._debug_addDefaultCloudConstant("Stable", "-1521957196");
        this.eventManager = new EventManager();
        commandManager = new CommandManager();
        commandManager.registerCommands();
        hackerDetector = new HackerDetector();
        FontManager.init();
        rotationManager = new RotationManager();
        notificationManager = new NotificationManager();
        slotSpoofManager = new SlotSpoofManager();
        taskManager = new TaskManager();
        astolfo = RandomUtils.nextInt(0, 4);
        VERSION += " (" + GitVersion.VERSION + ")";
    }

    public static void onLoaded() {
        try {
            ViaMCP.create();
            ViaMCP.INSTANCE.initAsyncSlider();
            ViaLoadingBase.getInstance().reload(ProtocolVersion.v1_12_2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RotationManager getRotationManager() {
        return rotationManager;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public boolean isInitializing() {
        return isInitializing;
    }

    public static boolean getIsBeta() { return true; }

    public static boolean isDev() {
        try {
            Class.forName("dev.faiths.Fai" + new StringBuilder("ht").reverse() + "s").getClass();
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
