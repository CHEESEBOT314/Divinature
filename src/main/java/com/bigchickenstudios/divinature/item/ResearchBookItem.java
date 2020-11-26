package com.bigchickenstudios.divinature.item;

import com.bigchickenstudios.divinature.client.DivinatureClient;
import com.bigchickenstudios.divinature.client.gui.research.ResearchScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ResearchBookItem extends Item {

    public ResearchBookItem(Properties properties) {
        super(properties);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
        if (world.isRemote()) {
            Minecraft.getInstance().displayGuiScreen(new ResearchScreen(DivinatureClient.getResearchManager()));
        }
        return ActionResult.resultConsume(player.getHeldItem(hand));
    }
}
