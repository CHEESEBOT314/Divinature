package com.bigchickenstudios.divinature.research;

import com.bigchickenstudios.divinature.item.crafting.IResearchRecipe;
import net.minecraft.entity.player.ServerPlayerEntity;

public final class ResearchUtils {


    public static boolean checkResearch(ServerPlayerEntity playerEntity, IResearchRecipe researchRecipe) {
        return PlayerResearch.getPlayer(playerEntity).getProgress(ResearchManager.getInstance().getResearch(researchRecipe.getRequiredResearch())).map(ResearchProgress::isComplete).orElse(false);
    }

    private ResearchUtils() {}
}
