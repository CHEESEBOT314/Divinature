package com.bigchickenstudios.divinature.client.renderer.tileentity;

import com.bigchickenstudios.divinature.tileentity.InfuserTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class InfuserTileEntityRenderer extends TileEntityRenderer<InfuserTileEntity> {

    private final ItemRenderer itemRenderer;

    public InfuserTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcher, ItemRenderer itemRendererIn) {
        super(rendererDispatcher);
        this.itemRenderer = itemRendererIn;
    }

    @Override
    public void render(@Nonnull InfuserTileEntity infuserTileEntity, float partialTick, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        ItemStack stack = infuserTileEntity.getTopItemStack();
        if (!stack.isEmpty()) {
            matrixStack.push();
            IBakedModel model = this.itemRenderer.getItemModelWithOverrides(stack, infuserTileEntity.getWorld(), null);
            float f = model.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.getY() * 0.25f;
            float bob = 0.05f * (float)Math.sin((infuserTileEntity.getWorld().getGameTime() + partialTick) / 20.0f) + 0.05f;
            float rot = (infuserTileEntity.getWorld().getGameTime() + partialTick) / 40.0f;
            matrixStack.translate(0.5D, 1.375D + f + bob, 0.5D);
            matrixStack.rotate(Vector3f.YP.rotation(rot));
            this.itemRenderer.renderItem(stack, ItemCameraTransforms.TransformType.GROUND, combinedLight, combinedOverlay, matrixStack, buffer);
            matrixStack.pop();
        }
    }
}
