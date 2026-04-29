package com.armilp.ifreq.network;

import com.armilp.ifreq.common.WalkieHandler;
import com.armilp.ifreq.common.frequency.FrequencyManager;
import com.armilp.ifreq.common.items.ItemWalkieTalkie;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class WalkieFrequencyPacket {

    private final double frequency;

    public WalkieFrequencyPacket(double frequency) {
        this.frequency = frequency;
    }

    public static void encode(WalkieFrequencyPacket pkt, FriendlyByteBuf buf) {
        buf.writeDouble(pkt.frequency);
    }

    public static WalkieFrequencyPacket decode(FriendlyByteBuf buf) {
        return new WalkieFrequencyPacket(buf.readDouble());
    }

    public static void handle(WalkieFrequencyPacket pkt, CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player == null) return;
            double freq = FrequencyManager.roundToTenth(pkt.frequency);
            boolean updated = trySet(player.getMainHandItem(), freq)
                    | trySet(player.getOffhandItem(), freq);
            if (updated) {
                WalkieHandler.onFrequencyChanged(player, freq);
            }
        });
        ctx.setPacketHandled(true);
    }

    private static boolean trySet(ItemStack stack, double frequency) {
        if (stack.getItem() instanceof ItemWalkieTalkie) {
            ItemWalkieTalkie.setFrequency(stack, frequency);
            return true;
        }
        return false;
    }
}