package com.bigchickenstudios.divinature.item.crafting;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;


public class InfuserRecipe extends AbstractPouchRecipe<InfuserRecipe.Inv> {

    private final Ingredient input;
    private final ItemStack output;

    private final int time;

    private InfuserRecipe(ResourceLocation idIn, Ingredient inputIn, NonNullList<Ingredient> pouchIn, ItemStack outputIn, int timeIn) {
        super(idIn, pouchIn);
        this.input = inputIn;
        this.output = outputIn;
        this.time = timeIn;
    }

    public int getTime() {
        return this.time;
    }

    @Nonnull
    @Override
    public ItemStack getCraftingResult(@Nonnull Inv inv) {
        return this.output.copy();
    }

    @Nonnull
    @Override
    public ItemStack getRecipeOutput() {
        return this.output;
    }

    @Nonnull
    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.INFUSER.get();
    }

    @Nonnull
    @Override
    public IRecipeType<?> getType() {
        return ModRecipeTypes.INFUSER;
    }

    @Override
    public boolean matchesElse(@Nonnull Inv inv, @Nonnull World world) {
        return this.input.test(inv.getInput());
    }

    public static class Inv extends AbstractPouchRecipe.PouchInv {
        private final ItemStack input;

        public Inv(ItemStack inputIn, NonNullList<ItemStack> pouchIn, boolean onlyPouchIn) {
            super(pouchIn, onlyPouchIn);
            this.input = inputIn;
        }

        public ItemStack getInput() {
            return this.input;
        }
    }

    public static Optional<InfuserRecipe> findMatch(ItemStack top, NonNullList<ItemStack> pouch, @Nullable World world) {
        return world != null ? AbstractRecipe.findMatch(ModRecipeTypes.INFUSER, new Inv(top, pouch, false), world) : Optional.empty();
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<InfuserRecipe> {

        @Nonnull
        @Override
        public InfuserRecipe read(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
            Ingredient input = Ingredient.deserialize(JSONUtils.isJsonArray(json, "input") ? JSONUtils.getJsonArray(json, "input") : JSONUtils.getJsonObject(json, "input"));
            NonNullList<Ingredient> pouch = AbstractPouchRecipe.readPouch(JSONUtils.getJsonArray(json, "pouch"), "divinature:infuser");
            ItemStack output = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "output"));
            return new InfuserRecipe(id, input, pouch, output, JSONUtils.getInt(json, "time"));
        }

        @Nullable
        @Override
        public InfuserRecipe read(@Nonnull ResourceLocation id, @Nonnull PacketBuffer buffer) {
            Ingredient input = Ingredient.read(buffer);
            NonNullList<Ingredient> pouch = AbstractPouchRecipe.readPouch(buffer);
            ItemStack output = buffer.readItemStack();
            int time = buffer.readVarInt();
            return new InfuserRecipe(id, input, pouch, output, time);
        }

        @Override
        public void write(@Nonnull PacketBuffer buffer, @Nonnull InfuserRecipe recipe) {
            recipe.input.write(buffer);
            recipe.writePouch(buffer);
            buffer.writeItemStack(recipe.output);
            buffer.writeVarInt(recipe.time);
        }
    }
}
