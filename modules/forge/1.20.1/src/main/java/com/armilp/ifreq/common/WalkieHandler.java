package com.armilp.ifreq.common;

import com.armilp.ifreq.MainEZ;
import com.armilp.ifreq.Plugin;
import com.armilp.ifreq.common.frequency.FrequencyManager;
import com.armilp.ifreq.common.items.ItemWalkieTalkie;
import com.armilp.ifreq.compat.curios.CuriosCompat;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(modid = MainEZ.MODID)
public class WalkieHandler {

    private static final Set<UUID> activePlayers = ConcurrentHashMap.newKeySet();
    private static final Map<UUID, Double> lastFrequencies = new ConcurrentHashMap<>();

    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        EquipmentSlot slot = event.getSlot();
        if (slot != EquipmentSlot.MAINHAND && slot != EquipmentSlot.OFFHAND) return;
        evaluateWalkieState(player);
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        Plugin.unsubscribeFromAll(player);
        forceCleanup(player.getUUID());
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        evaluateWalkieState(player);
    }

    public static void evaluateWalkieState(ServerPlayer player) {
        UUID playerId = player.getUUID();
        ItemStack walkie = getActiveWalkie(player);

        if (walkie == null || !ItemWalkieTalkie.isOn(walkie)) {
            if (activePlayers.contains(playerId)) {
                Plugin.unsubscribeFromAll(player);
                cleanupPlayer(playerId);
            }
            return;
        }

        double frequency = FrequencyManager.roundToTenth(ItemWalkieTalkie.getFrequency(walkie));
        double maxDistance = ((ItemWalkieTalkie) walkie.getItem()).getMaxDistance();
        Plugin.subscribeToFrequency(player, frequency, maxDistance);
        lastFrequencies.put(playerId, frequency);
        activePlayers.add(playerId);
    }

    public static void onPowerChanged(ServerPlayer player, boolean on) {
        if (!on) {
            UUID playerId = player.getUUID();
            if (activePlayers.contains(playerId)) {
                Plugin.unsubscribeFromAll(player);
                cleanupPlayer(playerId);
            }
        } else {
            evaluateWalkieState(player);
        }
    }

    public static void onFrequencyChanged(ServerPlayer player, double newFrequency) {
        UUID playerId = player.getUUID();
        if (!activePlayers.contains(playerId)) return;

        ItemStack walkie = getActiveWalkie(player);
        if (walkie == null) return;

        double maxDistance = ((ItemWalkieTalkie) walkie.getItem()).getMaxDistance();
        Plugin.subscribeToFrequency(player, newFrequency, maxDistance);
        lastFrequencies.put(playerId, newFrequency);
    }

    public static ItemStack getActiveWalkie(ServerPlayer player) {
        ItemStack hand = getHeldWalkie(player);
        if (hand != null) return hand;
        return CuriosCompat.getEquippedWalkie(player).orElse(null);
    }

    public static ItemStack getHeldWalkie(ServerPlayer player) {
        ItemStack main = player.getMainHandItem();
        if (main.getItem() instanceof ItemWalkieTalkie) return main;
        ItemStack off = player.getOffhandItem();
        if (off.getItem() instanceof ItemWalkieTalkie) return off;
        return null;
    }

    private static void cleanupPlayer(UUID playerId) {
        activePlayers.remove(playerId);
        lastFrequencies.remove(playerId);
    }

    public static void forceCleanup(UUID playerId) {
        cleanupPlayer(playerId);
    }

    public static Optional<Double> getPlayerFrequency(UUID playerId) {
        return Optional.ofNullable(lastFrequencies.get(playerId));
    }
}