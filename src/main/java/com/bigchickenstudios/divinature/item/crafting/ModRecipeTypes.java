package com.bigchickenstudios.divinature.item.crafting;

import com.bigchickenstudios.divinature.Constants;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.util.function.Function;

public final class ModRecipeTypes {

    public static final IRecipeType<InfuserRecipe> INFUSER = create("infuser", (p) -> new InfuserRecipe.Inv(ItemStack.EMPTY, p, true));

    private static <T extends IRecipe<?>> IRecipeType<T> create(String name) {
        return new IRecipeType<T>() {
            private final ResourceLocation loc = new ResourceLocation(Constants.MODID, name);
            @Override
            public String toString() {
                return loc.toString();
            }
        };
    }

    private static <T extends AbstractPouchRecipe<C>, C extends AbstractPouchRecipe.PouchInv> IRecipeType<T> create(String name, Function<NonNullList<ItemStack>, C> invSupplier) {
        IRecipeType<T> type = create(name);
        AbstractPouchRecipe.registerType(type, invSupplier);
        return type;
    }

    private ModRecipeTypes() {}
}