package com.armilp.ifreq.client.geo.model;

import com.armilp.ifreq.common.items.ItemWalkieTalkie;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

import static com.armilp.ifreq.MainEZ.MODID;

public class WalkieTalkieItemModel extends GeoModel<ItemWalkieTalkie> {

    private final ResourceLocation model      = new ResourceLocation(MODID, "geo/walkie_talkie.geo.json");
    private final ResourceLocation animations = new ResourceLocation(MODID, "animations/walkie_talkie.geo.animation.json");
    private final ResourceLocation texture    = new ResourceLocation(MODID, "textures/item/walkie_talkie_texture_model.png");

    @Override
    public ResourceLocation getModelResource(ItemWalkieTalkie animatable) {
        return model;
    }

    @Override
    public ResourceLocation getTextureResource(ItemWalkieTalkie animatable) {
        return texture;
    }

    @Override
    public ResourceLocation getAnimationResource(ItemWalkieTalkie animatable) {
        return animations;
    }
}