// Made with Blockbench 3.5.2
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


public class custom_model extends EntityModel<Entity> {
	private final ModelRenderer hood;

	public custom_model() {
		textureWidth = 64;
		textureHeight = 64;

		hood = new ModelRenderer(this);
		hood.setRotationPoint(0.0F, 24.0F, 0.0F);
		hood.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
		hood.setTextureOffset(32, 0).addBox(-4.0F, -7.5F, 4.0F, 8.0F, 7.0F, 1.0F, 0.0F, false);
		hood.setTextureOffset(32, 10).addBox(-3.0F, -6.5F, 5.0F, 6.0F, 6.0F, 1.0F, 0.0F, false);
		hood.setTextureOffset(0, 17).addBox(-2.0F, -5.0F, 6.0F, 4.0F, 4.0F, 1.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		hood.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}