package com.armilp.ifreq.common.items;

import com.armilp.ifreq.config.WalkieConfig;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemWalkieTalkie extends Item {

    private static final String TAG_FREQUENCY = "Frequency";
    private static final String TAG_ON = "On";
    private final String configKey;

    public ItemWalkieTalkie(Properties properties, String configKey) {
        super(properties);
        this.configKey = configKey;
    }

    public double getMaxDistance() {
        return WalkieConfig.getRange(configKey);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.ifreq.walkie_talkie.tooltip.frequency",
                String.format("%.1f", getFrequency(stack))));
        tooltip.add(Component.translatable("item.ifreq.walkie_talkie.tooltip.range",
                (int) getMaxDistance()));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide) {
            player.displayClientMessage(
                    Component.translatable("message.ifreq.walkie_talkie.frequency",
                            String.format("%.1f", getFrequency(stack))), true
            );
        }
        return InteractionResultHolder.success(stack);
    }

    public static void setFrequency(ItemStack stack, double frequency) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putDouble(TAG_FREQUENCY, frequency));
    }

    public static double getFrequency(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null || !customData.contains(TAG_FREQUENCY)) return 0.0D;
        return customData.getUnsafe().getDouble(TAG_FREQUENCY);
    }

    public static boolean isOn(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null || !customData.contains(TAG_ON)) return false;
        return customData.getUnsafe().getBoolean(TAG_ON);
    }

    public static void setOn(ItemStack stack, boolean on) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putBoolean(TAG_ON, on));
    }
}