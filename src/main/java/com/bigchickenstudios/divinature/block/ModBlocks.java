package com.bigchickenstudios.divinature.block;

import com.bigchickenstudios.divinature.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;

public final class ModBlocks {

    public static final RegistryObject<InfuserBlock> INFUSER;

    static {
        BLOCK_DEFERRED_REGISTER = new DeferredRegister<>(ForgeRegistries.BLOCKS, Constants.MODID);

        INFUSER = create("infuser", InfuserBlock::new, Material.ROCK);
    }


    private static <T extends Block> RegistryObject<T> create(String name, Function<Block.Properties, ? extends T> supplier, Material material, Function<Block.Properties, Block.Properties> propertiesModifier) {
        return BLOCK_DEFERRED_REGISTER.register(name, () -> supplier.apply(propertiesModifier.apply(Block.Properties.create(material))));
    }
    private static <T extends Block> RegistryObject<T> create(String name, Function<Block.Properties, ? extends T> supplier, Material material) {
        return create(name, supplier, material, (p) -> p);
    }
    private static RegistryObject<Block> create(String name, Material material, Function<Block.Properties, Block.Properties> propertiesModifier) {
        return create(name, Block::new, material, propertiesModifier);
    }
    private static RegistryObject<Block> create(String name, Material material) {
        return create(name, material, (p) -> p);
    }

    private static final DeferredRegister<Block> BLOCK_DEFERRED_REGISTER;

    private ModBlocks() {}
}
