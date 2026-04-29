package com.armilp.ifreq.network;

import com.armilp.ifreq.common.WalkieHandler;
import com.armilp.ifreq.common.items.ItemWalkieTalkie;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class WalkiePowerPacket {

    private final boolean on;

    public WalkiePowerPacket(boolean on) {
        this.on = on;
    }

    public static void encode(WalkiePowerPacket pkt, FriendlyByteBuf buf) {
        buf.writeBoolean(pkt.on);
    }

    public static WalkiePowerPacket decode(FriendlyByteBuf buf) {
        return new WalkiePowerPacket(buf.readBoolean());
    }

    public static void handle(WalkiePowerPacket pkt, CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player == null) return;
            ItemStack walkie = WalkieHandler.getHeldWalkie(player);
            if (walkie == null) return;
            ItemWalkieTalkie.setOn(walkie, pkt.on);
            WalkieHandler.onPowerChanged(player, pkt.on);
        });
        ctx.setPacketHandled(true);
    }
}