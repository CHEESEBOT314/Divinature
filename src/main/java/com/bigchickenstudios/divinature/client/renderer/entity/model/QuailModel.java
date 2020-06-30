package com.bigchickenstudios.divinature.client.renderer.entity.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

import javax.annotation.Nonnull;

public class QuailModel<T extends Entity> extends AgeableModel<T> {

	private final ImmutableList<ModelRenderer> headParts;
	private final ImmutableList<ModelRenderer> bodyParts;

	private final ModelRenderer body;
	private final ModelRenderer legR;
	private final ModelRenderer legL;
	private final ModelRenderer wingR;
	private final ModelRenderer wingL;

	public QuailModel() {
		this.textureWidth = 32;
		this.textureHeight = 16;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 24.0F, 0.0F);
		body.setTextureOffset(0, 8).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);
		body.setTextureOffset(16, 12).addBox(-2.0F, -5.5F, 2.0F, 4.0F, 3.0F, 1.0F, 0.0F, false);
		body.setTextureOffset(0, 4).addBox(-1.5F, -7.0F, -3.0F, 3.0F, 2.0F, 2.0F, 0.0F, false);
		body.setTextureOffset(15, 4).addBox(0.0F, -9.0F, -3.5F, 0.0F, 2.0F, 2.0F, 0.0F, false);
		body.setTextureOffset(11, 0).addBox(-1.0F, -6.5F, -4.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);

		legR = new ModelRenderer(this);
		legR.setRotationPoint(0.0F, 24.0F, 0.0F);
		legR.setTextureOffset(5, 0).addBox(-1.25F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		legL = new ModelRenderer(this);
		legL.setRotationPoint(0.0F, 24.0F, 0.0F);
		legL.setTextureOffset(0, 0).addBox(0.25F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		wingR = new ModelRenderer(this);
		wingR.setRotationPoint(3.0F, 24.0F, 2.0F);
		wingR.setTextureOffset(19, 3).addBox(-6.0F, -6.0F, -3.0F, 1.0F, 2.0F, 3.0F, 0.0F, false);

		wingL = new ModelRenderer(this);
		wingL.setRotationPoint(3.0F, 24.0F, 2.0F);
		wingL.setTextureOffset(19, 3).addBox(-1.0F, -6.0F, -3.0F, 1.0F, 2.0F, 3.0F, 0.0F, false);

		this.headParts = ImmutableList.of();
		this.bodyParts = ImmutableList.of();
	}

	@Override
	public void setRotationAngles(@Nonnull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Nonnull
	@Override
	protected Iterable<ModelRenderer> getHeadParts() {
		return this.headParts;
	}

	@Nonnull
	@Override
	protected Iterable<ModelRenderer> getBodyParts() {
		return this.bodyParts;
	}
}