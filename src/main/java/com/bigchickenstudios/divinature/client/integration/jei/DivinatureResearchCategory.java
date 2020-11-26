package com.bigchickenstudios.divinature.client.integration.jei;

import com.bigchickenstudios.divinature.item.crafting.IResearchRecipe;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.stream.Collectors;

public abstract class DivinatureResearchCategory<C extends IInventory, T extends IRecipe<C> & IResearchRecipe> extends DivinatureCategory<C, T> {

    public DivinatureResearchCategory(IRecipeType<T> typeIn, Class<T> clazzIn, String titleIn, IDrawable backgroundIn, IDrawable iconIn) {
        super(typeIn, clazzIn, titleIn, backgroundIn, iconIn);
    }

    public List<T> getAllRecipesWithResearch(List<ResourceLocation> researchList) {
        return this.getAllRecipes().stream().filter((t) -> researchList.contains(t.getRequiredResearch())).collect(Collectors.toList());
    }
}
