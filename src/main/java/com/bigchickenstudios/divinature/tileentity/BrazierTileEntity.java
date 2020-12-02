package com.bigchickenstudios.divinature.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;

public class BrazierTileEntity extends TileEntity {


    public BrazierTileEntity() {
        super(ModTileEntityTypes.BRAZIER.get());
    }

    public ActionResultType processInteract(PlayerEntity player, Hand hand, DoubleBlockHalf half) {
        return ActionResultType.SUCCESS;
    }
}
