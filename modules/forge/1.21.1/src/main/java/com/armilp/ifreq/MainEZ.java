package com.armilp.ifreq;

import com.armilp.ifreq.client.keys.KeyBindings;
import com.armilp.ifreq.client.screen.WalkieTalkieScreen;
import com.armilp.ifreq.common.registry.ModCreativeTab;
import com.armilp.ifreq.common.registry.ModItems;
import com.armilp.ifreq.common.registry.ModMenus;
import com.armilp.ifreq.common.registry.ModSounds;
import com.armilp.ifreq.network.ModPackets;
import com.armilp.ifreq.network.OpenWalkieGuiPacket;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.PacketDistributor;
import org.slf4j.Logger;

@Mod(MainEZ.MODID)
public class MainEZ {

    public static final String MODID = "ifreq";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MainEZ() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(modEventBus);
        ModSounds.register(modEventBus);
        ModCreativeTab.register(modEventBus);
        ModMenus.MENUS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ModPackets.register();
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    // Client
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(ModMenus.WALKIE_TALKIE_MENU.get(), WalkieTalkieScreen::new);
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void onKeyPress(InputEvent.Key event) {
            if (KeyBindings.OPEN_WALKIE_GUI == null) return;

            if (KeyBindings.OPEN_WALKIE_GUI.consumeClick()) {
                Minecraft mc = Minecraft.getInstance();
                if (mc.player != null &&
                        mc.player.getMainHandItem().getItem() == ModItems.WALKIE_TALKIE.get()) {
                    ModPackets.INSTANCE.send(new OpenWalkieGuiPacket(), PacketDistributor.SERVER.noArg());
                }
            }
        }
    }
}