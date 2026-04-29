package com.armilp.ifreq.network;

import com.armilp.ifreq.common.menu.WalkieTalkieMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class OpenWalkieGuiPacket {

    public OpenWalkieGuiPacket() {
    }

    public static void encode(OpenWalkieGuiPacket pkt, FriendlyByteBuf buf) {
    }

    public static OpenWalkieGuiPacket decode(FriendlyByteBuf buf) {
        return new OpenWalkieGuiPacket();
    }

    public static void handle(OpenWalkieGuiPacket pkt, CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player == null) return;
            player.openMenu(new SimpleMenuProvider(
                    (id, inv, p) -> new WalkieTalkieMenu(id, inv, p.getMainHandItem()),
                    Component.literal("Walkie Talkie")
            ));
        });
        ctx.setPacketHandled(true);
    }
}