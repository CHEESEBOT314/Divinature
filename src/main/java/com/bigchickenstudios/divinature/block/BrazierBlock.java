package com.bigchickenstudios.divinature.block;

import com.bigchickenstudios.divinature.tileentity.BrazierTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BrazierBlock extends Block {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    private static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    private static final VoxelShape LOWER = VoxelShapes.or(Block.makeCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 15.0D, 11.0D),
                                                            Block.makeCuboidShape(4.0D, 15.0D, 4.0D, 12.0D, 16.0D, 12.0D));
    private static final VoxelShape UPPER = Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 4.0D, 13.0D);

    public BrazierBlock(Block.Properties properties) {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(HALF, DoubleBlockHalf.LOWER).with(LIT, false));
    }

    @Nonnull
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        if (state.get(HALF) == DoubleBlockHalf.LOWER) {
            return LOWER;
        }
        return UPPER;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HALF, LIT);
    }

    @Nonnull
    @Override
    public BlockState updatePostPlacement(@Nonnull BlockState state, Direction direction, BlockState otherState, IWorld world, BlockPos pos, BlockPos otherPos) {
        if (direction.getAxis() == Direction.Axis.Y) {
            if ((state.get(HALF) == DoubleBlockHalf.LOWER) != (direction == Direction.DOWN)) {
                if (otherState.getBlock() != this || otherState.get(HALF) == state.get(HALF)) {
                    return Blocks.AIR.getDefaultState();
                }
            }
        }
        return super.updatePostPlacement(state, direction, otherState, world, pos, otherPos);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos blockpos = context.getPos();
        return blockpos.getY() < context.getWorld().getHeight() - 1 && context.getWorld().getBlockState(blockpos.up()).isReplaceable(context) ? super.getStateForPlacement(context) : null;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        world.setBlockState(pos.up(), this.getDefaultState().with(HALF, DoubleBlockHalf.UPPER), 3);
    }

    @Override
    public void harvestBlock(@Nonnull World world, PlayerEntity player, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nullable TileEntity te, @Nonnull ItemStack stack) {
        super.harvestBlock(world, player, pos, Blocks.AIR.getDefaultState(), te, stack);
    }

    @Override
    public void onBlockHarvested(World world, @Nonnull BlockPos pos, BlockState state, @Nonnull PlayerEntity player) {
        DoubleBlockHalf half = state.get(HALF);
        BlockPos otherPos = half == DoubleBlockHalf.LOWER ? pos.up() : pos.down();
        BlockState otherState = world.getBlockState(otherPos);
        if (otherState.getBlock() == this && otherState.get(HALF) != half) {
            world.setBlockState(otherPos, Blocks.AIR.getDefaultState(), 35);
            world.playEvent(player, 2001, otherPos, Block.getStateId(otherState));
            if (!world.isRemote && !player.isCreative()) {
                spawnDrops(state, world, pos, world.getTileEntity(pos), player, player.getHeldItemMainhand());
                spawnDrops(otherState, world, otherPos, world.getTileEntity(otherPos), player, player.getHeldItemMainhand());
            }
        }
        super.onBlockHarvested(world, pos, state, player);
    }

    @Nonnull
    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        DoubleBlockHalf half = state.get(HALF);
        TileEntity tileEntity = world.getTileEntity(pos.offset(Direction.DOWN, half == DoubleBlockHalf.UPPER ? 1 : 0));
        if (tileEntity instanceof BrazierTileEntity) {
            return ((BrazierTileEntity)tileEntity).processInteract(player, hand, half);
        }
        return ActionResultType.PASS;
    }

    @Nonnull
    @Override
    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return 15;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.get(HALF) == DoubleBlockHalf.UPPER;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BrazierTileEntity();
    }
}
