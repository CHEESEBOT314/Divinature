package com.bigchickenstudios.divinature.item.crafting;

import com.bigchickenstudios.divinature.Strings;
import com.bigchickenstudios.divinature.client.multiplayer.ClientResearchManager;
import com.bigchickenstudios.divinature.research.PlayerResearch;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.DistExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public final class ModRecipeTypes {

    private static final List<BiFunction<PlayerEntity, NonNullList<ItemStack>, Boolean>> POUCH_CHECKERS = new ArrayList<>();

    private static final BiFunction<PlayerEntity, ResourceLocation, Boolean> RESEARCH_CHECKER = DistExecutor.safeRunForDist(() -> ClientResearchManager::getResearchChecker, () -> PlayerResearch::getResearchChecker);

    public static final IRecipeType<InfuserRecipe> INFUSER = create("infuser");

    static {
        registerPouchRecipeWithResearch(INFUSER);
    }

    private static <T extends IRecipe<?>> IRecipeType<T> create(String name) {
        return new IRecipeType<T>() {
            private final ResourceLocation loc = new ResourceLocation(Strings.MODID, name);
            @Override
            public String toString() {
                return this.loc.toString();
            }
        };
    }

    private static <C extends IInventory, T extends IRecipe<C> & IPouchRecipe> void registerPouchRecipe(IRecipeType<T> type) {
        POUCH_CHECKERS.add((pe, pi) -> {
            for (T t : pe.getEntityWorld().getRecipeManager().getRecipesForType(type)) {
                if (t.matchesPouch(pi)) {
                    return true;
                }
            }
            return false;
        });
    }

    private static <C extends IInventory, T extends IRecipe<C> & IPouchRecipe & IResearchRecipe> void registerPouchRecipeWithResearch(IRecipeType<T> type) {
        POUCH_CHECKERS.add((pe, pi) -> {
            for (T t : pe.getEntityWorld().getRecipeManager().getRecipesForType(type)) {
                if (t.matchesPouch(pi) && RESEARCH_CHECKER.apply(pe, t.getRequiredResearch())) {
                    return true;
                }
            }
            return false;
        });
    }

    public static boolean isPouchValid(PlayerEntity player, NonNullList<ItemStack> pouch) {
        for (BiFunction<PlayerEntity, NonNullList<ItemStack>, Boolean> func : POUCH_CHECKERS) {
            if (func.apply(player, pouch)) {
                return true;
            }
        }
        return false;
    }

    private ModRecipeTypes() {}
}