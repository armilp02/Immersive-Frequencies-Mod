package com.armilp.ifreq.client.curios.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

@OnlyIn(Dist.CLIENT)
public class WalkieCurioRenderer implements ICurioRenderer {

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(
            ItemStack stack,
            SlotContext slotContext,
            PoseStack matrixStack,
            RenderLayerParent<T, M> renderLayerParent,
            MultiBufferSource renderTypeBuffer,
            int light,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {

        if (!(renderLayerParent.getModel() instanceof HumanoidModel<?> bipedModel)) return;

        LivingEntity entity = slotContext.entity();

        BakedModel bakedModel = Minecraft.getInstance()
                .getItemRenderer()
                .getModel(stack, entity.level(), entity, 0);

        matrixStack.pushPose();

        ICurioRenderer.translateIfSneaking(matrixStack, entity);
        ICurioRenderer.rotateIfSneaking(matrixStack, entity);

        bipedModel.body.translateAndRotate(matrixStack);

        matrixStack.translate(-0.11, 0.62, -0.17);
        matrixStack.scale(0.40f, 0.40f, 0.40f);
        matrixStack.mulPose(Axis.XP.rotationDegrees(180));
        matrixStack.mulPose(Axis.YP.rotationDegrees(180));

        Minecraft.getInstance().getItemRenderer().render(
                stack,
                ItemDisplayContext.NONE,
                false,
                matrixStack,
                renderTypeBuffer,
                light,
                OverlayTexture.NO_OVERLAY,
                bakedModel
        );

        matrixStack.popPose();
    }
}