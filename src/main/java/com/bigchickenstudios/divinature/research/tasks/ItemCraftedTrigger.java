package com.bigchickenstudios.divinature.research.tasks;

import com.bigchickenstudios.divinature.research.PlayerResearch;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class ItemCraftedTrigger extends TaskTrigger<PlayerEvent.ItemCraftedEvent, ItemCraftedTrigger.Instance> {

    public ItemCraftedTrigger(ResourceLocation id) {
        super(id);
        MinecraftForge.EVENT_BUS.addListener(this::trigger);
    }

    public void trigger(PlayerEvent.ItemCraftedEvent event) {
        if (event.getPlayer() instanceof ServerPlayerEntity) {
            this.test(PlayerResearch.getPlayer((ServerPlayerEntity)event.getPlayer()), event);
        }
    }

    @Override
    protected Instance deserializeInstance(JsonObject jsonObject) {
        if (!jsonObject.has("result")) {
            throw new IllegalArgumentException("No result field for ItemCraftedTrigger!");
        }
        return new Instance(Ingredient.deserialize(jsonObject.get("result")));
    }

    public static class Instance implements ITaskInstance<PlayerEvent.ItemCraftedEvent> {

        private final Ingredient ingredient;

        private Instance(Ingredient ingredientIn) {
            this.ingredient = ingredientIn;
        }

        @Override
        public boolean test(PlayerEvent.ItemCraftedEvent itemCraftedEvent) {
            return this.ingredient.test(itemCraftedEvent.getCrafting());
        }
    }
}
