package com.bigchickenstudios.divinature.client.renderer.overlay;

import com.bigchickenstudios.divinature.Strings;
import com.bigchickenstudios.divinature.world.level.block.entity.MortarTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class MortarOverlayRenderer extends OverlayRenderer<MortarTileEntity> {

    private static final ResourceLocation TEXTURE = Strings.createResourceLocation("textures/gui/container/mortar.png");

    @Override
    protected void render(MortarTileEntity mortarTileEntity, MatrixStack matrixStack, Minecraft mc, float partialTick) {
        NonNullList<ItemStack> stacks = mortarTileEntity.getStacks();
        if (!stacks.isEmpty()) {
            int x = mc.getMainWindow().getScaledWidth() / 2 - 64;
            int y = mc.getMainWindow().getScaledHeight() / 2 - 64;


            mc.getTextureManager().bindTexture(TEXTURE);
            AbstractGui.blit(matrixStack, x, y, 0, 0, 54, 54, 64, 64);
            AbstractGui.blit(matrixStack, x + 47, y + 47, 0, 55, 9, 9, 64, 64);
            for (int i = 0; i < stacks.size(); i++) {
                ItemStack stack = stacks.get(i);
                int x1 = x + (i % 3) * 16 + 3;
                int y1 = y + (i / 3) * 16 + 3;
                this.renderItem(mc, stack, x1, y1);
            }
        }
    }
}
