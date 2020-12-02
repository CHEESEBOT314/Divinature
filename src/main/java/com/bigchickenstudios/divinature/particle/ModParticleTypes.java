package com.bigchickenstudios.divinature.particle;

import com.bigchickenstudios.divinature.Strings;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModParticleTypes {

    public static final RegistryObject<BasicParticleType> INFUSER;

    static {
        PARTICLE_TYPE_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Strings.MODID);

        INFUSER = create("infuser", false);
    }

    public static void init(IEventBus bus) {
        PARTICLE_TYPE_DEFERRED_REGISTER.register(bus);
    }

    private static RegistryObject<BasicParticleType> create(String name, boolean alwaysShow) {
        return PARTICLE_TYPE_DEFERRED_REGISTER.register(name, () -> new BasicParticleType(alwaysShow));
    }

    private static final DeferredRegister<ParticleType<?>> PARTICLE_TYPE_DEFERRED_REGISTER;

    private ModParticleTypes() {}
}
