package com.bigchickenstudios.divinature.tileentity;

import com.bigchickenstudios.divinature.block.MortarBlock;
import com.bigchickenstudios.divinature.item.FilledPouchItem;
import com.bigchickenstudios.divinature.item.ModItems;
import com.bigchickenstudios.divinature.item.crafting.IPouchRecipe;
import com.bigchickenstudios.divinature.item.crafting.ModRecipeTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MortarTileEntity extends TileEntity implements ITickableTileEntity {

    private final NonNullList<ItemStack> itemStacks = NonNullList.create();
    private boolean isEmptying = false;
    private int level = 0;

    private boolean isGrinding = false;
    private int grindTime = 0;

    public MortarTileEntity() {
        super(ModTileEntityTypes.MORTAR.get());
    }

    public ActionResultType processInteract(PlayerEntity player, Hand hand) {
        if (this.getWorld() != null && !this.getWorld().isRemote() && !this.isGrinding) {
            ItemStack stack = player.getHeldItem(hand);
            boolean success = false;
            int a = 0;
            if (this.isEmptying) {
                if (!stack.isEmpty() && stack.getItem() == ModItems.EMPTY_POUCH.get()) {
                    if (this.level > 0) {
                        ItemStack outStack = FilledPouchItem.createItem(this.itemStacks);
                        stack.shrink(1);
                        if (stack.isEmpty()) {
                            player.setHeldItem(hand, outStack);
                        } else if (!player.addItemStackToInventory(outStack)) {
                            player.dropItem(outStack, false);
                        }
                        success = true;
                        a = -1;
                    }
                }
            }
            else {
                if (stack.isEmpty()) {
                    if (player.isSneaking()) {
                        if (this.level < 4 && (this.level > 0 || ModRecipeTypes.isPouchValid(player, this.itemStacks))) {
                            this.grind();
                            success = true;
                        }
                    }
                    else if (this.level < 1) {
                        int i = this.itemStacks.size() - 1;
                        if (i > -1) {
                            player.setHeldItem(hand, this.itemStacks.remove(i));
                            success = true;
                        }
                    }
                }
                else if (this.level < 1) {
                    if (this.itemStacks.size() < IPouchRecipe.MAX_POUCH_SIZE) {
                        this.itemStacks.add(stack.split(1));
                        success = true;
                    }
                }
            }

            if (success) {
                this.changeFillLevel(a);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.CONSUME;
    }

    @Override
    public void tick() {
        if (this.isGrinding) {
            this.grindTime++;
            if (this.grindTime > 20) {
                this.isGrinding = false;
                this.grindTime = 0;

                if (!this.getWorld().isRemote()) {
                    this.changeFillLevel(4);
                }
            }
        }
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        if (id == 1) {
            this.isGrinding = true;
            this.grindTime = 0;
            return true;
        }
        return super.receiveClientEvent(id, type);
    }

    private void grind() {
        this.isGrinding = true;
        this.grindTime = 0;
        this.getWorld().addBlockEvent(this.getPos(), this.getBlockState().getBlock(), 1, 0);
    }

    private void changeFillLevel(int a) {
        this.level = Math.max(Math.min(4, this.level + a), 0);
        BlockState oldState = this.getBlockState();
        BlockState state = oldState.with(MortarBlock.FILL, this.level);
        this.getWorld().setBlockState(this.getPos(), state, 3);
        this.getWorld().notifyBlockUpdate(this.getPos(), oldState, state, 3);
        if (this.level > 3 && a > 0) {
            this.isEmptying = true;
        }
        else if (this.level < 1 && a < 0) {
            this.isEmptying = false;
            this.itemStacks.clear();
        }
    }

    public NonNullList<ItemStack> getStacks() {
        return this.itemStacks;
    }

    public boolean isGrinding() {
        return this.isGrinding;
    }

    public int getGrindTime() {
        return this.grindTime;
    }

    public boolean isEmptying() {
        return this.isEmptying;
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        this.writeMain(compound);

        compound.putInt("Level", this.level);
        compound.putBoolean("Grinding", this.isGrinding);
        compound.putInt("GrindTime", this.grindTime);

        return compound;
    }

    private void writeMain(CompoundNBT compound) {
        super.write(compound);

        ListNBT list = new ListNBT();
        for (ItemStack stack : this.itemStacks) {
            list.add(stack.write(new CompoundNBT()));
        }
        compound.put("Items", list);
        compound.putBoolean("Emptying", this.isEmptying);
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        this.readMain(compound);

        this.level = compound.getInt("Level");
        this.isGrinding = compound.getBoolean("Grinding");
        this.grindTime = compound.getInt("GrindTime");
    }

    private void readMain(CompoundNBT compound) {
        super.read(null, compound);

        this.itemStacks.clear();
        ListNBT listNBT = compound.getList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < Math.min(listNBT.size(), IPouchRecipe.MAX_POUCH_SIZE); i++) {
            ItemStack stack = ItemStack.read(listNBT.getCompound(i));
            if (!stack.isEmpty()) {
                this.itemStacks.add(i, stack);
            }
        }
        this.isEmptying = compound.getBoolean("Emptying");
    }

    @Nonnull
    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt =  new CompoundNBT();
        this.writeMain(nbt);
        return nbt;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.readMain(pkt.getNbtCompound());
    }
}
