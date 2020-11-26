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


public class InfuserRecipe extends AbstractRecipe<InfuserRecipe.Inv> implements IPouchRecipe, IResearchRecipe {

    private final Ingredient input;
    private final NonNullList<Ingredient> pouch;
    private final ResourceLocation research;

    private final ItemStack output;

    private final int time;

    private InfuserRecipe(ResourceLocation idIn, Ingredient inputIn, NonNullList<Ingredient> pouchIn, ResourceLocation researchIn, ItemStack outputIn, int timeIn) {
        super(idIn);
        this.input = inputIn;
        this.pouch = pouchIn;
        this.research = researchIn;
        this.output = outputIn;
        this.time = timeIn;
    }

    public Ingredient getInput() {
        return this.input;
    }

    @Override
    public NonNullList<Ingredient> getPouch() {
        return this.pouch;
    }

    @Override
    public ResourceLocation getRequiredResearch() {
        return this.research;
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
    public boolean matches(@Nonnull Inv inv, @Nonnull World worldIn) {
        return this.input.test(inv.getInput()) && this.matchesPouch(inv.getPouch());
    }

    public static class Inv extends AbstractRecipe.FakeInv {
        private final ItemStack input;
        private final NonNullList<ItemStack> pouch;

        private Inv(ItemStack inputIn, NonNullList<ItemStack> pouchIn) {
            this.input = inputIn;
            this.pouch = pouchIn;
        }

        private ItemStack getInput() {
            return this.input;
        }

        private NonNullList<ItemStack> getPouch() {
            return this.pouch;
        }
    }

    public static Optional<InfuserRecipe> findMatch(ItemStack top, NonNullList<ItemStack> pouch, @Nullable World world) {
        return world != null ? AbstractRecipe.findMatch(ModRecipeTypes.INFUSER, new Inv(top, pouch), world) : Optional.empty();
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<InfuserRecipe> {

        @Nonnull
        @Override
        public InfuserRecipe read(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
            Ingredient input = Ingredient.deserialize(JSONUtils.isJsonArray(json, "input") ? JSONUtils.getJsonArray(json, "input") : JSONUtils.getJsonObject(json, "input"));
            NonNullList<Ingredient> pouch = IPouchRecipe.readPouch(JSONUtils.getJsonArray(json, "pouch"));
            ResourceLocation research = new ResourceLocation(JSONUtils.getString(json, "research"));
            ItemStack output = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "output"));
            return new InfuserRecipe(id, input, pouch, research, output, JSONUtils.getInt(json, "time"));
        }

        @Nullable
        @Override
        public InfuserRecipe read(@Nonnull ResourceLocation id, @Nonnull PacketBuffer buffer) {
            Ingredient input = Ingredient.read(buffer);
            NonNullList<Ingredient> pouch = IPouchRecipe.readPouch(buffer);
            ResourceLocation research = buffer.readResourceLocation();
            ItemStack output = buffer.readItemStack();
            int time = buffer.readVarInt();
            return new InfuserRecipe(id, input, pouch, research, output, time);
        }

        @Override
        public void write(@Nonnull PacketBuffer buffer, @Nonnull InfuserRecipe recipe) {
            recipe.input.write(buffer);
            IPouchRecipe.writePouch(recipe.pouch, buffer);
            buffer.writeResourceLocation(recipe.research);
            buffer.writeItemStack(recipe.output);
            buffer.writeVarInt(recipe.time);
        }
    }
}
