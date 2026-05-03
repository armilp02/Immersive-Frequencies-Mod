package com.armilp.ifreq.network;

import com.armilp.ifreq.MainEZ;
import com.armilp.ifreq.common.WalkieHandler;
import com.armilp.ifreq.common.frequency.FrequencyManager;
import com.armilp.ifreq.common.items.ItemWalkieTalkie;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record WalkieFrequencyPacket(double frequency) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<WalkieFrequencyPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MainEZ.MODID, "walkie_frequency"));

    public static final StreamCodec<FriendlyByteBuf, WalkieFrequencyPacket> STREAM_CODEC = StreamCodec.of(
            (buf, pkt) -> buf.writeDouble(pkt.frequency()),
            buf -> new WalkieFrequencyPacket(buf.readDouble())
    );

    public WalkieFrequencyPacket(double frequency) {
        this.frequency = FrequencyManager.roundToTenth(frequency);
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(WalkieFrequencyPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            ItemStack main = player.getMainHandItem();
            ItemStack off = player.getOffhandItem();
            boolean updated = updateFrequency(main, payload.frequency()) || updateFrequency(off, payload.frequency());
            if (updated) {
                WalkieHandler.onFrequencyChanged(player, payload.frequency());
            }
        });
    }

    private static boolean updateFrequency(ItemStack stack, double frequency) {
        if (stack.getItem() instanceof ItemWalkieTalkie) {
            ItemWalkieTalkie.setFrequency(stack, frequency);
            return true;
        }
        return false;
    }
}