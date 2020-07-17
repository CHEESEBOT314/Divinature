package com.bigchickenstudios.divinature.client.integration.jei;

import com.bigchickenstudios.divinature.block.ModBlocks;
import com.bigchickenstudios.divinature.client.DivinatureClient;
import com.bigchickenstudios.divinature.item.crafting.InfuserRecipe;
import com.bigchickenstudios.divinature.item.crafting.ModRecipeTypes;
import com.google.common.collect.ImmutableList;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class InfuserCategory implements IRecipeCategory<InfuserRecipe> {

    private final ResourceLocation uid = new ResourceLocation(ModRecipeTypes.INFUSER.toString());
    private final String title;
    private final IDrawable background;
    private final IDrawable icon;

    public InfuserCategory(IGuiHelper iGuiHelperIn) {
        this.title = I18n.format("divinature.jei.infuser");
        this.background = iGuiHelperIn.createDrawable(DivinaturePlugin.BACKGROUND_TEXTURES, 0, 0, 148, 60);
        this.icon = iGuiHelperIn.createDrawableIngredient(new ItemStack(ModBlocks.INFUSER.get()));
    }

    @Nonnull
    @Override
    public ResourceLocation getUid() {
        return this.uid;
    }

    @Nonnull
    @Override
    public Class<? extends InfuserRecipe> getRecipeClass() {
        return InfuserRecipe.class;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return this.title;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Nonnull
    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setIngredients(@Nonnull InfuserRecipe infuserRecipe, @Nonnull IIngredients iIngredients) {
        ImmutableList.Builder<Ingredient> builder = ImmutableList.builder();
        builder.add(infuserRecipe.getInput());
        builder.addAll(infuserRecipe.getPouch());
        iIngredients.setInputIngredients(builder.build());
        iIngredients.setOutput(VanillaTypes.ITEM, infuserRecipe.getRecipeOutput());
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout iRecipeLayout, @Nonnull InfuserRecipe infuserRecipe, @Nonnull IIngredients iIngredients) {
        IGuiItemStackGroup itemStackGroup = iRecipeLayout.getItemStacks();

        itemStackGroup.init(0, true, 66, 12);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                itemStackGroup.init(1 + j + (3 * i), true, 3 + 18 * j, 3 + 18 * i);
            }
        }
        itemStackGroup.init(10, false, 125, 21);
        itemStackGroup.set(iIngredients);
    }

    @Nonnull
    @Override
    public List<ITextComponent> getTooltipStrings(InfuserRecipe recipe, double mouseX, double mouseY) {
        if (mouseX > 91.0D && mouseX < 113.0D && mouseY > 22.0D && mouseY < 37.0D) {
            return Collections.singletonList(DivinaturePlugin.getTicks(recipe.getTime()));
        }

        return Collections.emptyList();
    }

    @Override
    public boolean handleClick(InfuserRecipe recipe, double mouseX, double mouseY, int mouseButton) {
        return false;
    }

    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(DivinatureClient.getRecipes(ModRecipeTypes.INFUSER), this.uid);
    }
}
