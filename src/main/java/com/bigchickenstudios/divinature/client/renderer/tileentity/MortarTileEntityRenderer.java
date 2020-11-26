package com.bigchickenstudios.divinature.client.renderer.tileentity;

import com.bigchickenstudios.divinature.Strings;
import com.bigchickenstudios.divinature.tileentity.MortarTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

public class MortarTileEntityRenderer extends TileEntityRenderer<MortarTileEntity> {

    public static final RenderMaterial TEXTURE = new RenderMaterial(PlayerContainer.LOCATION_BLOCKS_TEXTURE, Strings.createResourceLocation("entity/pestle"));

    private final ItemRenderer itemRenderer;

    private final ModelRenderer model;

    public MortarTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcher, ItemRenderer itemRendererIn) {
        super(rendererDispatcher);
        this.itemRenderer = itemRendererIn;
        this.model = new ModelRenderer(16, 16, 0, 0);
        this.model.addBox(-5.0f, -2.0f, -1.0f, 2.0f, 7.0f, 2.0f);
        this.model.setRotationPoint(8.0f, 9.0f, 8.0f);
        this.model.rotateAngleZ = (float)Math.PI * 3.0F / 4.0f;
    }

    @Override
    public void render(@Nonnull MortarTileEntity tileEntity, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        if (!tileEntity.isEmptying()) {
            NonNullList<ItemStack> list = tileEntity.getStacks();
            for (int i = 0; i < list.size(); i++) {
                ItemStack stack = list.get(i);
                if (!stack.isEmpty()) {
                    matrixStack.push();
                    matrixStack.translate(0.5D, 0.13671875D, 0.5D);
                    matrixStack.rotate(Vector3f.YP.rotationDegrees(90.0F * (i % 4)));
                    matrixStack.rotate(Vector3f.XP.rotationDegrees(90.0F));
                    if (i > 4) {
                        matrixStack.translate(-0.0625D, -0.0625D, -0.0859375D + i * 0.001D);
                    }
                    else if (i > 0) {
                        matrixStack.translate(0.0D, -0.0625D, -0.0625D + i * 0.001D);
                    }
                    matrixStack.scale(0.375F, 0.375F, 0.375F);
                    this.itemRenderer.renderItem(stack, ItemCameraTransforms.TransformType.FIXED, combinedLight, combinedOverlay, matrixStack, buffer);
                    matrixStack.pop();
                }
            }
        }

        float f = tileEntity.isGrinding() ? (float)Math.PI * Math.min(tileEntity.getGrindTime() + partialTicks, 20.0f) / 10.0f : 0.0f;
        matrixStack.translate(0.5D, 0.5D, 0.5D);
        matrixStack.rotate(Vector3f.YP.rotation(f));
        matrixStack.translate(-0.5D, -0.5D, -0.5D);

        IVertexBuilder iVertexBuilder = TEXTURE.getBuffer(buffer, RenderType::getEntitySolid);
        this.model.render(matrixStack, iVertexBuilder, combinedLight, combinedOverlay);
    }
}
