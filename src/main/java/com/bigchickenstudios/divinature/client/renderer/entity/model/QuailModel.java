package com.bigchickenstudios.divinature.client.renderer.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class QuailModel extends EntityModel<Entity> {
	private final ModelRenderer body;
	private final ModelRenderer legR;
	private final ModelRenderer legL;
	private final ModelRenderer footR;
	private final ModelRenderer footL;
	private final ModelRenderer wingR;
	private final ModelRenderer wingL;

	public QuailModel() {
		textureWidth = 32;
		textureHeight = 16;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 24.0F, 0.0F);
		body.setTextureOffset(0, 8).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);
		body.setTextureOffset(16, 12).addBox(-2.0F, -5.5F, 2.0F, 4.0F, 3.0F, 1.0F, 0.0F, false);
		body.setTextureOffset(0, 5).addBox(-1.5F, -6.0F, -3.0F, 3.0F, 2.0F, 1.0F, 0.0F, false);
		body.setTextureOffset(15, 4).addBox(0.0F, -8.0F, -3.5F, 0.0F, 2.0F, 2.0F, 0.0F, false);
		body.setTextureOffset(8, 6).addBox(-1.0F, -5.5F, -4.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);

		legR = new ModelRenderer(this);
		legR.setRotationPoint(0.0F, 24.0F, 0.0F);
		legR.setTextureOffset(9, 6).addBox(-1.25F, -2.0F, 0.5F, 1.0F, 2.0F, 0.0F, 0.0F, false);

		legL = new ModelRenderer(this);
		legL.setRotationPoint(0.0F, 24.0F, 0.0F);
		legL.setTextureOffset(9, 6).addBox(0.25F, -2.0F, 0.5F, 1.0F, 2.0F, 0.0F, 0.0F, false);

		footR = new ModelRenderer(this);
		footR.setRotationPoint(0.0F, 24.0F, 0.0F);
		footR.setTextureOffset(8, 7).addBox(-1.25F, 0.0F, -0.5F, 1.0F, 0.0F, 1.0F, 0.0F, false);

		footL = new ModelRenderer(this);
		footL.setRotationPoint(0.0F, 24.0F, 0.0F);
		footL.setTextureOffset(8, 7).addBox(0.25F, 0.0F, -0.5F, 1.0F, 0.0F, 1.0F, 0.0F, false);

		wingR = new ModelRenderer(this);
		wingR.setRotationPoint(3.0F, 24.0F, 2.0F);
		wingR.setTextureOffset(19, 3).addBox(-6.0F, -6.0F, -3.0F, 1.0F, 2.0F, 3.0F, 0.0F, false);

		wingL = new ModelRenderer(this);
		wingL.setRotationPoint(3.0F, 24.0F, 2.0F);
		wingL.setTextureOffset(19, 3).addBox(-1.0F, -6.0F, -3.0F, 1.0F, 2.0F, 3.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		body.render(matrixStack, buffer, packedLight, packedOverlay);
		legR.render(matrixStack, buffer, packedLight, packedOverlay);
		legL.render(matrixStack, buffer, packedLight, packedOverlay);
		footR.render(matrixStack, buffer, packedLight, packedOverlay);
		footL.render(matrixStack, buffer, packedLight, packedOverlay);
		wingR.render(matrixStack, buffer, packedLight, packedOverlay);
		wingL.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}