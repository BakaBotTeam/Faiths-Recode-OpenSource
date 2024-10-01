package dev.faiths.module.player;

import dev.faiths.Faiths;
import dev.faiths.event.Handler;
import dev.faiths.event.impl.LivingUpdateEvent;
import dev.faiths.event.impl.PacketEvent;
import dev.faiths.event.impl.UpdateEvent;
import dev.faiths.event.impl.WorldEvent;
import dev.faiths.module.Category;
import dev.faiths.module.CheatModule;
import dev.faiths.utils.DebugUtil;
import dev.faiths.utils.HYTUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0BPacketEntityAction;

import java.util.concurrent.CopyOnWriteArrayList;

import static dev.faiths.utils.IMinecraft.mc;

@SuppressWarnings("unused")
public class ModuleNotify extends CheatModule {
    private static CopyOnWriteArrayList<EntityPlayer> godAxePlayer = new CopyOnWriteArrayList<>();
    private static CopyOnWriteArrayList<EntityPlayer> kbBallPlayer = new CopyOnWriteArrayList<>();
    private static CopyOnWriteArrayList<EntityPlayer> enchantedGApplePlayer = new CopyOnWriteArrayList<>();
    public ModuleNotify() {
        super("Notify", Category.PLAYER);
    }

    private Handler<UpdateEvent> handler = event -> {
        mc.theWorld.playerEntities.forEach((entity) -> {
            if (!entity.equals(mc.thePlayer) && HYTUtils.isHoldingGodAxe(entity) && !godAxePlayer.contains(entity)) {
                godAxePlayer.add(entity);
                DebugUtil.log("Notify", "WARNING! " + entity.getName() + " is holding GodAxe!");
                mc.thePlayer.playSound("random.orb", 1, 16);
            }
            if (!entity.equals(mc.thePlayer) && HYTUtils.isKBBall(entity.getEquipmentInSlot(0)) && !kbBallPlayer.contains(entity)) {
                kbBallPlayer.add(entity);
                DebugUtil.log("Notify", "WARNING! " + entity.getName() + " is holding KBBall!");
                mc.thePlayer.playSound("random.orb", 1, 16);
            }
            if (!entity.equals(mc.thePlayer) && HYTUtils.isHoldingEnchantedGoldenApple(entity) && !enchantedGApplePlayer.contains(entity)) {
                enchantedGApplePlayer.add(entity);
                DebugUtil.log("Notify", "WARNING! " + entity.getName() + " is holding Enchanted GApple!");
                mc.thePlayer.playSound("random.orb", 1, 16);
            }
        });
    };

    private Handler<WorldEvent> worldEventHandler = event -> {
        godAxePlayer.clear();
        kbBallPlayer.clear();
        enchantedGApplePlayer.clear();
    };

    @Override
    public void onEnable() {
        godAxePlayer.clear();
        kbBallPlayer.clear();
        enchantedGApplePlayer.clear();
    }

    @Override
    public void onDisable() {
        godAxePlayer.clear();
        kbBallPlayer.clear();
        enchantedGApplePlayer.clear();
    }

    public static boolean isHeldGodAxe(EntityPlayer player) {
        if (!Faiths.moduleManager.getModule(ModuleNotify.class).getState()) return false;
        return godAxePlayer.contains(player);
    }

    public static boolean isHeldKBBall(EntityPlayer player) {
        if (!Faiths.moduleManager.getModule(ModuleNotify.class).getState()) return false;
        return kbBallPlayer.contains(player);
    }

    public static boolean isHeldEnchantedGApple(EntityPlayer player) {
        if (!Faiths.moduleManager.getModule(ModuleNotify.class).getState()) return false;
        return enchantedGApplePlayer.contains(player);
    }
}
