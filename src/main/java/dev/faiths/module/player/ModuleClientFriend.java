package dev.faiths.module.player;

import dev.faiths.event.Handler;
import dev.faiths.event.impl.PacketEvent;
import dev.faiths.module.Category;
import dev.faiths.module.CheatModule;
import dev.faiths.module.world.ModuleIRC;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;

import static dev.faiths.utils.IMinecraft.mc;

@SuppressWarnings("unused")
public class ModuleClientFriend extends CheatModule {
    public ModuleClientFriend() {
        super("ClientFriend", Category.PLAYER);
        setState(true);
    }

    @SuppressWarnings("unused")
    private Handler<PacketEvent> packetEventHandler = event -> {
        if (event.getType() == PacketEvent.Type.SEND) {
            Packet packet = event.getPacket();
            if (packet instanceof C02PacketUseEntity && ((C02PacketUseEntity) packet).getAction() == C02PacketUseEntity.Action.ATTACK && mc.theWorld != null) {
                Entity attackedEntity = ((C02PacketUseEntity) packet).getEntityFromWorld(mc.theWorld);
                if (attackedEntity instanceof EntityPlayer && ModuleIRC.isFriend(attackedEntity)) {
                    event.setCancelled(true);   
                }
            }
        }
    };
}
