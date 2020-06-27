package com.bigchickenstudios.divinature.tileentity;

import com.bigchickenstudios.divinature.block.InfuserBlock;
import com.bigchickenstudios.divinature.item.FilledPouchItem;
import com.bigchickenstudios.divinature.item.crafting.InfuserRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class InfuserTileEntity extends TileEntity implements ITickableTileEntity {

    private ItemStack top = ItemStack.EMPTY;
    private int colour = 0xFFFFFF;

    private NonNullList<ItemStack> pouch = NonNullList.create();
    private InfuserRecipe recipe;
    private int infuseTime = 0;

    public InfuserTileEntity() {
        super(ModTileEntityTypes.INFUSER.get());
    }

    @Override
    public void tick() {
        if (this.isActive() && this.getWorld() != null && !this.getWorld().isRemote()) {
            if (this.recipe == null) {
                if (!this.loadRecipe(this.pouch, false)) {
                    this.pouch.clear();
                    this.update(this.getBlockState().with(InfuserBlock.LIT, false));
                }
            }
            else {
                this.infuseTime--;
                if (this.infuseTime == 0) {
                    this.top = this.recipe.getRecipeOutput().copy();
                    this.pouch.clear();
                    this.recipe = null;
                    this.update(this.getBlockState().with(InfuserBlock.LIT, false));
                }
            }
        }
    }

    private boolean isActive() {
        return this.infuseTime > 0;
    }

    private boolean loadRecipe(NonNullList<ItemStack> pouchIn, boolean setTime) {
        Optional<InfuserRecipe> optionalInfuserRecipe = InfuserRecipe.findMatch(this.top, pouchIn, this.getWorld());
        if (optionalInfuserRecipe.isPresent()) {
            this.recipe = optionalInfuserRecipe.get();
            if (this.infuseTime > this.recipe.getTime() || setTime) {
                this.infuseTime = this.recipe.getTime();
            }
            return true;
        }
        this.infuseTime = 0;
        return false;
    }

    public ActionResultType processInteract(PlayerEntity player, Hand hand, DoubleBlockHalf half) {
        if (this.getWorld() != null && !this.getWorld().isRemote() && !this.isActive()) {
            ItemStack stack = player.getHeldItem(hand);
            boolean changed = false;
            BlockState to = this.getBlockState();
            if (half == DoubleBlockHalf.UPPER) {
                if (this.top.isEmpty()) {
                    if (!stack.isEmpty()) {
                        this.top = stack.copy();
                        this.top.setCount(1);
                        if (!player.abilities.isCreativeMode) {
                            stack.shrink(1);
                        }
                        changed = true;
                    }
                } else {
                    if (stack.isEmpty()) {
                        player.setHeldItem(hand, this.top);
                    } else if (!player.addItemStackToInventory(this.top)) {
                        player.dropItem(this.top, false);
                    }
                    this.top = ItemStack.EMPTY;
                    changed = true;
                }
            }
            else {
                if (!stack.isEmpty() && stack.getItem() instanceof FilledPouchItem) {
                    if (this.loadRecipe(FilledPouchItem.readContents(stack.getOrCreateTag()), true)) {
                        stack.shrink(1);
                        changed = true;
                        to = this.getBlockState().with(InfuserBlock.LIT, true);
                    }
                }
            }

            if (changed) {
                this.update(to);
                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.CONSUME;
    }

    private void update(BlockState state) {
        if (this.getWorld() != null) {
            BlockState oldState = this.getBlockState();
            this.getWorld().setBlockState(this.pos, state, 3);
            this.getWorld().notifyBlockUpdate(this.pos, oldState, state, 3);
        }
        this.markDirty();
    }

    public ItemStack getTopItemStack() {
        return this.top;
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        this.writeMain(compound);

        FilledPouchItem.writeContents(this.pouch, compound);
        compound.putInt("InfuseTime", this.infuseTime);

        return compound;
    }

    private void writeMain(CompoundNBT compound) {
        super.write(compound);
        compound.put("TopItem", this.top.write(new CompoundNBT()));
        compound.putInt("Colour", this.colour);
    }

    @Override
    public void read(CompoundNBT compound) {
        this.readMain(compound);

        this.pouch = FilledPouchItem.readContents(compound);
        this.infuseTime = compound.getInt("InfuseTime");
    }

    private void readMain(CompoundNBT compound) {
        super.read(compound);
        this.top = ItemStack.read(compound.getCompound("TopItem"));
        this.colour = compound.getInt("Colour");
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

    public int getColour() {
        return this.colour;
    }
}
