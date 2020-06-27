package com.bigchickenstudios.divinature.item.crafting;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Supplier;

public final class RecipeTester {
















    private static class Tester<T extends IRecipe<C> & IPouchRecipe, C extends IInventory> {
        private final IRecipeType<T> type;
        private final Supplier<C> supplier;

        private Tester(IRecipeType<T> typeIn, Supplier<C> supplierIn) {
            this.type = typeIn;
            this.supplier = supplierIn;
        }

        private List<T> get(World world) {
            return world.getRecipeManager().getRecipes(this.type, this.supplier.get(), world);
        }
    }

    private RecipeTester() {}
}
