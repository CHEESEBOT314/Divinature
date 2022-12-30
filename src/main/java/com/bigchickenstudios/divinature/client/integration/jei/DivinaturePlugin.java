package com.bigchickenstudios.divinature.client.integration.jei;

import com.bigchickenstudios.divinature.Strings;
import com.bigchickenstudios.divinature.world.level.block.ModBlocks;
import com.bigchickenstudios.divinature.event.research.ResearchCompletedEvent;
import com.google.common.collect.Sets;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nonnull;
import java.util.Set;

@JeiPlugin
public class DivinaturePlugin implements IModPlugin {

    public static final ResourceLocation BACKGROUND_TEXTURES = Strings.createResourceLocation("textures/gui/jei/backgrounds.png");

    private static final ResourceLocation UID = Strings.createResourceLocation("main");

    private final Set<DivinatureResearchCategory<?, ?>> researchCategories = Sets.newLinkedHashSet();

    private InfuserCategory infuserCategory;

    private IJeiRuntime runtime;

    @Nonnull
    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper iGuiHelper = registration.getJeiHelpers().getGuiHelper();

        registration.addRecipeCategories(this.infuserCategory = new InfuserCategory(iGuiHelper));

        this.researchCategories.add(this.infuserCategory);

        MinecraftForge.EVENT_BUS.addListener(this::onResearchCompleted);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        this.infuserCategory.registerRecipes(registration);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.INFUSER.get()), this.infuserCategory.getUid());
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        this.runtime = jeiRuntime;
        IRecipeManager recipeManager = this.runtime.getRecipeManager();
        this.researchCategories.forEach((c) -> this.hideAllRecipes(recipeManager, c));
    }

    private <T> void hideAllRecipes(IRecipeManager recipeManager, IRecipeCategory<T> category) {
        recipeManager.getRecipes(category).forEach((r) -> recipeManager.hideRecipe(r, category.getUid()));
    }

    public static TranslationTextComponent getTicks(int i) {
        return new TranslationTextComponent("jei.tooltip.divinature.ticks", i);
    }

    private void onResearchCompleted(ResearchCompletedEvent event) {
        IRecipeManager recipeManager = this.runtime.getRecipeManager();
        this.researchCategories.forEach((c) -> c.getAllRecipesWithResearch(event.getResearch()).forEach((t) -> recipeManager.unhideRecipe(t, c.getUid())));
    }
}
