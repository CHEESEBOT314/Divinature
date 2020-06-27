package com.bigchickenstudios.divinature.client.renderer.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

import javax.annotation.Nonnull;

public class ToadletModel extends SegmentedModel<Entity> {

	private final ImmutableList<ModelRenderer> parts;

	private final ModelRenderer body;
	private final ModelRenderer legsF;
	private final ModelRenderer legsB;
	private final ModelRenderer eyes;

	public ToadletModel() {
		this.textureWidth = 16;
		this.textureHeight = 16;

		this.body = new ModelRenderer(this, 0, 0);
		this.body.setRotationPoint(0.0F, 23.0F, -1.0F);
		this.body.addBox(-1.5F, -1.5F, -1.0F, 3.0F, 2.0F, 4.0F);
		this.setRotationAngle(body, 0.4363F, 0.0F, 0.0F);

		this.legsF = new ModelRenderer(this, 0, 6);
		this.legsF.addBox(0.25F, -2.0F, 2.25F, 1.0F, 2.0F, 1.0F);
		this.legsF.addBox(2.75F, -2.0F, 2.25F, 1.0F, 2.0F, 1.0F);

		this.legsB = new ModelRenderer(this, 4, 6);
		this.legsB.addBox(-0.25F, -1.0F, 0.0F, 1.0F, 1.0F, 2.0F);
		this.legsB.addBox(3.25F, -1.0F, 0.0F, 1.0F, 1.0F, 2.0F);

		this.eyes = new ModelRenderer(this, 0, 9);
		this.eyes.addBox(0.25F, -4.0F, 3.0F, 1.0F, 1.0F, 1.0F);
		this.eyes.addBox(2.75F, -4.0F, 3.0F, 1.0F, 1.0F, 1.0F);

		this.parts = ImmutableList.of(this.body, this.legsF, this.legsB, this.eyes);
	}

	@Override
	public void setRotationAngles(@Nonnull Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){

	}

	@Nonnull
	@Override
	public Iterable<ModelRenderer> getParts() {
		return this.parts;
	}

	private void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}