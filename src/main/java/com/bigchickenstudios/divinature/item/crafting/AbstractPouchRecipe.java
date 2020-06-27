package com.bigchickenstudios.divinature.item.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonParseException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.RecipeMatcher;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class AbstractPouchRecipe<T extends AbstractPouchRecipe.PouchInv> extends AbstractRecipe<T> {

    public static final int MAX_POUCH_SIZE = 9;

    protected final NonNullList<Ingredient> pouch;

    protected AbstractPouchRecipe(ResourceLocation idIn, NonNullList<Ingredient> pouchIn) {
        super(idIn);
        this.pouch = pouchIn;
    }

    @Override
    public final boolean matches(@Nonnull T inv, @Nonnull World world) {
        return RecipeMatcher.findMatches(inv.getPouch(), this.pouch) != null && (inv.isOnlyPouch() || this.matchesElse(inv, world));
    }

    public abstract boolean matchesElse(@Nonnull T inv, @Nonnull World world);

    public static NonNullList<Ingredient> readPouch(JsonArray array, String recipe) {
        NonNullList<Ingredient> pouch = NonNullList.create();
        for (int i = 0; i < array.size(); i++) {
            Ingredient ingredient = Ingredient.deserialize(array.get(i));
            if (!ingredient.hasNoMatchingItems()) {
                pouch.add(ingredient);
            }
        }
        if (pouch.isEmpty()) {
            throw new JsonParseException("No pouch ingredients for " + recipe + " recipe!");
        }
        if (pouch.size() > MAX_POUCH_SIZE) {
            throw new JsonParseException("Too many pouch ingredients for " + recipe + " recipe the max is " + MAX_POUCH_SIZE);
        }
        return pouch;
    }

    public static NonNullList<Ingredient> readPouch(PacketBuffer buffer) {
        int count = buffer.readVarInt();
        NonNullList<Ingredient> pouch = NonNullList.withSize(count, Ingredient.EMPTY);
        for (int i = 0; i < count; i++) {
            pouch.set(i, Ingredient.read(buffer));
        }
        return pouch;
    }

    protected void writePouch(PacketBuffer buffer) {
        buffer.writeVarInt(this.pouch.size());
        for (Ingredient i : this.pouch) {
            i.write(buffer);
        }
    }

    public static class PouchInv extends AbstractRecipe.FakeInv {

        private final NonNullList<ItemStack> pouch;
        private final boolean onlyPouch;

        public PouchInv(NonNullList<ItemStack> pouchIn, boolean onlyPouchIn) {
            this.pouch = pouchIn;
            this.onlyPouch = onlyPouchIn;
        }

        public NonNullList<ItemStack> getPouch() {
            return this.pouch;
        }

        public boolean isOnlyPouch() {
            return this.onlyPouch;
        }
    }


    private static class Checker<T extends AbstractPouchRecipe<C>, C extends AbstractPouchRecipe.PouchInv> {

        private final IRecipeType<T> type;
        private final Function<NonNullList<ItemStack>, C> invSupplier;

        private Checker(IRecipeType<T> typeIn, Function<NonNullList<ItemStack>, C> invSupplierIn) {
            this.type = typeIn;
            this.invSupplier = invSupplierIn;
        }

        private boolean has(NonNullList<ItemStack> pouch, World world) {
            return world.getRecipeManager().getRecipe(this.type, this.invSupplier.apply(pouch), world).isPresent();
        }
    }

    private static final List<Checker<?, ?>> CHECKERS = new ArrayList<>();

    public static <T extends AbstractPouchRecipe<C>, C extends AbstractPouchRecipe.PouchInv> void registerType(IRecipeType<T> typeIn, Function<NonNullList<ItemStack>, C> invSupplierIn) {
        CHECKERS.add(new Checker<>(typeIn, invSupplierIn));
    }

    public static boolean isValidPouch(NonNullList<ItemStack> pouch, World world) {
        for (Checker c : CHECKERS) {
            if (c.has(pouch, world)) {
                return true;
            }
        }
        return false;
    }
}
