package com.bigchickenstudios.divinature.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;

public class FilledPouchItem extends Item {

    public FilledPouchItem(Properties properties) {
        super(properties);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        if (stack.hasTag()) {
            CompoundNBT nbt = stack.getOrCreateTag();
            NonNullList<ItemStack> stacks = readContents(nbt);
            if (!stacks.isEmpty()) {
                tooltip.add(new TranslationTextComponent("pouch.divinature.contains").mergeStyle(TextFormatting.GOLD));
                for (ItemStack s : stacks) {
                    tooltip.add(new StringTextComponent("- ").mergeStyle(TextFormatting.DARK_AQUA).append(s.getItem().getDisplayName(s)));
                }
            }
        }
    }

    public static NonNullList<ItemStack> readContents(CompoundNBT nbt) {
        NonNullList<ItemStack> out = NonNullList.create();
        ListNBT list = nbt.getList("PouchItems", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            ItemStack stack = ItemStack.read(list.getCompound(i));
            if (!stack.isEmpty()) {
                out.add(stack);
            }
        }
        return out;
    }

    public static void writeContents(NonNullList<ItemStack> stacks, CompoundNBT nbt) {
        ListNBT list = new ListNBT();
        for (ItemStack stack : stacks) {
            if (!stack.isEmpty()) {
                list.add(stack.write(new CompoundNBT()));
            }
        }
        nbt.put("PouchItems", list);
    }

    public static ItemStack createItem(NonNullList<ItemStack> stacks) {
        ItemStack out = new ItemStack(ModItems.FILLED_POUCH.get(), 1);
        writeContents(stacks, out.getOrCreateTag());
        return out;
    }
}
