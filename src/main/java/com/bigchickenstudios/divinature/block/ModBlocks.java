package com.bigchickenstudios.divinature.block;

import com.bigchickenstudios.divinature.Constants;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.potion.Effects;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;

public final class ModBlocks {

    public static final RegistryObject<FlowerBlock> BLACKBELL;

    public static final RegistryObject<BurdockBlock> BURDOCK;

    public static final RegistryObject<Block> ELM_PLANKS;
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_ELM_LOG;
    public static final RegistryObject<RotatedPillarBlock> ELM_LOG;
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_ELM_WOOD;
    public static final RegistryObject<RotatedPillarBlock> ELM_WOOD;
    public static final RegistryObject<LeavesBlock> ELM_LEAVES;

    public static final RegistryObject<Block> MAGNOLIA_PLANKS;
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_MAGNOLIA_LOG;
    public static final RegistryObject<RotatedPillarBlock> MAGNOLIA_LOG;
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_MAGNOLIA_WOOD;
    public static final RegistryObject<RotatedPillarBlock> MAGNOLIA_WOOD;
    public static final RegistryObject<LeavesBlock> MAGNOLIA_LEAVES;

    public static final RegistryObject<MortarBlock> MORTAR;
    public static final RegistryObject<InfuserBlock> INFUSER;

    static {
        BLOCK_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MODID);

        BLACKBELL = create("blackbell", (p) -> new FlowerBlock(Effects.WEAKNESS, 9, p), Material.PLANTS, (p) -> p.doesNotBlockMovement().hardnessAndResistance(0.0F).sound(SoundType.PLANT));

        BURDOCK = create("burdock", BurdockBlock::new, Material.PLANTS, (p) -> p.doesNotBlockMovement().tickRandomly().hardnessAndResistance(0.0F).sound(SoundType.CROP));

        ELM_PLANKS = create("elm_planks", Material.WOOD, (p) -> p.hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD));
        STRIPPED_ELM_LOG = create("stripped_elm_log", RotatedPillarBlock::new, Material.WOOD, (p) -> p.hardnessAndResistance(2.0F).sound(SoundType.WOOD));
        ELM_LOG = create("elm_log", (p) -> new StrippableRotatedPillarBlock(p, STRIPPED_ELM_LOG), Material.WOOD, (p) -> p.hardnessAndResistance(2.0F).sound(SoundType.WOOD));
        STRIPPED_ELM_WOOD = create("stripped_elm_wood", RotatedPillarBlock::new, Material.WOOD, (p) -> p.hardnessAndResistance(2.0F).sound(SoundType.WOOD));
        ELM_WOOD = create("elm_wood", (p) -> new StrippableRotatedPillarBlock(p, STRIPPED_ELM_WOOD), Material.WOOD, (p) -> p.hardnessAndResistance(2.0F).sound(SoundType.WOOD));
        ELM_LEAVES = create("elm_leaves", LeavesBlock::new, Material.LEAVES, (p) -> p.hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid());

        MAGNOLIA_PLANKS = create("magnolia_planks", Material.WOOD, (p) -> p.hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD));
        STRIPPED_MAGNOLIA_LOG = create("stripped_magnolia_log", RotatedPillarBlock::new, Material.WOOD, (p) -> p.hardnessAndResistance(2.0F).sound(SoundType.WOOD));
        MAGNOLIA_LOG = create("magnolia_log", (p) -> new StrippableRotatedPillarBlock(p, STRIPPED_MAGNOLIA_LOG), Material.WOOD, (p) -> p.hardnessAndResistance(2.0F).sound(SoundType.WOOD));
        STRIPPED_MAGNOLIA_WOOD = create("stripped_magnolia_wood", RotatedPillarBlock::new, Material.WOOD, (p) -> p.hardnessAndResistance(2.0F).sound(SoundType.WOOD));
        MAGNOLIA_WOOD = create("magnolia_wood", (p) -> new StrippableRotatedPillarBlock(p, STRIPPED_MAGNOLIA_WOOD), Material.WOOD, (p) -> p.hardnessAndResistance(2.0F).sound(SoundType.WOOD));
        MAGNOLIA_LEAVES = create("magnolia_leaves", LeavesBlock::new, Material.LEAVES, (p) -> p.hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid());

        MORTAR = create("mortar", MortarBlock::new, Material.ROCK);
        INFUSER = create("infuser", InfuserBlock::new, Material.ROCK);
    }

    public static void init(IEventBus bus) {
        BLOCK_DEFERRED_REGISTER.register(bus);
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
