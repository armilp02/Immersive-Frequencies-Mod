package com.armilp.ifreq.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModPackets {

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(OpenWalkieGuiPacket.TYPE, OpenWalkieGuiPacket.STREAM_CODEC, OpenWalkieGuiPacket::handle);
        registrar.playToServer(WalkieFrequencyPacket.TYPE, WalkieFrequencyPacket.STREAM_CODEC, WalkieFrequencyPacket::handle);
        registrar.playToServer(WalkiePowerPacket.TYPE, WalkiePowerPacket.STREAM_CODEC, WalkiePowerPacket::handle);
    }
}