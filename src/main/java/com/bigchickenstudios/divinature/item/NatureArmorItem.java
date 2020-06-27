package com.bigchickenstudios.divinature.item;

import com.bigchickenstudios.divinature.client.renderer.entity.model.NatureArmorModel;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class NatureArmorItem extends ArmorItem {

    private static final UUID[] ARMOR_MODIFIERS = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};
    private static final int[] BASE_ARMOR = new int[]{1, 3, 4, 2};

    public NatureArmorItem(EquipmentSlotType slotType, Properties properties) {
        super(MATERIAL, slotType, properties);
    }

    @Nullable
    @Override
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        return NatureArmorModel.get(armorSlot, _default);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, @Nonnull List<ITextComponent> tooltip, ITooltipFlag flag) {

    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        Multimap<String, AttributeModifier> map = HashMultimap.create();
        if (slot == this.slot) {
            int a = BASE_ARMOR[slot.getIndex()];
            int t = 0;
            CompoundNBT tag = stack.getTag();
            if (tag != null && tag.contains("NatureArmor", Constants.NBT.TAG_COMPOUND)) {
                CompoundNBT armor = tag.getCompound("NatureArmor");
                a += armor.getInt("Armor");
                t += armor.getInt("Toughness");
            }
            map.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(ARMOR_MODIFIERS[slot.getIndex()], "Armor modifier", a, AttributeModifier.Operation.ADDITION));
            map.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(ARMOR_MODIFIERS[slot.getIndex()], "Armor toughness", t, AttributeModifier.Operation.ADDITION));
        }
        return map;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    private static final IArmorMaterial MATERIAL = new IArmorMaterial() {
        @Override
        public int getDurability(@Nonnull EquipmentSlotType slotIn) {
            return 0;
        }

        @Override
        public int getDamageReductionAmount(@Nonnull EquipmentSlotType slotIn) {
            return 0;
        }

        @Override
        public int getEnchantability() {
            return 0;
        }

        @Nonnull
        @Override
        public SoundEvent getSoundEvent() {
            return SoundEvents.ITEM_ARMOR_EQUIP_LEATHER;
        }

        @Nonnull
        @Override
        public Ingredient getRepairMaterial() {
            return Ingredient.EMPTY;
        }

        @Nonnull
        @Override
        public String getName() {
            return "divinature:nature";
        }

        @Override
        public float getToughness() {
            return 0;
        }
    };
}
