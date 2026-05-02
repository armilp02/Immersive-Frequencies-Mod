package com.armilp.ifreq.compat.curios;

import com.armilp.ifreq.common.items.ItemWalkieTalkie;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.SlotTypePreset;

import java.util.Optional;

public class CuriosCompat {

    public static final String SLOT_BELT = "belt";

    public static boolean isLoaded() {
        return ModList.get().isLoaded("curios");
    }

    public static void setup() {
        InterModComms.sendTo(
                "curios",
                "register_type",
                () -> SlotTypePreset.BELT.getMessageBuilder().build()
        );

        MinecraftForge.EVENT_BUS.addGenericListener(
                ItemStack.class,
                CuriosCompat::attachCurioCapability
        );
    }

    private static void attachCurioCapability(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();
        if (!(stack.getItem() instanceof ItemWalkieTalkie)) return;

        event.addCapability(
                new ResourceLocation("ifreq", "walkie_talkie_curio"),
                new ICapabilityProvider() {
                    @Override
                    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
                        return CuriosCapability.ITEM.orEmpty(
                                cap,
                                LazyOptional.of(() -> () -> stack)
                        );
                    }
                }
        );
    }

    public static boolean hasWalkieEquipped(Player player) {
        if (!isLoaded()) return false;
        return CuriosApi.getCuriosInventory(player)
                .resolve()
                .map(inv -> inv.findFirstCurio(s -> s.getItem() instanceof ItemWalkieTalkie).isPresent())
                .orElse(false);
    }

    public static Optional<ItemStack> getEquippedWalkie(Player player) {
        if (!isLoaded()) return Optional.empty();
        return CuriosApi.getCuriosInventory(player)
                .resolve()
                .flatMap(inv -> inv.findFirstCurio(s -> s.getItem() instanceof ItemWalkieTalkie))
                .map(SlotResult::stack);
    }
}