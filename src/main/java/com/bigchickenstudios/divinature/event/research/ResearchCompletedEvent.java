package com.bigchickenstudios.divinature.event.research;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

public class ResearchCompletedEvent extends Event {

    private final List<ResourceLocation> added;

    public ResearchCompletedEvent(List<ResourceLocation> addedIn) {
        this.added = ImmutableList.copyOf(addedIn);
    }

    public List<ResourceLocation> getResearch() {
        return this.added;
    }

    public static void post(List<ResourceLocation> research) {
        MinecraftForge.EVENT_BUS.post(new ResearchCompletedEvent(research));
    }
}
