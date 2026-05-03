package com.armilp.ifreq.network;

import com.armilp.ifreq.MainEZ;
import com.armilp.ifreq.common.WalkieHandler;
import com.armilp.ifreq.common.items.ItemWalkieTalkie;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record WalkiePowerPacket(boolean on) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<WalkiePowerPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MainEZ.MODID, "walkie_power"));

    public static final StreamCodec<FriendlyByteBuf, WalkiePowerPacket> STREAM_CODEC = StreamCodec.of(
            (buf, pkt) -> buf.writeBoolean(pkt.on()),
            buf -> new WalkiePowerPacket(buf.readBoolean())
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(WalkiePowerPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            ItemStack walkie = WalkieHandler.getHeldWalkie(player);
            if (walkie == null) return;
            ItemWalkieTalkie.setOn(walkie, payload.on());
            WalkieHandler.onPowerChanged(player, payload.on());
        });
    }
}