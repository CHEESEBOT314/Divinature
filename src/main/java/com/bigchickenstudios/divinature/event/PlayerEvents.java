package com.bigchickenstudios.divinature.event;

import com.bigchickenstudios.divinature.world.level.block.IStrippable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public final class PlayerEvents {

    @SubscribeEvent
    public static void onInteract(PlayerInteractEvent.RightClickBlock event) {
        if (event.getItemStack().getItem() instanceof AxeItem) {
            World world = event.getWorld();
            BlockPos blockpos = event.getPos();
            BlockState blockstate = world.getBlockState(blockpos);
            if (blockstate.getBlock() instanceof IStrippable) {
                IStrippable strippable = (IStrippable)blockstate.getBlock();
                Block strippedBlock = strippable.getStrippedVersion();
                PlayerEntity player = event.getPlayer();
                world.playSound(player, blockpos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (!world.isRemote()) {
                    world.setBlockState(blockpos, strippedBlock.getDefaultState().with(BlockStateProperties.AXIS, blockstate.get(BlockStateProperties.AXIS)), 11);
                    if (player != null) {
                        event.getItemStack().damageItem(1, player, (p) -> p.sendBreakAnimation(event.getHand()));
                    }
                }
                event.setCancellationResult(ActionResultType.SUCCESS);
                event.setCanceled(true);
            }
        }
    }

    private PlayerEvents() {}
}
