package com.armilp.ifreq.common.registry;

import com.armilp.ifreq.MainEZ;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTab {

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MainEZ.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> IFREQ_TAB =
            TABS.register("ifreq_tab", () ->
                    CreativeModeTab.builder()
                            .icon(() -> new ItemStack(ModItems.WALKIE_TALKIE.get()))
                            .title(Component.translatable("itemGroup.ifreq"))
                            .displayItems((parameters, output) ->
                                    output.accept(ModItems.WALKIE_TALKIE.get()))
                            .build()
            );

    public static void register(IEventBus bus) {
        TABS.register(bus);
    }
}