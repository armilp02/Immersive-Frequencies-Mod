package com.armilp.ifreq;

import com.armilp.ifreq.common.registry.ModCreativeTab;
import com.armilp.ifreq.common.registry.ModItems;
import com.armilp.ifreq.common.registry.ModMenus;
import com.armilp.ifreq.common.registry.ModSounds;
import com.armilp.ifreq.compat.curios.CuriosCompat;
import com.armilp.ifreq.config.WalkieConfig;
import com.armilp.ifreq.network.ModPackets;
import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(MainEZ.MODID)
public class MainEZ {

    public static final String MODID = "ifreq";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MainEZ(IEventBus modEventBus) {
        ModItems.register(modEventBus);
        ModSounds.register(modEventBus);
        ModCreativeTab.register(modEventBus);
        ModMenus.MENUS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(ModPackets::register);
        if (CuriosCompat.isLoaded()) {
            modEventBus.addListener(CuriosCompat::registerCapabilities);
        }
        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        WalkieConfig.load();
        if (CuriosCompat.isLoaded()) {
            CuriosCompat.setup();
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }
}