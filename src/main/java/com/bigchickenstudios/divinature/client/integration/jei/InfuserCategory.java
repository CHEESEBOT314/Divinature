package com.bigchickenstudios.divinature.client.integration.jei;

import com.bigchickenstudios.divinature.block.ModBlocks;
import com.bigchickenstudios.divinature.item.crafting.InfuserRecipe;
import com.bigchickenstudios.divinature.item.crafting.ModRecipeTypes;
import com.google.common.collect.ImmutableList;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class InfuserCategory extends DivinatureResearchCategory<InfuserRecipe.Inv, InfuserRecipe> {

    public InfuserCategory(IGuiHelper iGuiHelperIn) {
        super(  ModRecipeTypes.INFUSER,
                InfuserRecipe.class,
                I18n.format("divinature.jei.infuser"),
                iGuiHelperIn.createDrawable(DivinaturePlugin.BACKGROUND_TEXTURES, 0, 0, 148, 60),
                iGuiHelperIn.createDrawableIngredient(new ItemStack(ModBlocks.INFUSER.get())));
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
}
