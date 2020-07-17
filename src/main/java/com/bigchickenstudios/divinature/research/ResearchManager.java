package com.bigchickenstudios.divinature.research;

import com.google.common.collect.Maps;
import com.google.gson.*;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;

public class ResearchManager extends JsonReloadListener {

    private static ResearchManager INSTANCE;

    public static void init() {
        MinecraftForge.EVENT_BUS.addListener(ResearchManager::onReload);
    }

    public static ResearchManager getInstance() {
        return INSTANCE;
    }

    private static void onReload(AddReloadListenerEvent event) {
        event.addListener(INSTANCE = new ResearchManager());
    }

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = (new GsonBuilder()).create();

    private ResearchList researchList = new ResearchList();

    private ResearchManager() {
        super(GSON, "divinature/research");
    }

    @Override
    protected void apply(@Nonnull Map<ResourceLocation, JsonElement> object, @Nonnull IResourceManager resourceManager, @Nonnull IProfiler profiler) {
        Map<ResourceLocation, Research.Builder> map = Maps.newHashMap();
        object.forEach((rl, je) -> {
            try {
                JsonObject json = JSONUtils.getJsonObject(je, "research");
                map.put(rl, Research.Builder.deserialize(json));
            }
            catch (JsonParseException | IllegalArgumentException e) {
                LOGGER.error("Error loading research {}: {}", rl.toString(), e.getMessage());
            }
        });
        ResearchList researchListTemp = new ResearchList();
        researchListTemp.loadResearch(map);
        this.researchList = researchListTemp;

        PlayerResearch.reloadAll(this);
    }

    public Research getResearch(ResourceLocation id) {
        return this.researchList.getResearch(id);
    }

    public Collection<Research> getAllResearch() {
        return this.researchList.getAll();
    }
}
