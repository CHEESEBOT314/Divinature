package com.bigchickenstudios.divinature.item;

import com.bigchickenstudios.divinature.Constants;
import com.bigchickenstudios.divinature.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public final class ModItems {

    public static final RegistryObject<BlockNamedItem> BURDOCK_SEEDS;
    public static final RegistryObject<Item> BURDOCK_ROOT;

    public static final RegistryObject<Item> EMPTY_POUCH;
    public static final RegistryObject<FilledPouchItem> FILLED_POUCH;

    public static final RegistryObject<NatureArmorItem> NATURE_HELMET;

    public static final ItemGroup GROUP;
    private static final UnaryOperator<Item.Properties> I = UnaryOperator.identity();
    private static final BiFunction<Item.Properties, Boolean, Item.Properties> GROUP_FUNC;

    static {
        ITEM_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MODID);

        create(ModBlocks.BLACKBELL, true);

        create(ModBlocks.ELM_PLANKS, true);
        create(ModBlocks.ELM_LOG, true);
        create(ModBlocks.STRIPPED_ELM_LOG, true);
        create(ModBlocks.ELM_WOOD, true);
        create(ModBlocks.STRIPPED_ELM_WOOD, true);
        create(ModBlocks.ELM_LEAVES, true);

        create(ModBlocks.MORTAR, true);
        create(ModBlocks.INFUSER, true);

        BURDOCK_SEEDS = create("burdock_seeds", ModBlocks.BURDOCK, true);
        BURDOCK_ROOT = create("burdock_root", true);

        EMPTY_POUCH = create("empty_pouch", true);
        FILLED_POUCH = create("filled_pouch", FilledPouchItem::new, (p) -> p.maxStackSize(4), false);

        NATURE_HELMET = create("nature_helmet", (p) -> new NatureArmorItem(EquipmentSlotType.HEAD, p), I, true);

        GROUP = new ItemGroup(Constants.MODID) {
            @Nonnull
            @Override
            public ItemStack createIcon() {
                return new ItemStack(BURDOCK_SEEDS.get());
            }
        };
        GROUP_FUNC = (p, b) -> b ? p.group(GROUP) : p;
    }

    public static void init(IEventBus bus) {
        ITEM_DEFERRED_REGISTER.register(bus);
    }

    private static <T extends Item> RegistryObject<T> create(String name, Function<Item.Properties, ? extends T> supplier, UnaryOperator<Item.Properties> propertiesModifier, boolean addToGroup) {
        return ITEM_DEFERRED_REGISTER.register(name, () -> supplier.apply(propertiesModifier.apply(GROUP_FUNC.apply(new Item.Properties(), addToGroup))));
    }
    private static <T extends Item> RegistryObject<T> create(String name, Function<Item.Properties, ? extends T> supplier, boolean addToGroup) {
        return create(name, supplier, I, addToGroup);
    }
    private static RegistryObject<Item> create(String name, UnaryOperator<Item.Properties> propertiesModifier, boolean addToGroup) {
        return create(name, Item::new, propertiesModifier, addToGroup);
    }
    private static RegistryObject<Item> create(String name, boolean addToGroup) {
        return create(name, I, addToGroup);
    }

    private static <T extends BlockItem, S extends Block> RegistryObject<T> create(RegistryObject<S> obj, BiFunction<? super S, Item.Properties, ? extends T> supplier, UnaryOperator<Item.Properties> propertiesModifier, boolean addToGroup) {
        return create(obj.getId().getPath(), (p) -> supplier.apply(obj.get(), p), propertiesModifier, addToGroup);
    }
    private static <T extends BlockItem, S extends Block> RegistryObject<T> create(RegistryObject<S> obj, BiFunction<? super S, Item.Properties, ? extends T> supplier, boolean addToGroup) {
        return create(obj, supplier, I, addToGroup);
    }
    private static <S extends Block> RegistryObject<BlockItem> create(RegistryObject<S> obj, UnaryOperator<Item.Properties> propertiesModifier, boolean addToGroup) {
        return create(obj, BlockItem::new, propertiesModifier, addToGroup);
    }
    private static <S extends Block> RegistryObject<BlockItem> create(RegistryObject<S> obj, boolean addToGroup) {
        return create(obj, I, addToGroup);
    }
    private static <S extends Block> RegistryObject<BlockNamedItem> create(String name, RegistryObject<S> obj, UnaryOperator<Item.Properties> propertiesModifier, boolean addToGroup) {
        return create(name, (p) -> new BlockNamedItem(obj.get(), p), propertiesModifier, addToGroup);
    }
    private static <S extends Block> RegistryObject<BlockNamedItem> create(String name, RegistryObject<S> obj, boolean addToGroup) {
        return create(name, obj, I, addToGroup);
    }

    private static final DeferredRegister<Item> ITEM_DEFERRED_REGISTER;

    private ModItems() {}
}
