package com.bigchickenstudios.divinature.tileentity;

import com.bigchickenstudios.divinature.Strings;
import com.bigchickenstudios.divinature.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public final class ModTileEntityTypes {

    public static final RegistryObject<TileEntityType<MortarTileEntity>> MORTAR;
    public static final RegistryObject<TileEntityType<InfuserTileEntity>> INFUSER;
    public static final RegistryObject<TileEntityType<BrazierTileEntity>> BRAZIER;

    static {
        TILE_ENTITY_TYPE_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Strings.MODID);

        MORTAR = create("mortar", MortarTileEntity::new, ModBlocks.MORTAR);
        INFUSER = create("infuser", InfuserTileEntity::new, ModBlocks.INFUSER);
        BRAZIER = create("brazier", BrazierTileEntity::new, Arrays.asList(ModBlocks.STONE_BRAZIER, ModBlocks.GRANITE_BRAZIER, ModBlocks.DIORITE_BRAZIER, ModBlocks.ANDESITE_BRAZIER));
    }

    public static void init(IEventBus bus) {
        TILE_ENTITY_TYPE_DEFERRED_REGISTER.register(bus);
    }

    private static <T extends TileEntity> RegistryObject<TileEntityType<T>> create(String name, Supplier<? extends T> supplier, RegistryObject<? extends Block> block) {
        return TILE_ENTITY_TYPE_DEFERRED_REGISTER.register(name, () -> {
            TileEntityType.Builder<T> builder = TileEntityType.Builder.create(supplier, block.get());
            return builder.build(null);
        });
    }

    private static <T extends TileEntity> RegistryObject<TileEntityType<T>> create(String name, Supplier<? extends T> supplier, List<RegistryObject<? extends Block>> blocks) {
        return TILE_ENTITY_TYPE_DEFERRED_REGISTER.register(name, () -> {
            TileEntityType.Builder<T> builder = TileEntityType.Builder.create(supplier, blocks.stream().map(RegistryObject::get).toArray(Block[]::new));
            return builder.build(null);
        });
    }

    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPE_DEFERRED_REGISTER;

    private ModTileEntityTypes() {}
}
