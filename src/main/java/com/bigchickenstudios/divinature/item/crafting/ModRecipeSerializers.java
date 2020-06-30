package com.bigchickenstudios.divinature.item.crafting;

import com.bigchickenstudios.divinature.Constants;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public final class ModRecipeSerializers {

    public static final RegistryObject<InfuserRecipe.Serializer> INFUSER;

    static {
        RECIPE_SERIALIZER_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Constants.MODID);

        INFUSER = create("infuser", InfuserRecipe.Serializer::new);
    }

    public static void init(IEventBus bus) {
        RECIPE_SERIALIZER_DEFERRED_REGISTER.register(bus);
    }

    private static <T extends IRecipeSerializer<?>> RegistryObject<T> create(String name, Supplier<? extends T> supplier) {
        return RECIPE_SERIALIZER_DEFERRED_REGISTER.register(name, supplier);
    }

    private static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZER_DEFERRED_REGISTER;

    private ModRecipeSerializers() {}
}
