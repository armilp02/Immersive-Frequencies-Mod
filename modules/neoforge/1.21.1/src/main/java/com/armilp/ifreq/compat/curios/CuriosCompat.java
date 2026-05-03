package com.armilp.ifreq.compat.curios;

import com.armilp.ifreq.common.items.ItemWalkieTalkie;
import com.armilp.ifreq.common.registry.ModItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.SlotTypePreset;

import java.util.Optional;

public class CuriosCompat {

    public static boolean isLoaded() {
        return ModList.get().isLoaded("curios");
    }

    public static void setup() {
        InterModComms.sendTo(
                "curios",
                "register_type",
                () -> SlotTypePreset.BELT.getMessageBuilder().build()
        );
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(
                CuriosCapability.ITEM,
                (stack, context) -> () -> stack,
                ModItems.WALKIE_TALKIE.get()
        );
    }

    public static boolean hasWalkieEquipped(Player player) {
        if (!isLoaded()) return false;
        return CuriosApi.getCuriosInventory(player)
                .map(inv -> inv.findFirstCurio(s -> s.getItem() instanceof ItemWalkieTalkie).isPresent())
                .orElse(false);
    }

    public static Optional<ItemStack> getEquippedWalkie(Player player) {
        if (!isLoaded()) return Optional.empty();
        return CuriosApi.getCuriosInventory(player)
                .flatMap(inv -> inv.findFirstCurio(s -> s.getItem() instanceof ItemWalkieTalkie))
                .map(SlotResult::stack);
    }
}