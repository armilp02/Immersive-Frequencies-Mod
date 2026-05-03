package com.armilp.ifreq.common.registry;

import com.armilp.ifreq.MainEZ;
import com.armilp.ifreq.common.items.ItemWalkieTalkie;
import com.armilp.ifreq.config.WalkieConfig;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MainEZ.MODID);

    static {
        WalkieConfig.preRegister("walkie_talkie", 1000.0);
    }

    public static final DeferredItem<ItemWalkieTalkie> WALKIE_TALKIE =
            ITEMS.register("walkie_talkie",
                    () -> new ItemWalkieTalkie(new Item.Properties().stacksTo(1), "walkie_talkie"));

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}