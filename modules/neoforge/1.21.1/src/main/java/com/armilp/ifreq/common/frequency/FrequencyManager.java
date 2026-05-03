package com.armilp.ifreq.common.frequency;

import com.armilp.ifreq.MainEZ;
import com.armilp.ifreq.Plugin;
import com.armilp.ifreq.common.WalkieHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = MainEZ.MODID)
public class FrequencyManager {

    public static double roundToTenth(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    public static void cleanupFrequencyData(Level level, double frequency) {
        if (level.isClientSide()) return;
        try {
            FrequencySavedData savedData = FrequencySavedData.get(level);
            savedData.getGroupName(frequency).ifPresent(savedData::removeMapping);
        } catch (Exception e) {
            MainEZ.LOGGER.error("[iFreq] Error cleaning frequency data {}: {}", frequency, e.getMessage());
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        double freq = WalkieHandler.getPlayerFrequency(player.getUUID()).orElse(-1.0);

        Plugin.unsubscribeFromAll(player);
        WalkieHandler.forceCleanup(player.getUUID());

        if (freq > 0 && Plugin.getSubscribedCount(freq) == 0) {
            cleanupFrequencyData(player.level(), freq);
        }
    }
}