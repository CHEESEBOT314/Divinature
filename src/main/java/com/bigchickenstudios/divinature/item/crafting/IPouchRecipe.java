package com.bigchickenstudios.divinature.item.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.RecipeMatcher;

import java.util.List;

public interface IPouchRecipe {

    int MAX_POUCH_SIZE = 9;

    NonNullList<Ingredient> getPouch();

    default boolean matchesPouch(NonNullList<ItemStack> pouch) {
        return RecipeMatcher.findMatches(pouch, this.getPouch()) != null;
    }

    static NonNullList<Ingredient> readPouch(JsonArray jsonArray) {
        NonNullList<Ingredient> pouch = NonNullList.create();
        for (JsonElement e : jsonArray) {
            Ingredient ingredient = Ingredient.deserialize(e);
            if (ingredient.hasNoMatchingItems()) {
                throw new IllegalArgumentException("Pouch ingredient has no matching stacks");
            }
            pouch.add(ingredient);
        }
        if (pouch.isEmpty()) {
            throw new IllegalArgumentException("Pouch has no ingredients");
        }
        if (pouch.size() > MAX_POUCH_SIZE) {
            throw new IllegalArgumentException("Pouch has too many ingredients");
        }
        return pouch;
    }

    static NonNullList<Ingredient> readPouch(PacketBuffer buffer) {
        int count = buffer.readVarInt();
        NonNullList<Ingredient> pouch = NonNullList.withSize(count, Ingredient.EMPTY);
        for (int i = 0; i < count; i++) {
            pouch.set(i, Ingredient.read(buffer));
        }
        return pouch;
    }

    static void writePouch(NonNullList<Ingredient> pouch, PacketBuffer buffer) {
        buffer.writeVarInt(pouch.size());
        for (Ingredient i : pouch) {
            i.write(buffer);
        }
    }
}
