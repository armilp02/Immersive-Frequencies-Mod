package com.armilp.ifreq.client;

import com.armilp.ifreq.MainEZ;
import com.armilp.ifreq.client.keys.KeyBindings;
import com.armilp.ifreq.client.screen.WalkieTalkieScreen;
import com.armilp.ifreq.common.items.ItemWalkieTalkie;
import com.armilp.ifreq.common.registry.ModMenus;
import com.armilp.ifreq.compat.curios.CuriosCompat;
import com.armilp.ifreq.network.ModPackets;
import com.armilp.ifreq.network.OpenWalkieGuiPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientMain {

    @Mod.EventBusSubscriber(modid = MainEZ.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(ModMenus.WALKIE_TALKIE_MENU.get(), WalkieTalkieScreen::new);
        }

        @SubscribeEvent
        public static void onRegisterKeys(RegisterKeyMappingsEvent event) {
            KeyBindings.onRegisterKeyMappings(event);
        }
    }

    @Mod.EventBusSubscriber(modid = MainEZ.MODID, value = Dist.CLIENT)
    public static class ForgeEvents {

        @SubscribeEvent
        public static void onKeyPress(InputEvent.Key event) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null || mc.screen != null) return;

            if (KeyBindings.OPEN_WALKIE_GUI != null && KeyBindings.OPEN_WALKIE_GUI.consumeClick()) {
                if (isHoldingOrWearingWalkie(mc)) {
                    ModPackets.CHANNEL.sendToServer(new OpenWalkieGuiPacket());
                }
            }
        }

        private static boolean isHoldingOrWearingWalkie(Minecraft mc) {
            assert mc.player != null;
            ItemStack main = mc.player.getMainHandItem();
            if (main.getItem() instanceof ItemWalkieTalkie) return true;
            ItemStack off = mc.player.getOffhandItem();
            if (off.getItem() instanceof ItemWalkieTalkie) return true;
            return CuriosCompat.hasWalkieEquipped(mc.player);
        }
    }
}