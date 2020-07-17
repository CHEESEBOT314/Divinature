package com.bigchickenstudios.divinature.item.crafting;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Optional;

public abstract class AbstractRecipe<T extends IInventory> implements IRecipe<T> {

    private final ResourceLocation id;

    protected AbstractRecipe(ResourceLocation idIn) {
        this.id = idIn;
    }

    @Nonnull
    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    public static class FakeInv implements IInventory {

        @Override
        public int getSizeInventory() {
            return 0;
        }
        @Override
        public boolean isEmpty() {
            return false;
        }
        @Nonnull
        @Override
        public ItemStack getStackInSlot(int index) {
            return ItemStack.EMPTY;
        }
        @Nonnull
        @Override
        public ItemStack decrStackSize(int index, int count) {
            return ItemStack.EMPTY;
        }
        @Nonnull
        @Override
        public ItemStack removeStackFromSlot(int index) {
            return ItemStack.EMPTY;
        }
        @Override
        public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {}
        @Override
        public void markDirty() {}
        @Override
        public boolean isUsableByPlayer(@Nonnull PlayerEntity player) {
            return false;
        }
        @Override
        public void clear() {}
    }

    public static <T extends AbstractRecipe<C>, C extends FakeInv> Optional<T> findMatch(IRecipeType<T> type, C inv, World world) {
        return world.getRecipeManager().getRecipe(type, inv, world);
    }
}
