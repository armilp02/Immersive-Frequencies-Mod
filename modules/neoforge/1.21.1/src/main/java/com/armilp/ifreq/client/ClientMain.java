package com.armilp.ifreq.client;

import com.armilp.ifreq.MainEZ;
import com.armilp.ifreq.client.curios.renderer.WalkieCurioRenderer;
import com.armilp.ifreq.client.keys.KeyBindings;
import com.armilp.ifreq.client.screen.WalkieTalkieScreen;
import com.armilp.ifreq.common.items.ItemWalkieTalkie;
import com.armilp.ifreq.common.registry.ModItems;
import com.armilp.ifreq.common.registry.ModMenus;
import com.armilp.ifreq.compat.curios.CuriosCompat;
import com.armilp.ifreq.network.OpenWalkieGuiPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

public class ClientMain {

    @EventBusSubscriber(modid = MainEZ.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            CuriosRendererRegistry.register(ModItems.WALKIE_TALKIE.get(), WalkieCurioRenderer::new);
        }

        @SubscribeEvent
        public static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
            event.register(ModMenus.WALKIE_TALKIE_MENU.get(), WalkieTalkieScreen::new);
        }

        @SubscribeEvent
        public static void onRegisterKeys(RegisterKeyMappingsEvent event) {
            KeyBindings.onRegisterKeyMappings(event);
        }
    }

    @EventBusSubscriber(modid = MainEZ.MODID, value = Dist.CLIENT)
    public static class ForgeEvents {

        @SubscribeEvent
        public static void onKeyPress(InputEvent.Key event) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null || mc.screen != null) return;

            if (KeyBindings.OPEN_WALKIE_GUI != null && KeyBindings.OPEN_WALKIE_GUI.consumeClick()) {
                if (isHoldingOrWearingWalkie(mc)) {
                    PacketDistributor.sendToServer(new OpenWalkieGuiPacket());
                }
            }
        }

        private static boolean isHoldingOrWearingWalkie(Minecraft mc) {
            ItemStack main = mc.player.getMainHandItem();
            if (main.getItem() instanceof ItemWalkieTalkie) return true;
            ItemStack off = mc.player.getOffhandItem();
            if (off.getItem() instanceof ItemWalkieTalkie) return true;
            return CuriosCompat.hasWalkieEquipped(mc.player);
        }
    }
}