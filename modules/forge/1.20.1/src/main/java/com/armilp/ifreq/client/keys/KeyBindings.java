package com.armilp.ifreq.client.keys;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {

    public static KeyMapping OPEN_WALKIE_GUI;

    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        OPEN_WALKIE_GUI = new KeyMapping(
                "key.ifreq.open_walkie_gui",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "key.categories.ifreq"
        );
        event.register(OPEN_WALKIE_GUI);
    }
}