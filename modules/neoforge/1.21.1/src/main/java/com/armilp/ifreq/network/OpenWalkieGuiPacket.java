package com.armilp.ifreq.network;

import com.armilp.ifreq.MainEZ;
import com.armilp.ifreq.common.menu.WalkieTalkieMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record OpenWalkieGuiPacket() implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<OpenWalkieGuiPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MainEZ.MODID, "open_walkie_gui"));

    public static final StreamCodec<FriendlyByteBuf, OpenWalkieGuiPacket> STREAM_CODEC =
            StreamCodec.unit(new OpenWalkieGuiPacket());

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(OpenWalkieGuiPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            player.openMenu(new SimpleMenuProvider(
                    (id, inv, p) -> new WalkieTalkieMenu(id, inv, p.getMainHandItem()),
                    Component.literal("Walkie Talkie")
            ));
        });
    }
}