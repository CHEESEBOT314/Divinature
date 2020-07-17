package com.bigchickenstudios.divinature.client.integration.jei;

import com.bigchickenstudios.divinature.Constants;
import com.bigchickenstudios.divinature.block.ModBlocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;

@JeiPlugin
public class DivinaturePlugin implements IModPlugin {

    public static final ResourceLocation BACKGROUND_TEXTURES = Constants.rl("textures/gui/jei/backgrounds.png");
    public static final TranslationTextComponent FILLED_POUCH = new TranslationTextComponent("item.divinature.filled_pouch");


    private static final ResourceLocation UID = Constants.rl("main");

    private InfuserCategory infuserCategory;

    @Nonnull
    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper iGuiHelper = registration.getJeiHelpers().getGuiHelper();

        registration.addRecipeCategories(this.infuserCategory = new InfuserCategory(iGuiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        this.infuserCategory.registerRecipes(registration);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.INFUSER.get()), this.infuserCategory.getUid());
    }

    public static TranslationTextComponent getTicks(int i) {
        return new TranslationTextComponent("jei.tooltip.divinature.ticks", i);
    }
}
