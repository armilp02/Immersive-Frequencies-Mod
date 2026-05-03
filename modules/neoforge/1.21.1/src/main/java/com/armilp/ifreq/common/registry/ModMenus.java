package com.armilp.ifreq.common.registry;

import com.armilp.ifreq.MainEZ;
import com.armilp.ifreq.common.menu.WalkieTalkieMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenus {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, MainEZ.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<WalkieTalkieMenu>> WALKIE_TALKIE_MENU =
            MENUS.register("walkie_talkie_menu",
                    () -> IMenuTypeExtension.create(WalkieTalkieMenu::new)
            );
}