package com.bigchickenstudios.divinature.client.integration.jei;

import com.bigchickenstudios.divinature.client.DivinatureClient;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class DivinatureCategory<C extends IInventory, T extends IRecipe<C>> implements IRecipeCategory<T> {

    private final IRecipeType<T> type;
    private final ResourceLocation uid;
    private final Class<T> clazz;
    private final String title;
    private final IDrawable background;
    private final IDrawable icon;

    public DivinatureCategory(IRecipeType<T> typeIn, Class<T> clazzIn, String titleIn, IDrawable backgroundIn, IDrawable iconIn) {
        this.type = typeIn;
        this.uid = new ResourceLocation(this.type.toString());
        this.clazz = clazzIn;
        this.title = titleIn;
        this.background = backgroundIn;
        this.icon = iconIn;
    }

    @Nonnull
    @Override
    public ResourceLocation getUid() {
        return this.uid;
    }

    @Nonnull
    @Override
    public Class<? extends T> getRecipeClass() {
        return this.clazz;
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

    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(this.getAllRecipes(), this.getUid());
    }

    public List<T> getAllRecipes() {
        return DivinatureClient.getRecipes(this.type);
    }
}
