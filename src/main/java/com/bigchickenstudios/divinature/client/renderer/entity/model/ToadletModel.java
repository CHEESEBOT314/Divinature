package com.bigchickenstudios.divinature.client.renderer.entity.model;

public class ToadletModel extends EntityModel<Entity> {
	private final ModelRenderer body;
	private final ModelRenderer limbs;

	public ToadletModel() {
		textureWidth = 16;
		textureHeight = 16;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 23.0F, -1.0F);
		setRotationAngle(body, 0.4363F, 0.0F, 0.0F);
		body.setTextureOffset(0, 0).addBox(-1.5F, -1.5F, -1.0F, 3.0F, 2.0F, 4.0F, 0.0F, false);

		limbs = new ModelRenderer(this);
		limbs.setRotationPoint(-2.0F, 24.0F, -2.0F);
		limbs.setTextureOffset(4, 6).addBox(-0.25F, -1.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		limbs.setTextureOffset(4, 6).addBox(3.25F, -1.0F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		limbs.setTextureOffset(0, 9).addBox(0.25F, -4.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		limbs.setTextureOffset(0, 9).addBox(2.75F, -4.0F, 3.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		limbs.setTextureOffset(0, 6).addBox(0.25F, -2.0F, 2.25F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		limbs.setTextureOffset(0, 6).addBox(2.75F, -2.0F, 2.25F, 1.0F, 2.0F, 1.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		body.render(matrixStack, buffer, packedLight, packedOverlay);
		limbs.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}