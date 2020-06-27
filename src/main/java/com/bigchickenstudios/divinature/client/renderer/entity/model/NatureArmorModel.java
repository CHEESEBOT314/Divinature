package com.bigchickenstudios.divinature.client.renderer.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

import javax.annotation.Nonnull;

public abstract class NatureArmorModel extends BipedModel<LivingEntity> {

    public NatureArmorModel() {
        super(RenderType::getEntityCutoutNoCull, 1.0F, 0.0F, 64, 64);
    }

    @Override
    public void setRotationAngles(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.updateRotations();
    }

    protected abstract void updateRotations();

    @Nonnull
    @Override
    protected abstract Iterable<ModelRenderer> getBodyParts();

    @Nonnull
    @Override
    protected Iterable<ModelRenderer> getHeadParts() {
        return ImmutableList.of();
    }

    protected void copyToEach(ModelRenderer parent, ModelRenderer... children) {
        for (ModelRenderer child : children) {
            child.copyModelAngles(parent);
        }
    }

    public static <A extends BipedModel<?>> A get(EquipmentSlotType slot, A _default) {
        A out = _default;
        switch (slot) {
            case HEAD:
                out = (A)Head.INSTANCE;
                break;
        }
        return out;
    }

    public static class Head extends NatureArmorModel {

        private static final Head INSTANCE = new Head();

        private final ImmutableList<ModelRenderer> parts;

        private ModelRenderer main;
        private ModelRenderer extra0;
        private ModelRenderer extra1;
        private ModelRenderer extra2;

        public Head() {
            super();
            this.main = new ModelRenderer(this, 0, 0);
            this.main.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.64F);
            this.main.setRotationPoint(0.0F, 0.0F, 0.0F);
            this.extra0 = new ModelRenderer(this, 32, 0);
            this.extra0.addBox(-4.0F, -7.5F, 4.72F, 8.0F, 7.0F, 1.0F, 0.64F, 0.56F, 0.08F);
            this.extra0.setRotationPoint(0.0F, 0.0F, 0.0F);
            this.extra1 = new ModelRenderer(this, 32, 10);
            this.extra1.addBox(-3.0F, -6.5F, 5.88F, 6.0F, 6.0F, 1.0F, 0.48F, 0.48F, 0.08F);
            this.extra1.setRotationPoint(0.0F, 0.0F, 0.0F);
            this.extra2 = new ModelRenderer(this, 0, 17);
            this.extra2.addBox(-2.0F, -5.0F, 7.04F, 4.0F, 4.0F, 1.0F, 0.32F, 0.32F, 0.08F);
            this.extra2.setRotationPoint(0.0F, 0.0F, 0.0F);

            this.parts = ImmutableList.of(this.main, this.extra0, this.extra1, this.extra2);
        }

        @Override
        protected void updateRotations() {
            this.copyToEach(this.bipedHead, this.main, this.extra0, this.extra1, this.extra2);
        }

        @Nonnull
        @Override
        protected Iterable<ModelRenderer> getBodyParts() {
            return this.parts;
        }
    }
}
