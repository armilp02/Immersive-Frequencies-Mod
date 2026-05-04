package com.armilp.ifreq.common.items;

import com.armilp.ifreq.client.geo.renderer.WalkieGeoItemRenderer;
import com.armilp.ifreq.config.WalkieConfig;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

public class ItemWalkieTalkie extends Item implements GeoItem {

    private static final RawAnimation OFF_STATE_ANIM = RawAnimation.begin().thenPlayAndHold("off_walkie_state");
    private static final RawAnimation ON_STATE_ANIM = RawAnimation.begin().thenPlay("on_walkie_state");

    private static final String TAG_FREQUENCY = "Frequency";
    private static final String TAG_ON = "On";

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final String configKey;

    public ItemWalkieTalkie(Properties properties, String configKey) {
        super(properties);
        this.configKey = configKey;
    }

    public double getMaxDistance() {
        return WalkieConfig.getRange(configKey);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.ifreq.walkie_talkie.tooltip.frequency",
                String.format("%.1f", getFrequency(stack))));
        tooltip.add(Component.translatable("item.ifreq.walkie_talkie.tooltip.range",
                (int) getMaxDistance()));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide) {
            boolean currentlyOn = isOn(stack);
            boolean newState = !currentlyOn;
            setOn(stack, newState);

            player.displayClientMessage(
                    Component.translatable("message.ifreq.walkie_talkie.frequency",
                            String.format("%.1f", getFrequency(stack))), true
            );
        }
        return InteractionResultHolder.success(stack);
    }

    public static void setFrequency(ItemStack stack, double frequency) {
        stack.getOrCreateTag().putDouble(TAG_FREQUENCY, frequency);
    }

    public static double getFrequency(ItemStack stack) {
        if (!stack.hasTag()) return 0.0D;
        return stack.getTag().getDouble(TAG_FREQUENCY);
    }

    public static boolean isOn(ItemStack stack) {
        if (!stack.hasTag()) return false;
        return stack.getTag().getBoolean(TAG_ON);
    }

    public static void setOn(ItemStack stack, boolean on) {
        stack.getOrCreateTag().putBoolean(TAG_ON, on);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private WalkieGeoItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new WalkieGeoItemRenderer();
                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "StateController", 0, state -> {
            ItemStack stack = state.getData(DataTickets.ITEMSTACK);

            if (stack == null) return PlayState.STOP;

            if (isOn(stack)) {
                return state.setAndContinue(ON_STATE_ANIM);
            } else {
                return state.setAndContinue(OFF_STATE_ANIM);
            }
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}