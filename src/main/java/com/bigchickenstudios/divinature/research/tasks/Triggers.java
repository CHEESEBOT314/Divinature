package com.bigchickenstudios.divinature.research.tasks;

import com.bigchickenstudios.divinature.Strings;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public final class Triggers {

    private static final Map<ResourceLocation, TaskTrigger<?, ?>> REGISTRY = Maps.newHashMap();

    public static final ItemCraftedTrigger ITEM_CRAFTED = register(new ItemCraftedTrigger(Strings.createResourceLocation("item_crafted")));

    public static <T extends TaskTrigger<?, ?>> T register(T t) {
        if (REGISTRY.containsKey(t.getId())) {
            throw new IllegalArgumentException("Duplicate trigger id: " + t.getId().toString());
        }
        REGISTRY.put(t.getId(), t);
        return t;
    }

    public static TaskTrigger<?, ?> get(ResourceLocation id) {
        return REGISTRY.get(id);
    }

    public static Iterable<TaskTrigger<?, ?>> getAll() {
        return REGISTRY.values();
    }


    private Triggers() {}
}
