package com.armilp.ifreq.network;

import com.armilp.ifreq.MainEZ;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.SimpleChannel;

public class ModPackets {

    public static final int PROTOCOL_VERSION = 1;

    public static final SimpleChannel INSTANCE = ChannelBuilder
            .named(ResourceLocation.fromNamespaceAndPath(MainEZ.MODID, "main"))
            .networkProtocolVersion(PROTOCOL_VERSION)
            .clientAcceptedVersions((status, version) ->
                    status == Channel.VersionTest.Status.VANILLA ||
                            (status == Channel.VersionTest.Status.PRESENT && version == PROTOCOL_VERSION))
            .serverAcceptedVersions((status, version) ->
                    status == Channel.VersionTest.Status.VANILLA ||
                            (status == Channel.VersionTest.Status.PRESENT && version == PROTOCOL_VERSION))
            .simpleChannel();

    private static int packetId = 0;

    public static int nextID() {
        return packetId++;
    }

    public static void register() {
        INSTANCE.messageBuilder(OpenWalkieGuiPacket.class, nextID())
                .encoder(OpenWalkieGuiPacket::encode)
                .decoder(OpenWalkieGuiPacket::decode)
                .consumerMainThread(OpenWalkieGuiPacket::handle)
                .add();

        INSTANCE.messageBuilder(WalkieFrequencyPacket.class, nextID())
                .encoder(WalkieFrequencyPacket::encode)
                .decoder(WalkieFrequencyPacket::decode)
                .consumerMainThread(WalkieFrequencyPacket::handle)
                .add();

        INSTANCE.messageBuilder(WalkiePowerPacket.class, nextID())
                .encoder(WalkiePowerPacket::encode)
                .decoder(WalkiePowerPacket::decode)
                .consumerMainThread(WalkiePowerPacket::handle)
                .add();

        INSTANCE.build();
    }
}