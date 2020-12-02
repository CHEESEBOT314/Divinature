package com.bigchickenstudios.divinature.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class InfuserParticle extends SpriteTexturedParticle {

    private static final Random RANDOM = new Random();
    private final IAnimatedSprite spriteWithAge;

    private InfuserParticle(ClientWorld world, double x, double y, double z, double r, double g, double b, IAnimatedSprite sprite) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.spriteWithAge = sprite;
        this.motionX = 0.5D * (0.5D - RANDOM.nextDouble()) * 0.10000000149011612D;
        this.motionY = 0.10000000149011612D;
        this.motionZ = 0.5D * (0.5D - RANDOM.nextDouble()) * 0.10000000149011612D;

        this.setColor((float)r, (float)g, (float)b);

        this.particleScale *= 0.75F;
        this.maxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
        this.canCollide = false;
        this.selectSpriteWithAge(this.spriteWithAge);
    }

    @Override
    public void tick() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.age++ >= this.maxAge) {
            this.setExpired();
        } else {
            this.selectSpriteWithAge(this.spriteWithAge);
            this.motionY += 0.004D;
            this.move(this.motionX, this.motionY, this.motionZ);
            if (this.posY == this.prevPosY) {
                this.motionX *= 1.1D;
                this.motionZ *= 1.1D;
            }

            this.motionX *= 0.9599999785423279D;
            this.motionY *= 0.9599999785423279D;
            this.motionZ *= 0.9599999785423279D;
            if (this.onGround) {
                this.motionX *= 0.699999988079071D;
                this.motionZ *= 0.699999988079071D;
            }

        }
    }

    @Nonnull
    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements IParticleFactory<BasicParticleType> {

        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite spriteSetIn) {
            this.spriteSet = spriteSetIn;
        }

        @Nullable
        @Override
        public Particle makeParticle(@Nonnull BasicParticleType basicParticleType, @Nonnull ClientWorld clientWorld, double v, double v1, double v2, double v3, double v4, double v5) {
            return new InfuserParticle(clientWorld, v, v1, v2, v3, v4, v5, this.spriteSet);
        }
    }
}
