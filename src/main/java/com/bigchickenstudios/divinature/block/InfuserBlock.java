package com.bigchickenstudios.divinature.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.PushReaction;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InfuserBlock extends Block {

    private static final BooleanProperty LIT = BlockStateProperties.LIT;
    private static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public InfuserBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(LIT, false).with(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(LIT, HALF);
    }

    @Nonnull
    @Override
    public BlockState updatePostPlacement(@Nonnull BlockState state, Direction direction, BlockState otherState, IWorld world, BlockPos pos, BlockPos otherPos) {
        return super.updatePostPlacement(state, direction, otherState, world, pos, otherPos);
    }

    @Nonnull
    @Override
    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.get(HALF) == DoubleBlockHalf.LOWER;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return null;
    }
}
